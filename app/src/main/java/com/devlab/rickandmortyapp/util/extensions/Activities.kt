package com.devlab.rickandmortyapp.util.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelLazy
import com.devlab.rickandmortyapp.R
import com.devlab.rickandmortyapp.base.BaseActivity
import com.devlab.rickandmortyapp.base.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

fun <DB : ViewDataBinding, VM : BaseViewModel> BaseActivity<DB, VM>.viewModel(): Lazy<VM> {
    return lazy {
        val clazz =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>

        ViewModelLazy(clazz.kotlin, { viewModelStore }, { defaultViewModelProviderFactory }).value
    }
}

fun FragmentActivity.returnPage(clazz: KClass<out Fragment>) {
    val fm = supportFragmentManager
    fm.popBackStack(clazz.java.simpleName, 0)
}

fun <T> FragmentActivity.returnResult(data: T) {
    val i = Intent()

    val bundle = Bundle()
    bundle.putSerializable("data", data as Serializable)

    i.putExtras(bundle)

    setResult(Activity.RESULT_OK, i)
    finish()
}

fun FragmentActivity.startActivity(kClass: KClass<out FragmentActivity>, bundle: Bundle?) {
    val intent = Intent(this, kClass.java)

    bundle?.let { intent.putExtras(bundle) }

    startActivity(intent)
}