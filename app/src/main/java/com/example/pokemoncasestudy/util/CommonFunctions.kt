package com.example.pokemoncasestudy.util

import android.app.Activity
import android.content.Intent
import android.view.View

/**
 * Brings the related activity to the front of screen
 */
fun Activity.bringToFront() {
    this.startActivity(Intent(this, this::class.java))
}

/**
 * Sets the visibility to visible
 */
fun View.setVisible() {
    this.visibility = View.VISIBLE
}

/**
 * Sets the visibility to gone
 */
fun View.setGone() {
    this.visibility = View.GONE
}