package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.SavePasswordUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SavePasswordUseCaseTest {
    private lateinit var savePasswordUseCase: SavePasswordUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val password = "testPassword123"

    @Before
    fun setUp() {
        savePasswordUseCase = SavePasswordUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingSavePassword() = runTest {
        // Given
        coEvery { mockMemberRepository.savePassword(any()) } just runs

        // When
        savePasswordUseCase(password = password)

        // Then
        coVerify { mockMemberRepository.savePassword(password) }
    }
}