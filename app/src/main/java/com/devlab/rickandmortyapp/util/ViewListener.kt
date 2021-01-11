package com.devlab.rickandmortyapp.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.reflect.KClass

interface ViewListener {

    fun goBack()

    fun returnPage(clazz: KClass<out Fragment>)

    fun <T> returnResult(data: T)

    fun startActivity(kClass: KClass<out FragmentActivity>, bundle: Bundle? = null)

    fun finishActivity()

    fun showToast(message:String)
}