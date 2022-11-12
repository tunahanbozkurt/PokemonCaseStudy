package com.example.pokemoncasestudy.util

import android.app.Activity
import android.content.Intent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

/**
 * Brings the related activity to the front of screen
 */
fun Activity.bringToFront() {
    this.startActivity(Intent(this, this::class.java))
}

/**
 * Checks connection by sending ping to google servers.
 * Very fast and reliable
 */
suspend fun isOnline(dispatcher: CoroutineDispatcher): Boolean {
    return withContext(dispatcher) {
        try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddress: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddress, timeoutMs)
            sock.close()
            return@withContext true

        } catch (e: IOException) {
            return@withContext false
        }
    }

}