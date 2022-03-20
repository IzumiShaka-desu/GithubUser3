package com.darkshandev.githubuser.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.darkshandev.githubuser.R


fun ImageView.loadCircleImage(name: String) {
    val defaultOption = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .error(R.drawable.ic_broken_image)
        .circleCrop()
    Glide.with(context)
        .setDefaultRequestOptions(defaultOption)
        .load(
            resources.getIdentifier(
                name,
                "drawable",
                context.packageName
            )
        )
        .into(this)
}

fun getBitmapFromView(view: View): Bitmap? {
    var bitmap =
        Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    var canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}