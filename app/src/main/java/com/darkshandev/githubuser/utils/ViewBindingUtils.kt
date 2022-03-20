package com.darkshandev.githubuser.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter(("resname"))
fun loadImageDrawable(view: ImageView, name: String) {
    name.let {
        view.loadCircleImage(it)
    }
}
