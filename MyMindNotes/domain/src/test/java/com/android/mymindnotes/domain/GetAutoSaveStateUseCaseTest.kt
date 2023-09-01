package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoSaveStateUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
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
class GetAutoSaveStateUseCaseTest {
    private lateinit var getAutoSaveStateUseCase: GetAutoSaveStateUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val testState = true

    @Before
    fun setUp() {
        getAutoSaveStateUseCase = GetAutoSaveStateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingGetAutoSaveCheck() = runTest {
        // Given
        coEvery { mockMemberRepository.getAutoSaveCheck() } returns flowOf(testState)

        // When
        val resultFlow = getAutoSaveStateUseCase()

        // Then
        resultFlow.collect { state ->
            assertEquals(testState, state)
        }
        coVerify { mockMemberRepository.getAutoSaveCheck() }
    }
}
