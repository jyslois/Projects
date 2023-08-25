package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
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
class SaveUserIndexUseCaseTest {
    private lateinit var saveUserIndexUseCase: SaveUserIndexUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    val userIndex = 1

    @Before
    fun setUp() {
        saveUserIndexUseCase = SaveUserIndexUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest {
        // Given
        coEvery { mockMemberRepository.saveUserIndex(any()) } just Runs

        // When
        saveUserIndexUseCase(userIndex)

        // Then
        coVerify { mockMemberRepository.saveUserIndex(userIndex) }
    }
}