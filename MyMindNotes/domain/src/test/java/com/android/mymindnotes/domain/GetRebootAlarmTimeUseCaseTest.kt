package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.GetRebootAlarmTimeUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetRebootAlarmTimeUseCaseTest {
    private lateinit var getRebootAlarmTimeUseCase: GetRebootAlarmTimeUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        getRebootAlarmTimeUseCase = GetRebootAlarmTimeUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // Test Data
    private val testRebootAlarmTime = 79200L

    @Test
    fun invoke_retrievingRebootAlarmTime() = runTest {
        // Given
        coEvery { mockMemberRepository.getRebootTime() } returns flowOf(testRebootAlarmTime)

        // When
        val resultFlow = getRebootAlarmTimeUseCase()

        // Then
        resultFlow.collect { rebootAlarmTime ->
            assertEquals(testRebootAlarmTime, rebootAlarmTime)
        }
        coVerify { mockMemberRepository.getRebootTime() }
    }
}