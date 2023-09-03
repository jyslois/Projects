package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
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
class SaveIdAndPasswordUseCaseTest {
    private lateinit var saveIdAndPasswordUseCase: SaveIdAndPasswordUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val testId = "seolois@hotmail.com"
    private val testPassword = "testPassword123"

    @Before
    fun setUp() {
        saveIdAndPasswordUseCase = SaveIdAndPasswordUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingSaveIdAndPassword() = runTest {
        // Given
        coEvery { mockMemberRepository.saveIdAndPassword(any(), any()) } just Runs

        // When
        saveIdAndPasswordUseCase(id = testId, password = testPassword)

        // Then
        coVerify { mockMemberRepository.saveIdAndPassword(testId, testPassword) }
    }
}