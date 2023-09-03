package com.android.mymindnotes.domain
import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class ClearTraumaDiaryTempRecordsUseCaseTest {
    private lateinit var clearTraumaDiaryTempRecordsUseCase: ClearTraumaDiaryTempRecordsUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    @BeforeTest
    fun setUp() {
        clearTraumaDiaryTempRecordsUseCase = ClearTraumaDiaryTempRecordsUseCase(mockTraumaDiaryRepository)
    }

    @AfterTest
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_clearingDiaryTempRecords() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.clearTraumaDiaryTempRecords() } just Runs

        // When
        clearTraumaDiaryTempRecordsUseCase()

        // Then
        coVerify { mockTraumaDiaryRepository.clearTraumaDiaryTempRecords() }
    }
}