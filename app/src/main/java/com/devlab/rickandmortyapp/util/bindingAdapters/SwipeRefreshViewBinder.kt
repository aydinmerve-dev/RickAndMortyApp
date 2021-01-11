package com.devlab.rickandmortyapp.util.bindingAdapters

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class SwipeRefreshViewBinder {

    companion object {

        @JvmStatic
        @BindingAdapter("isRefreshing")
        fun setIsRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean) {
            view.isRefreshing = isRefreshing
        }
    }
}