package com.android.mymindnotes.domain

import android.app.NotificationManager
import androidx.core.app.NotificationCompat

interface NotificationHelper {
    fun createChannel()
    fun getManager(): NotificationManager
    fun getChannelNotification(): NotificationCompat.Builder
}