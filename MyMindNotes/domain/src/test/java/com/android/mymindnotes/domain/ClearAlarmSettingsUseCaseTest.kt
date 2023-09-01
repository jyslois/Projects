package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
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
class ClearAlarmSettingsUseCaseTest {
    private lateinit var clearAlarmSettingsUseCase: ClearAlarmSettingsUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        clearAlarmSettingsUseCase = ClearAlarmSettingsUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingClearAlarmRelatedKeys() = runTest {
        // Given
        coEvery { mockMemberRepository.clearAlarmRelatedKeys() } just Runs

        // When
        clearAlarmSettingsUseCase()

        // Then
        coVerify { mockMemberRepository.clearAlarmRelatedKeys() }
    }
}