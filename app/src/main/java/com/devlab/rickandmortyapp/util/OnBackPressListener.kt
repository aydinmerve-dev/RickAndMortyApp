package com.devlab.rickandmortyapp.util

interface OnBackPressListener {

    /**
     * Eger isBackEnable durumunda ise viewModel'in onBackPress methodu cagirilabilir demektir.
     * Default olarak fragment tarafinda true olarak set edilecektir.
     * Activity'nin setOnBackPressListener methoduna register olmak istemeyen ozellikle child
     * fragment'lar icin bu deger false set edilmelidir.
     */
    fun isBackEnable(): Boolean

    fun onBackPressed(): Boolean
}