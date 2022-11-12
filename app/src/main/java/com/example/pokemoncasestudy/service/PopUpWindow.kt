package com.example.pokemoncasestudy.service

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.pokemoncasestudy.R

class PopUpWindow(val context: Context, data: PopUpWindowData) {

    private val popUpWindow: View
    private var popUpViewGroup: ViewGroup? = null
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

        popUpViewGroup = if (popUpWindow.parent != null ) popUpWindow.parent as ViewGroup else null

        mParams!!.gravity = Gravity.CENTER
        mWindowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    fun open() {

        try {
            if (popUpWindow.windowToken == null) {
                if (popUpWindow.parent == null) {
                    mWindowManager.addView(popUpWindow, mParams)
                }
            }
        } catch (e: Exception) {

        }
    }

    fun close() {
        context.stopService(Intent(context, OverlayForegroundService::class.java))
        try {
            mWindowManager.removeView(popUpWindow)
            popUpWindow.invalidate()
            popUpViewGroup?.removeAllViews()
        } catch (e: Exception) {

        }
    }
}

data class PopUpWindowData(
    val pokemonName: String?
)