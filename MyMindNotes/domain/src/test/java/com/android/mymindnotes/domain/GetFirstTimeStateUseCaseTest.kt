package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.GetFirstTimeStateUseCase
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
class GetFirstTimeStateUseCaseTest {
    private lateinit var getFirstTimeStateUseCase: GetFirstTimeStateUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val testFirstTimeState = true

    @Before
    fun setUp() {
        getFirstTimeStateUseCase = GetFirstTimeStateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingGetFirstTime() = runTest {
        // Given
        coEvery { mockMemberRepository.getFirstTime() } returns flowOf(testFirstTimeState)

        // When
        val resultFlow = getFirstTimeStateUseCase()

        // Then
        resultFlow.collect { isFirstTime ->
            assertEquals(testFirstTimeState, isFirstTime)
        }
        coVerify { mockMemberRepository.getFirstTime() }
    }
}
