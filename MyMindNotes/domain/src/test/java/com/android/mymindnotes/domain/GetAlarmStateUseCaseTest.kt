package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmStateUseCase
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
class GetAlarmStateUseCaseTest {
    private lateinit var getAlarmStateUseCase: GetAlarmStateUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        getAlarmStateUseCase = GetAlarmStateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test Data
    private val testAlarmState = true

    @Test
    fun invoke_retrievingAlarmState() = runTest {
        // Given
        coEvery { mockMemberRepository.getAlarmState() } returns flowOf(testAlarmState)

        // When
        val resultFlow = getAlarmStateUseCase()

        // Then
        resultFlow.collect { alarmState ->
            assertEquals(testAlarmState, alarmState)
        }
        coVerify { mockMemberRepository.getAlarmState() }
    }
}