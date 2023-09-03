package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiarySituationUseCase
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
class GetTraumaDiarySituationUseCaseTest {
    private lateinit var getTraumaDiarySituationUseCase: GetTraumaDiarySituationUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testSituation = "테스트 상황"

    @BeforeTest
    fun setUp() {
        getTraumaDiarySituationUseCase = GetTraumaDiarySituationUseCase(mockTraumaDiaryRepository)
    }

    @AfterTest
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_retrievingDiarySituation() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.getSituation() } returns flowOf(testSituation)

        // When
        val resultFlow = getTraumaDiarySituationUseCase()

        // Then
        resultFlow.collect { situation ->
            assertEquals(testSituation, situation)
        }
        coVerify { mockTraumaDiaryRepository.getSituation() }
    }
}