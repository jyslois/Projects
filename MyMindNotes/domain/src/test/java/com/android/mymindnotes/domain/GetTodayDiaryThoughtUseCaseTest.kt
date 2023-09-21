package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryThoughtUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
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
class GetTodayDiaryThoughtUseCaseTest {
    private lateinit var getTodayDiaryThoughtUseCase: GetTodayDiaryThoughtUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // Test data
    private val testThought = "테스트 생각"

    @Before
    fun setUp() {
        getTodayDiaryThoughtUseCase = GetTodayDiaryThoughtUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_retrievingDiaryThought() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.getThought() } returns flowOf(testThought)

        // When
        val resultFlow = getTodayDiaryThoughtUseCase()

        // Then
        resultFlow.collect { thought ->
            assertEquals(testThought, thought)
        }
        coVerify { mockTodayDiaryRepository.getThought() }
    }

}