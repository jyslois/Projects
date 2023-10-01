package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmStateUseCase
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
class SaveAlarmStateUseCaseTest {
    private lateinit var saveAlarmStateUseCase: SaveAlarmStateUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        saveAlarmStateUseCase = SaveAlarmStateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test data
    private val testAlarmState = true

    @Test
    fun invoke_savingAlarmState() = runTest {
        // Given
        coEvery { mockMemberRepository.saveAlarmState(any()) } just Runs

        // When
        saveAlarmStateUseCase(testAlarmState)

        // Then
        coVerify { mockMemberRepository.saveAlarmState(testAlarmState) }
    }
}