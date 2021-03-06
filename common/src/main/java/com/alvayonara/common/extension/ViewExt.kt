package com.alvayonara.common.extension

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.alvayonara.common.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun EditText?.textToString(): String {
    return this?.text?.toString().orEmpty()
}

/**
 * @param text text of snackbar
 *
 * @return displaying Snackbar
 *
 */
fun View.showErrorSnackbar(
    text: String
) {
    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_LONG).apply {
        setBackgroundTint(ContextCompat.getColor(this.context, R.color.cinnabar))
        setTextColor(ContextCompat.getColor(this.context, R.color.white))
    }
    snackbar.show()
}

/**
 * @param text text of snackbar
 *
 * @return displaying Snackbar
 *
 */
fun View.showSuccessSnackbar(
    text: String
) {
    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_LONG).apply {
        setBackgroundTint(ContextCompat.getColor(this.context, android.R.color.holo_green_light))
        setTextColor(ContextCompat.getColor(this.context, R.color.white))
    }
    snackbar.show()
}

/**
 * @param path url of the image
 * @param errorColor error color
 * @return displaying image from Glide
 *
 */
fun ImageView.cacheImage(path: String, errorColor: Int) {
    Glide.with(this)
        .load(path)
        .placeholder(R.color.dove_gray)
        .transition(DrawableTransitionOptions.withCrossFade(200))
        .error(errorColor)
        .into(this)
}