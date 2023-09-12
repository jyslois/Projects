package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiarySituationUseCase
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
class SaveTodayDiarySituationUseCaseTest {
    private lateinit var saveTodayDiarySituationUseCase: SaveTodayDiarySituationUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    @Before
    fun setUp() {
        saveTodayDiarySituationUseCase = SaveTodayDiarySituationUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    // 테스트 데이터
    private val testSituation = "테스트 상황"

    @Test
    fun invoke_SavingDiarySituation() = runTest {
       // Given
        coEvery { mockTodayDiaryRepository.saveSituation(any()) } just Runs

        // When
        saveTodayDiarySituationUseCase(situation = testSituation)

        // Then
        coVerify { mockTodayDiaryRepository.saveSituation(testSituation) }
    }

}