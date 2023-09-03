package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryThoughtUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetTraumaDiaryThoughtUseCaseTest {
    private lateinit var getTraumaDiaryThoughtUseCase: GetTraumaDiaryThoughtUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testThought = "테스트 생각"

    @BeforeTest
    fun setUp() {
        getTraumaDiaryThoughtUseCase = GetTraumaDiaryThoughtUseCase(mockTraumaDiaryRepository)
    }

    @AfterTest
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_retrievingDiaryThought() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.getThought() } returns flowOf(testThought)

        // When
        val resultFlow = getTraumaDiaryThoughtUseCase()

        // Then
        resultFlow.collect { thought ->
            assertEquals(testThought, thought)
        }
        coVerify { mockTraumaDiaryRepository.getThought() }
    }
}
