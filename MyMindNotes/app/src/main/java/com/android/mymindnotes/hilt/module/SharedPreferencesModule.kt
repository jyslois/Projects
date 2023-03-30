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

    @FirstTime
    @Provides
    fun provideSharedPreferencesforFirstTime(application: Application): SharedPreferences
    = application.getSharedPreferences("firstTime", Activity.MODE_PRIVATE)

    @Alarm
    @Provides
    fun provideSharedPreferencesforAlarm(application: Application): SharedPreferences
    = application.getSharedPreferences("alarm", Activity.MODE_PRIVATE)

    @Time
    @Provides
    fun provideSharedPreferencesforTime(application: Application): SharedPreferences
    = application.getSharedPreferences("time", Activity.MODE_PRIVATE)
}


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AutoSave

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class User

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class FirstTime

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Alarm

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Time