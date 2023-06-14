package com.android.mymindnotes.hilt.modules.alarm

import android.content.Context
import com.android.mymindnotes.domain.alarmInterface.AlarmManagerHelperInterface
import com.android.mymindnotes.presentation.alarm.AlarmBootReceiver
import com.android.mymindnotes.presentation.alarm.AlarmManagerHelper
import com.android.mymindnotes.presentation.alarm.AlarmReceiver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmManagerHelperInterfaceModule {
    @Singleton
    @Binds
    abstract fun bindAlarmReceiver(
        alarmManagerHelper: AlarmManagerHelper
    ): AlarmManagerHelperInterface
}

@Module
@InstallIn(SingletonComponent::class)
object AlarmManagerHelperModule {

    @Provides
    fun providesAlarmManagerHelper(@ApplicationContext context: Context): AlarmManagerHelper {
        return AlarmManagerHelper(context)
    }

}