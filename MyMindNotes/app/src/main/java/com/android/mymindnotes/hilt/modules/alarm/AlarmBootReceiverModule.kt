package com.android.mymindnotes.hilt.modules.alarm

import com.android.mymindnotes.presentation.alarm.AlarmBootReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlarmBootReceiverModule {

    @Provides
    fun providesAlarmBootReceiver(): AlarmBootReceiver {
        return AlarmBootReceiver()
    }

}