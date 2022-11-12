package com.example.pokemoncasestudy.service


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.pokemoncasestudy.R
import com.example.pokemoncasestudy.util.CHANNEL_NAME
import com.example.pokemoncasestudy.util.NOTIFICATION_CHANNEL_ID
import com.example.pokemoncasestudy.util.POKEMON_NAME


class OverlayForegroundService: Service() {

    var pokemonName: String? = null
    var popUpWindow: PopUpWindow? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) foregroundService() else startForeground(
            1,
            Notification()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            pokemonName = intent?.getStringExtra(POKEMON_NAME)

        }catch (e: Exception){
            e.printStackTrace()
        }
        
        if (popUpWindow == null){
            openWindow()
        }
        else {
            removeWindow()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun foregroundService() {

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_MIN
        )
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)
        manager?.createNotificationChannel(channel)

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Service running")
            .setContentText("Displaying over other apps")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    private fun openWindow() {
        popUpWindow = PopUpWindow(this, PopUpWindowData(pokemonName = pokemonName))
        popUpWindow!!.open()
    }

    private fun removeWindow() {
        popUpWindow!!.close()
        popUpWindow = PopUpWindow(this, PopUpWindowData(pokemonName = pokemonName))
        popUpWindow!!.open()
    }
}