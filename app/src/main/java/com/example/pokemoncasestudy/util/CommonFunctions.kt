package com.example.pokemoncasestudy.util

import android.app.Activity
import android.content.Intent

/**
 * Brings the related activity to the front of screen
 */
fun Activity.bringToFront() {
    this.startActivity(Intent(this, this::class.java))
}