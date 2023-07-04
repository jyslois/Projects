package com.android.mymindnotes.hilt.modules.dataSource

import com.android.mymindnotes.data.dataSources.MemberRemoteDataSource
import com.android.mymindnotes.data.dataSources.MemberRemoteDataSourceInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MemberRemoteDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindMemberRemoteDataSource(
        memberRemoteDataSource: MemberRemoteDataSource
    ): MemberRemoteDataSourceInterface
}