package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.GetPasswordUseCase
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
class GetPasswordUseCaseTest {
    private lateinit var getPasswordUseCase: GetPasswordUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val testPassword = "testPassword123"

    @Before
    fun setUp() {
        getPasswordUseCase = GetPasswordUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingGetPassword() = runTest {
        // Given
        coEvery { mockMemberRepository.getPassword() } returns flowOf(testPassword)

        // When
        val resultFlow = getPasswordUseCase()

        // Then
        resultFlow.collect { password ->
            assertEquals(testPassword, password)
        }
        coVerify { mockMemberRepository.getPassword() }
    }
}