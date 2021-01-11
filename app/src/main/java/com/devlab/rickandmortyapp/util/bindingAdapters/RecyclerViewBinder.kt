package com.devlab.rickandmortyapp.util.bindingAdapters

import androidx.databinding.BindingAdapter
import com.todkars.shimmer.ShimmerRecyclerView

class RecyclerViewBinder {

    companion object {

        @JvmStatic
        @BindingAdapter("shimmerVisibility")
        fun setShimmerVisibility(view: ShimmerRecyclerView, isShow: Boolean) {
            if (isShow) view.showShimmer() else view.hideShimmer()
        }
    }
}