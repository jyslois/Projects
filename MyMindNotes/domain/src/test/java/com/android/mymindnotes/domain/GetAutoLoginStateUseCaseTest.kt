package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoLoginStateUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAutoLoginStateUseCaseTest {
    private lateinit var getAutoLoginStateUseCase: GetAutoLoginStateUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val testState = true

    @Before
    fun setUp() {
        getAutoLoginStateUseCase = GetAutoLoginStateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingGetAutoLoginCheck() = runTest {
        // Given
        coEvery { mockMemberRepository.getAutoLoginCheck() } returns flowOf(testState)

        // When
        val resultFlow = getAutoLoginStateUseCase()

        // Then
        resultFlow.collect { state ->
            assertEquals(testState, state)
        }
        coVerify { mockMemberRepository.getAutoLoginCheck() }
    }
}
