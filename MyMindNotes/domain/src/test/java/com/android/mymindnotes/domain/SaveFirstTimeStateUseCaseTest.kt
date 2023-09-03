package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
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
class SaveFirstTimeStateUseCaseTest {
    private lateinit var saveFirstTimeStateUseCase: SaveFirstTimeStateUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val testState = true

    @Before
    fun setUp() {
        saveFirstTimeStateUseCase = SaveFirstTimeStateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingSaveFirstTime() = runTest {
        // Given
        coEvery { mockMemberRepository.saveFirstTime(any()) } just Runs

        // When
        saveFirstTimeStateUseCase(state = testState)

        // Then
        coVerify { mockMemberRepository.saveFirstTime(testState) }
    }
}