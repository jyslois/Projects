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

    // Member
    @AutoSave
    @Provides
    fun provideSharedPreferencesForAutoSave(application: Application): SharedPreferences
    = application.getSharedPreferences("autoSave", Activity.MODE_PRIVATE)

    @User
    @Provides
    fun provideSharedPreferencesForUser(application: Application): SharedPreferences
    = application.getSharedPreferences("user", Activity.MODE_PRIVATE)

    @FirstTime
    @Provides
    fun provideSharedPreferencesForFirstTime(application: Application): SharedPreferences
    = application.getSharedPreferences("firstTime", Activity.MODE_PRIVATE)

    @Alarm
    @Provides
    fun provideSharedPreferencesForAlarm(application: Application): SharedPreferences
    = application.getSharedPreferences("alarm", Activity.MODE_PRIVATE)

    @Time
    @Provides
    fun provideSharedPreferencesForTime(application: Application): SharedPreferences
    = application.getSharedPreferences("time", Activity.MODE_PRIVATE)

    // Today Diary
    @Emotion
    @Provides
    fun provideSharedPreferencesForEmotion(application: Application): SharedPreferences
    = application.getSharedPreferences("emotion", Activity.MODE_PRIVATE)

    @EmotionText
    @Provides
    fun provideSharedPreferencesForEmotionText(application: Application): SharedPreferences
    = application.getSharedPreferences("emotionText", Activity.MODE_PRIVATE)

    @Situation
    @Provides
    fun provideSharedPreferencesForSituation(application: Application): SharedPreferences
    = application.getSharedPreferences("situation", Activity.MODE_PRIVATE)

    @Thought
    @Provides
    fun provideSharedPreferencesForThought(application: Application): SharedPreferences
    = application.getSharedPreferences("thought", Activity.MODE_PRIVATE)

    @Reflection
    @Provides
    fun provideSharedPreferencesForReflection(application: Application): SharedPreferences
     = application.getSharedPreferences("reflection", Activity.MODE_PRIVATE)

    @Type
    @Provides
    fun provideSharedPreferencesForType(application: Application): SharedPreferences
    = application.getSharedPreferences("type", Activity.MODE_PRIVATE)

    @EmotionColor
    @Provides
    fun provideSharedPreferencesForEmotionColor(application: Application): SharedPreferences
    = application.getSharedPreferences("emotionColor", Activity.MODE_PRIVATE)

    // Trauma Diary
    @Trauma_Emotion
    @Provides
    fun provideSharedPreferencesForTraumaEmotion(application: Application): SharedPreferences
            = application.getSharedPreferences("trauma_emotion", Activity.MODE_PRIVATE)

    @Trauma_EmotionText
    @Provides
    fun provideSharedPreferencesForTraumaEmotionText(application: Application): SharedPreferences
            = application.getSharedPreferences("trauma_emotionText", Activity.MODE_PRIVATE)

    @Trauma_Situation
    @Provides
    fun provideSharedPreferencesForTraumaSituation(application: Application): SharedPreferences
            = application.getSharedPreferences("trauma_situation", Activity.MODE_PRIVATE)

    @Trauma_Thought
    @Provides
    fun provideSharedPreferencesForTraumaThought(application: Application): SharedPreferences
            = application.getSharedPreferences("trauma_thought", Activity.MODE_PRIVATE)

    @Trauma_Reflection
    @Provides
    fun provideSharedPreferencesForTraumaReflection(application: Application): SharedPreferences
            = application.getSharedPreferences("trauma_reflection", Activity.MODE_PRIVATE)

    @Trauma_Type
    @Provides
    fun provideSharedPreferencesForTraumaType(application: Application): SharedPreferences
            = application.getSharedPreferences("trauma_type", Activity.MODE_PRIVATE)

    @Trauma_EmotionColor
    @Provides
    fun provideSharedPreferencesForTraumaEmotionColor(application: Application): SharedPreferences
            = application.getSharedPreferences("trauma_emotionColor", Activity.MODE_PRIVATE)

}


// User

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

// Today Diary

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Emotion

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class EmotionText

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Situation

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Thought

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Reflection

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Type

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class EmotionColor

// Trauma Diary

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Trauma_Emotion

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Trauma_EmotionText

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Trauma_Situation

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Trauma_Thought

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Trauma_Reflection

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Trauma_Type

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Trauma_EmotionColor