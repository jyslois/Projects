package com.android.mymindnotes.domain
import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryReflectionUseCase
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
class GetTraumaDiaryReflectionUseCaseTest {
    private lateinit var getTraumaDiaryReflectionUseCase: GetTraumaDiaryReflectionUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testReflection = "테스트 회고"

    @BeforeTest
    fun setUp() {
        getTraumaDiaryReflectionUseCase = GetTraumaDiaryReflectionUseCase(mockTraumaDiaryRepository)
    }

    @AfterTest
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_retrievingDiaryReflection() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.getReflection() } returns flowOf(testReflection)

        // When
        val resultFlow = getTraumaDiaryReflectionUseCase()

        // Then
        resultFlow.collect { reflection ->
            assertEquals(testReflection, reflection)
        }
        coVerify { mockTraumaDiaryRepository.getReflection() }
    }
}
