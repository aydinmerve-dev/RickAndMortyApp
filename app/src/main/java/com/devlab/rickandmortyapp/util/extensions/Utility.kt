package com.devlab.rickandmortyapp.util.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import kotlin.math.roundToInt

fun FragmentActivity.hideKeyboard() {
    try {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    } catch (e: java.lang.Exception) {
    }
}