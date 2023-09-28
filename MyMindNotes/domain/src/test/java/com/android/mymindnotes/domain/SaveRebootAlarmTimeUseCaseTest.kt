package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.SaveRebootAlarmTimeUseCase
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
class SaveRebootAlarmTimeUseCaseTest {
    private lateinit var saveRebootAlarmTimeUseCase: SaveRebootAlarmTimeUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        saveRebootAlarmTimeUseCase = SaveRebootAlarmTimeUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test Data
    private val testRebootTime = 79200L

    @Test
    fun invoke_savingRebootAlarmTime() = runTest {
        // Given
        coEvery { mockMemberRepository.saveRebootTime(any()) } just Runs

        // When
        saveRebootAlarmTimeUseCase(time = testRebootTime)

        // Then
        coVerify { mockMemberRepository.saveRebootTime(testRebootTime) }
    }
}