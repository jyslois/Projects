package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveAutoLoginStateUseCaseTest {
    private lateinit var saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    private val testState = true

    @Before
    fun setUp() {
        saveAutoLoginStateUseCase = SaveAutoLoginStateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingSaveAutoLoginCheck() = runTest {
        // Given
        coEvery { mockMemberRepository.saveAutoLoginCheck(any()) } just Runs

        // When
        saveAutoLoginStateUseCase(state = testState)

        // Then
        coVerify { mockMemberRepository.saveAutoLoginCheck(testState) }
    }
}
