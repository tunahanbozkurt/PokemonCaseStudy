package com.example.pokemoncasestudy.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.util.BACK_BITMAP
import com.example.pokemoncasestudy.util.FRONT_BITMAP

@SuppressLint("InflateParams")
class PopUpWindow(val context: Context, data: PopUpWindowData) {

    private val popUpWindow: View
    private var popUpViewGroup: ViewGroup? = null
    private var params: WindowManager.LayoutParams? = null
    private val windowManager: WindowManager
    private val layoutInflater: LayoutInflater

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popUpWindow = layoutInflater.inflate(R.layout.pop_up_window, null)

        val pokemonNameText = popUpWindow.findViewById<TextView>(R.id.pokemon_name)
        val frontImageView = popUpWindow.findViewById<ImageView>(R.id.image_front)
        val backImageView =  popUpWindow.findViewById<ImageView>(R.id.image_back)
        pokemonNameText.text = context.getString(R.string.pokemon_name, data.pokemonName)

        val bitmapList = readBitmapsFromInternalStorage(context)
        frontImageView.setImageBitmap(bitmapList[0])
        backImageView.setImageBitmap(bitmapList[1])

        popUpWindow.findViewById<Button>(R.id.remove_overlay).setOnClickListener {
            close()
        }

        popUpViewGroup = if (popUpWindow.parent != null ) popUpWindow.parent as ViewGroup else null

        params?.gravity = Gravity.CENTER
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    /**
     * Opens popUpWindow
     */
    fun open() {

        try {
            if (popUpWindow.windowToken == null) {
                if (popUpWindow.parent == null) {
                    windowManager.addView(popUpWindow, params)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    /**
     * Closes popUpWindow
     */
    fun close() {
        context.stopService(Intent(context, OverlayForegroundService::class.java))
        try {
            windowManager.removeView(popUpWindow)
            popUpWindow.invalidate()
            popUpViewGroup?.removeAllViews()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun readBitmapsFromInternalStorage(context: Context): List<Bitmap?> {

    val frontBitmap = BitmapFactory.decodeStream(context.openFileInput(FRONT_BITMAP))
    val backBitmap = BitmapFactory.decodeStream(context.openFileInput(BACK_BITMAP))
    return if (frontBitmap != null && backBitmap != null){
        listOf(frontBitmap, backBitmap)
    }else{
        listOf()
    }
}

data class PopUpWindowData(
    val pokemonName: String?
)