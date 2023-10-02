package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmMinuteUseCase
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveAlarmMinuteUseCaseTest {
    private lateinit var saveAlarmMinuteUseCase: SaveAlarmMinuteUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        saveAlarmMinuteUseCase = SaveAlarmMinuteUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test Data
    private val testAlarmMinute = 10

    @Test
    fun invoke_savingAlarmMinute() = runTest {
        // Given
        coEvery { mockMemberRepository.saveMinute(any()) } just Runs

        // When
        saveAlarmMinuteUseCase(minute = testAlarmMinute)

        // Then
        coVerify { mockMemberRepository.saveMinute(testAlarmMinute) }
    }
}