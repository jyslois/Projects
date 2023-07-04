package com.android.mymindnotes.hilt.modules.dataSource

import com.android.mymindnotes.data.dataSources.MemberLocalDataSource
import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MemberLocalDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindMemberLocalDataSource(
        memberLocalDataSource: MemberLocalDataSource
    ): MemberLocalDataSourceInterface
}