package com.android.mymindnotes.hilt.module

import com.android.mymindnotes.domain.AlarmBootReceiver
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