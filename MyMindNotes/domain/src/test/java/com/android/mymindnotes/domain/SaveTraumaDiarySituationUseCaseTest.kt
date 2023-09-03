package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiarySituationUseCase
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
class SaveTraumaDiarySituationUseCaseTest {
    private lateinit var saveTraumaDiarySituationUseCase: SaveTraumaDiarySituationUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testSituation = "테스트 상황"

    @Before
    fun setUp() {
        saveTraumaDiarySituationUseCase = SaveTraumaDiarySituationUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiarySituation() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveSituation(any()) } just Runs

        // When
        saveTraumaDiarySituationUseCase.invoke(situation = testSituation)

        // Then
        coVerify { mockTraumaDiaryRepository.saveSituation(testSituation) }
    }
}
