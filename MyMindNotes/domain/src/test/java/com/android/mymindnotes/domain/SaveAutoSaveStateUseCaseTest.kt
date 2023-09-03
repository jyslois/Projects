package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
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
class SaveAutoSaveStateUseCaseTest {
    private lateinit var saveAutoSaveStateUseCase: SaveAutoSaveStateUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    private val testState = true

    @Before
    fun setUp() {
        saveAutoSaveStateUseCase = SaveAutoSaveStateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingSaveAutoSaveCheck() = runTest {
        // Given
        coEvery { mockMemberRepository.saveAutoSaveCheck(any()) } just Runs

        // When
        saveAutoSaveStateUseCase(state = testState)

        // Then
        coVerify { mockMemberRepository.saveAutoSaveCheck(testState) }
    }
}
