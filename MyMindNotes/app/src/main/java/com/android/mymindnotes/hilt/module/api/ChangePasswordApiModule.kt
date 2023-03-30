package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.data.retrofit.api.user.ChangeNicknameApi
import com.android.mymindnotes.data.retrofit.api.user.ChangePasswordApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChangePasswordApiModule {
    @Provides
    @Singleton
    fun provideChangePasswordApi (
        retrofitService: RetrofitService
    ): ChangePasswordApi {
        return retrofitService.retrofit.create(ChangePasswordApi::class.java)
    }
}