package com.devlab.rickandmortyapp.util.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.devlab.rickandmortyapp.base.BaseActivity
import com.devlab.rickandmortyapp.base.BaseUseCase
import com.devlab.rickandmortyapp.base.BaseViewModel
import com.devlab.rickandmortyapp.util.ViewListener
import javax.inject.Inject
import kotlin.reflect.KClass

fun BaseViewModel.setUseCasesListener() {
    val viewModel = this

    this::class.java.fields.forEach {
        if (it != null && it.isAnnotationPresent(Inject::class.java)) {
            val obj = it.get(this)
            if (obj is BaseUseCase<*, *>) {
                useCases.add(obj)
            }
        }
    }
}

fun BaseActivity<*, *>.setViewListener() {
    val baseActivity = this

    viewModel.viewListener = object : ViewListener {
        override fun finishActivity() {
            baseActivity.finish()
        }

        override fun startActivity(kClass: KClass<out FragmentActivity>, bundle: Bundle?) {
            baseActivity.startActivity(kClass, bundle)
        }

        override fun returnPage(clazz: KClass<out Fragment>) {
            baseActivity.returnPage(clazz)
        }

        override fun <T> returnResult(data: T) {
            baseActivity.returnResult(data)
        }

        override fun goBack() {
            onBackPressed()
        }

        override fun showToast(message: String) {
            baseActivity.showToast(message)
        }
    }
}
