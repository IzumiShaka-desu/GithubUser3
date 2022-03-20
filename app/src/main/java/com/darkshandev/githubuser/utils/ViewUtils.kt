package com.darkshandev.githubuser.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.darkshandev.githubuser.R


fun ImageView.loadCircleImage(url: String) {
    val defaultOption = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .error(R.drawable.ic_broken_image)
        .circleCrop()
    if (url.isEmpty()) {
        Glide.with(context)
            .setDefaultRequestOptions(defaultOption)
            .load(
                ColorDrawable(ContextCompat.getColor(context, R.color.grey))
            )
            .into(this)
    } else {


        Glide.with(context)
            .setDefaultRequestOptions(defaultOption)
            .load(
                url
            )
            .into(this)
    }
}

fun getBitmapFromView(view: View): Bitmap? {
    val bitmap =
        Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}
