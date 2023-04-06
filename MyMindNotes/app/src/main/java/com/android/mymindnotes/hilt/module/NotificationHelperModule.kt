package com.android.mymindnotes.hilt.module

import com.android.mymindnotes.domain.NotificationHelper
import com.android.mymindnotes.presentation.NotificationHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationHelperModule {
    @Singleton
    @Binds
    abstract fun bindNotificationHelper (
        notificationHelperImpl: NotificationHelperImpl
    ): NotificationHelper
}