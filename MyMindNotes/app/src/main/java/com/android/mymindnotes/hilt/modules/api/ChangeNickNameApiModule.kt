package com.android.mymindnotes.hilt.modules.api

import com.android.mymindnotes.core.network.RetrofitService
import com.android.mymindnotes.data.retrofit.api.user.ChangeNicknameApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChangeNickNameApiModule {
    @Provides
    @Singleton
    fun provideChangeNickNameApi (
        retrofitService: RetrofitService
    ): ChangeNicknameApi {
        return retrofitService.retrofit.create(ChangeNicknameApi::class.java)
    }
}