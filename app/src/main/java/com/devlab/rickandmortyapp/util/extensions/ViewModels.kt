package com.devlab.rickandmortyapp.util.extensions

import androidx.fragment.app.Fragment
import com.devlab.rickandmortyapp.base.BaseViewModel
import kotlin.reflect.KClass

fun BaseViewModel.goBack() {
    viewListener?.goBack()
}

fun BaseViewModel.returnPage(clazz: KClass<out Fragment>) {
    viewListener?.returnPage(clazz)
}

fun <T> BaseViewModel.returnResult(data: T) {
    viewListener?.returnResult(data)
}
