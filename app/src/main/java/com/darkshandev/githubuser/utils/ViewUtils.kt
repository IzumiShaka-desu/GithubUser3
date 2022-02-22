package com.darkshandev.githubuser.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.darkshandev.githubuser.R

fun ImageView.loadCircleImage(name: String){
    val defaultOption= RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .error(R.drawable.ic_broken_image)
        .circleCrop()
    Glide.with(context)
        .setDefaultRequestOptions(defaultOption)
        .load(
            resources.getIdentifier(
                name,
                "drawable",
                context.packageName)
        ).into(this)
}

