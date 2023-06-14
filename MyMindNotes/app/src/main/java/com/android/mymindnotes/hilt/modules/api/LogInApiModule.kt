package com.android.mymindnotes.hilt.modules.api

import com.android.mymindnotes.data.retrofit.api.user.LoginApi
import com.android.mymindnotes.core.network.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LogInApiModule {
    @Provides
    @Singleton
    fun provideLogInApi(
        retrofitService: RetrofitService
    ): LoginApi {
        return retrofitService.retrofit.create(LoginApi::class.java)
    }
}