package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmTimeUseCase
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
class SaveAlarmTimeUseCaseTest {
    private lateinit var saveAlarmTimeUseCase: SaveAlarmTimeUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        saveAlarmTimeUseCase = SaveAlarmTimeUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test data
    private val testTime = "22:00"

    @Test
    fun invoke_savingAlarmTime() = runTest {
        // Given
        coEvery { mockMemberRepository.saveTime(any()) } just Runs

        // When
        saveAlarmTimeUseCase(testTime)

        // Then
        coVerify { mockMemberRepository.saveTime(testTime) }
    }
}