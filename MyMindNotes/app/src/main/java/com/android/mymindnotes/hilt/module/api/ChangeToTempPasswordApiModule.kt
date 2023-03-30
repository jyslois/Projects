package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.data.retrofit.api.user.ChangeToTempPasswordApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChangeToTempPasswordApiModule {
    @Provides
    @Singleton
    fun provideChangeToTempPasswordApi (
        retrofitService: RetrofitService
    ): ChangeToTempPasswordApi {
        return retrofitService.retrofit.create(ChangeToTempPasswordApi::class.java)
    }
}