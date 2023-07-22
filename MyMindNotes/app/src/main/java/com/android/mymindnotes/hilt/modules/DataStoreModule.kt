package com.android.mymindnotes.hilt.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    // 하나의 DataStore 인스턴스를 위한 확장 프로퍼티를 선언
    private val Context.dataStore by preferencesDataStore(
        name = "dataStore",
        produceMigrations = { context ->
            // SharedPreferences names from which to migrate
            val names = listOf(
                "autoSave", "user", "firstTime", "alarm", "time", "emotion",
                "emotionDescription", "situation", "thought", "reflection", "type",
                "emotionColor", "date", "day", "trauma_emotion", "trauma_emotionText",
                "trauma_situation", "trauma_thought", "trauma_reflection", "trauma_type",
                "trauma_emotionColor", "trauma_date", "trauma_day"
            )
            names.map { SharedPreferencesMigration(context, it) }
        }
    )

    // 하나의 DataStore 인스턴스를 제공
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

}