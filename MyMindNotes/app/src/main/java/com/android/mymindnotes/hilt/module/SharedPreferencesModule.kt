package com.android.mymindnotes.hilt.module

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier


@InstallIn(SingletonComponent::class)
@Module
object SharedPreferencesModule {
    @AutoSave
    @Provides
    fun provideSharedPreferencesforAutoSave(application: Application): SharedPreferences
    = application.getSharedPreferences("autoSave", Activity.MODE_PRIVATE)

    @User
    @Provides
    fun provideSharedPreferencesforUser(application: Application): SharedPreferences
    = application.getSharedPreferences("user", Activity.MODE_PRIVATE)
}


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AutoSave
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class User