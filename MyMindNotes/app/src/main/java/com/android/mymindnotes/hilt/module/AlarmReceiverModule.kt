package com.android.mymindnotes.hilt.module

import com.android.mymindnotes.domain.AlarmReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlarmReceiverModule {

    @Provides
    fun providesAlarmReceiver(): AlarmReceiver {
        return AlarmReceiver()
    }
}