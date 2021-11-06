package com.example.dare

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class PedometerService : Service() {
    var steps = 0

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        steps = intent!!.getIntExtra("steps", 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.run)
            .setContentTitle("Speed")
            .setContentText(steps.toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notification: Notification = builder.build()
        startForeground(notificationId, notification)

        /*with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification)
        }*/
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}