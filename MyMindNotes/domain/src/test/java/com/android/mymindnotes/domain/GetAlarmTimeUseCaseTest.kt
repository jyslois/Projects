package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmTimeUseCase
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
class GetAlarmTimeUseCaseTest {
    private lateinit var getAlarmTimeUseCase: GetAlarmTimeUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // Test Data
    private val testAlarmTime = "22:00"

    @Before
    fun setUp() {
        getAlarmTimeUseCase = GetAlarmTimeUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_retrievingAlarmTime() = runTest {
        // Given
        coEvery { mockMemberRepository.getTime() } returns flowOf(testAlarmTime)

        // When
        val resultFlow = getAlarmTimeUseCase()

        // Then
        resultFlow.collect { alarmTime ->
            assertEquals(testAlarmTime, alarmTime)
        }
        coVerify { mockMemberRepository.getTime() }
    }
}