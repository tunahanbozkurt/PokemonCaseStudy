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


class OverlayForegroundService: Service() {

    var pokemonName: String? = null
    var popUpWindow: PopUpWindow? = null

    override fun onBind(intent: Intent?): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
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
            pokemonName = intent?.getStringExtra("POKEMON_NAME")

        }catch (e: Exception){}

        if (popUpWindow == null){
            popUpWindow = PopUpWindow(this, PopUpWindowData(pokemonName = pokemonName))
            popUpWindow!!.open()
        }else {
            popUpWindow!!.close()
            popUpWindow = PopUpWindow(this, PopUpWindowData(pokemonName = pokemonName))
            popUpWindow!!.open()
        }


        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun foregroundService() {
        val NOTIFICATION_CHANNEL_ID = "overlay_channel"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_MIN
        )
        val manager =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)!!
        manager.createNotificationChannel(chan)
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
}