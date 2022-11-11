package com.example.pokemoncasestudy.service

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.pokemoncasestudy.R

class PopUpWindow(val context: Context, data: PopUpWindowData) {

    private val popUpWindow: View
    private var mParams: WindowManager.LayoutParams? = null
    private val mWindowManager: WindowManager
    private val layoutInflater: LayoutInflater

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popUpWindow = layoutInflater.inflate(R.layout.pop_up_window, null)

        popUpWindow.findViewById<TextView>(R.id.pokemon_name).text = data.pokemonName

        val frontBitmap = BitmapFactory.decodeStream(context.openFileInput("frontBitmap"))
        popUpWindow.findViewById<ImageView>(R.id.image_front).setImageBitmap(frontBitmap)
        val backBitmap = BitmapFactory.decodeStream(context.openFileInput("backBitmap"))
        popUpWindow.findViewById<ImageView>(R.id.image_back).setImageBitmap(backBitmap)

        popUpWindow.findViewById<Button>(R.id.remove_overlay).setOnClickListener {
            close()
        }

        mParams!!.gravity = Gravity.CENTER
        mWindowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    fun open() {
        remove()
        try {
            if (popUpWindow.windowToken == null) {
                if (popUpWindow.parent == null) {
                    mWindowManager.addView(popUpWindow, mParams)
                }
            }
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }

    fun close() {
        context.stopService(Intent(context, OverlayForegroundService::class.java))
        try {
            (context.getSystemService(WINDOW_SERVICE) as WindowManager).removeView(popUpWindow)
            popUpWindow.invalidate()
            (popUpWindow.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }

    fun remove() {
        try {
            (context.getSystemService(WINDOW_SERVICE) as WindowManager).removeView(popUpWindow)
            popUpWindow.invalidate()
            (popUpWindow.parent as ViewGroup).removeAllViews()

        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }
}

data class PopUpWindowData(
    val pokemonName: String?
)