package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryReflectionUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetTodayDiaryReflectionUseCaseTest {
    private lateinit var getTodayDiaryReflectionUseCase: GetTodayDiaryReflectionUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // 테스트 데이터
    private val testReflection = "테스트 회고"

    @Before
    fun setUp() {
        getTodayDiaryReflectionUseCase = GetTodayDiaryReflectionUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_retrievingTodayDiaryReflection() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.getReflection() } returns flowOf(testReflection)

        // When
        val resultFlow = getTodayDiaryReflectionUseCase()

        // Then
        resultFlow.collect { reflection ->
            assertEquals(testReflection, reflection)
        }
        coVerify { mockTodayDiaryRepository.getReflection() }
    }
}