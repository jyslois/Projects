package com.android.mymindnotes.hilt.module

import com.android.mymindnotes.hilt.module.coroutine.DefaultDispatcher
import com.android.mymindnotes.hilt.module.coroutine.IoDispatcher
import com.android.mymindnotes.hilt.module.coroutine.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopesModule {
    @Provides
    @Singleton // provides always the same instance
    @IoDispatcherCoroutineScope
    fun providesIOCoroutineScope(
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(ioDispatcher)

    @Provides
    @Singleton // provides always the same instance
    @DefaultDispatcherCoroutineScope
    fun providesDefaultCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(defaultDispatcher)

    @Provides
    @Singleton // provides always the same instance
    @MainDispatcherCoroutineScope
    fun providesMainCoroutineScope(
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(mainDispatcher)
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcherCoroutineScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcherCoroutineScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainDispatcherCoroutineScope
