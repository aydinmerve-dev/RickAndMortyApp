package com.devlab.rickandmortyapp.util.bindingAdapters

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class ImageViewBinder {

    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl", "placeHolder", requireAll = false)
        fun loadRemoteImage(view: ImageView, imageUrl: String?, placeholder: Drawable?) {
            Glide.with(view.context)
                .load(imageUrl)
                .placeholder(placeholder)
                .apply(RequestOptions().transform(RoundedCornersTransformation(
                    16,
                    0,
                    RoundedCornersTransformation.CornerType.ALL
                )))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(view)
        }
    }
}