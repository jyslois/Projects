package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.data.retrofit.api.user.DeleteUserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeleteUserApiModule {
    @Provides
    @Singleton
    fun provideDeleteUserApi (
        retrofitService: RetrofitService
    ): DeleteUserApi {
        return retrofitService.retrofit.create(DeleteUserApi::class.java)
    }
}