package com.devlab.rickandmortyapp.base

import android.text.TextUtils
import androidx.annotation.CallSuper
import com.devlab.rickandmortyapp.R
import com.devlab.rickandmortyapp.repository.base.ErrorModel
import com.devlab.rickandmortyapp.util.Strings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

abstract class BaseUseCase<Response, Params>(vararg cases: BaseUseCase<*, *>) {

    @Inject
    lateinit var strings: Strings

    private val requestTag = this.javaClass.simpleName

    private var useCases: Array<BaseUseCase<*, *>> = arrayOf(*cases)

    private var success: ((Response) -> Unit)? = null
    private var error: ((ErrorModel) -> Unit)? = null

    /**
     * ViewModel kapanması sonrasında içerisindeki RxJava çağrılarının döndürdüğü
     * disposable'ların temizlenmesi için tutulan değişken
     */
    private val compositeDisposable = CompositeDisposable()

    private var retryTask: (() -> Observable<Response>)? = null

    abstract fun on(params: Params? = null)

    /**
     * Servis cagrisinde servisi belirtmek icin kullanilir
     */
    @CallSuper
    fun on(
        params: Params? = null,
        success: ((Response) -> Unit)? = null,
        error: ((ErrorModel) -> Unit)? = null
    ) {
        this.success = success
        this.error = error

        clear()

        on(params)
    }

    /**
     * Lifecycle tamamlandığında silinecek disposable'lar tutulur
     *
     * @param disposable disposable
     */
    private fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * Memory leak önlenmesi için disposable'lar silinir.
     */
    @CallSuper
    open fun clear() {
        compositeDisposable.clear()
    }

    fun retry() {
        retryTask?.let { sendRequest { it() } }
    }

    fun sendRequest(task: () -> Observable<Response>) {
        clear()

        add(
            task()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getResponseListener())
        )
    }

    private fun execute() {

    }

    open fun onSendError(error: ErrorModel) {
        this.error?.invoke(error)
    }

    open fun onSendSuccess(t: Response) {
        this.success?.invoke(t)
    }

    private fun getResponseListener(): DisposableObserver<Response> {
        return object : DisposableObserver<Response>() {
            override fun onComplete() {
            }

            override fun onNext(t: Response) {
                onSendSuccess(t)
            }

            @CallSuper
            override fun onError(e: Throwable) {
                when (e) {
                    is ConnectException,
                    is SocketTimeoutException,
                    is SSLHandshakeException -> {
                        onSendError(ErrorModel(strings.getString(R.string.connect_error)))
                    }
                    is HttpException -> {
                        onSendError(ErrorModel(e.code().toString()))
                    }
                    is Exception -> {
                        onSendError(
                            ErrorModel(
                                if (!TextUtils.isEmpty(e.message)) {
                                    e.message!!
                                } else {
                                    strings.getString(R.string.an_error_occurred)
                                }
                            )
                        )
                    }
                    is ErrorModel -> {
                        onSendError(e)
                    }
                    else -> {
                        onSendError(ErrorModel(strings.getString(R.string.an_error_occurred)))
                    }
                }
            }

        }
    }
}
