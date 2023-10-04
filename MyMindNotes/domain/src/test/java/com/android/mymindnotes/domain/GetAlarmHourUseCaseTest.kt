package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmHourUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetAlarmHourUseCaseTest {
    private lateinit var getAlarmHourUseCase: GetAlarmHourUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        getAlarmHourUseCase = GetAlarmHourUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test data
    private val testAlarmHour = 22

    @Test
    fun invoke_retrievingAlarmHour() = runTest {
        // Given
        coEvery { mockMemberRepository.getHour() } returns flowOf(testAlarmHour)

        // When
        val resultFlow = getAlarmHourUseCase()

        // Then
        resultFlow.collect { alarmHour ->
            assertEquals(testAlarmHour, alarmHour)
        }
        coVerify { mockMemberRepository.getHour() }
    }
}