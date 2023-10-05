package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmMinuteUseCase
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
class GetAlarmMinuteUseCaseTest {
    private lateinit var getAlarmMinuteUseCase: GetAlarmMinuteUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        getAlarmMinuteUseCase = GetAlarmMinuteUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test data
    private val testAlarmMinute = 10

    @Test
    fun invoke_retrievingAlarmMinute() = runTest {
        // Given
        coEvery { mockMemberRepository.getMinute() } returns flowOf(testAlarmMinute)

        // When
        val resultFlow = getAlarmMinuteUseCase()

        // Then
        resultFlow.collect { alarmMinute ->
            assertEquals(testAlarmMinute, alarmMinute)
        }
        coVerify { mockMemberRepository.getMinute() }
    }
}