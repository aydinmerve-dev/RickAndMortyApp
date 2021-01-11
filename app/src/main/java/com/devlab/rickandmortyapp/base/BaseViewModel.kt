package com.devlab.rickandmortyapp.base

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devlab.rickandmortyapp.util.OnBackPressListener
import com.devlab.rickandmortyapp.util.ViewListener
import com.devlab.rickandmortyapp.util.extensions.setUseCasesListener
import kotlin.reflect.KClass

abstract class BaseViewModel(vararg cases: BaseUseCase<*, *>) : ViewModel(),
    OnBackPressListener {

    var arguments: Bundle? = null

    /**
     * Use case'lerde request'leri dispose etmek için use case listesini tutuyoruz
     */
    var useCases = arrayListOf(*cases)

    var fragmentManager: FragmentManager? = null
    var viewListener: ViewListener? = null

    var isLoading = MutableLiveData<Boolean>()

    open fun onCreate(savedInstanceState: Bundle?) {
    }

    @CallSuper
    open fun onViewCreated(savedInstanceState: Bundle?) {
        setUseCasesListener()
    }

    @CallSuper
    open fun onStart() {
    }

    @CallSuper
    open fun onDestroy() {
    }

    @CallSuper
    open fun onResume() {
    }

    @CallSuper
    open fun onPause() {
    }

    @CallSuper
    open fun onSaveInstanceState(outState: Bundle?) {
    }

    /**
     * ViewModel lifecycle'ı sonlandığında çağırılır.
     * RxJava Disposable'larını temizler.
     */
    override fun onCleared() {
        super.onCleared()

        for (useCase in useCases) {
            useCase.clear()
        }
    }

    open fun onDestroyView() {
        for (useCase in useCases) {
            useCase.clear()
        }
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    open fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
    }

    override fun isBackEnable(): Boolean {
        return false
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    fun startActivity(kClass: KClass<out FragmentActivity>, bundle: Bundle? = null) {
        viewListener?.startActivity(kClass, bundle)
    }

    fun finishActivity() {
        viewListener?.finishActivity()
    }

    fun showToast(message: String) {
        viewListener?.showToast(message)
    }

    fun showLoading() {
        isLoading.postValue(true)
    }

    fun hideLoading() {
        isLoading.postValue(false)
    }

}