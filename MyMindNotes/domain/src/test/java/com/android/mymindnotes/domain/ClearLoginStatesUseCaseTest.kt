package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.loginStates.ClearLoginStatesUseCase
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
class ClearLoginStatesUseCaseTest {
    private lateinit var clearLoginStatesUseCase: ClearLoginStatesUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    @Before
    fun setUp() {
        clearLoginStatesUseCase = ClearLoginStatesUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingClearLoginStatesRelatedKeys() = runTest {
        // Given
        coEvery { mockMemberRepository.clearLoginStatesRelatedKeys() } just Runs

        // When
        clearLoginStatesUseCase()

        // Then
        coVerify { mockMemberRepository.clearLoginStatesRelatedKeys() }
    }
}
