package com.akpdev.movies.common

import android.widget.ImageView
import com.akpdev.movies.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImageWithGlide(url:String?){
    url?.let {
        Glide.with(this).asBitmap().load(it).apply(
            RequestOptions.placeholderOf(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
        ).into(this)
    }
}