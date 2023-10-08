package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmHourUseCase
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
class SaveAlarmHourUseCaseTest {
    private lateinit var saveAlarmHourUseCase: SaveAlarmHourUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        saveAlarmHourUseCase = SaveAlarmHourUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test Data
    private val testAlarmHour = 22

    @Test
    fun invoke_savingAlarmHour() = runTest {
        // Given
        coEvery { mockMemberRepository.saveHour(any()) } just Runs

        // When
        saveAlarmHourUseCase(hour = testAlarmHour)

        // Then
        coVerify { mockMemberRepository.saveHour(testAlarmHour) }
    }
}