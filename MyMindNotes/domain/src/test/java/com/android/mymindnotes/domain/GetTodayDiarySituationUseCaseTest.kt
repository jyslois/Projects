package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiarySituationUseCase
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
class GetTodayDiarySituationUseCaseTest {
    private lateinit var getTodayDiarySituationUseCase: GetTodayDiarySituationUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // 테스트 데이터
    private val testSituation = "테스트 상황"

    @Before
    fun setUp() {
        getTodayDiarySituationUseCase = GetTodayDiarySituationUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_retrievingTodayDiarySituation() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.getSituation() } returns flowOf(testSituation)

        // When
        val resultFlow = getTodayDiarySituationUseCase()

        // Then
        resultFlow.collect { situation ->
            assertEquals(testSituation, situation)
        }
        coVerify { mockTodayDiaryRepository.getSituation() }
    }
}