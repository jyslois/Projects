package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ClearTimeSettingsUseCaseTest {
    private lateinit var clearTimeSettingsUseCase: ClearTimeSettingsUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        clearTimeSettingsUseCase = ClearTimeSettingsUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingClearRebootTimeKey() = runTest {
        // Given
        coEvery { mockMemberRepository.clearRebootTimeKey() } just Runs

        // When
        clearTimeSettingsUseCase()

        // Then
        coVerify { mockMemberRepository.clearRebootTimeKey() }
    }
}