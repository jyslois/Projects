package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.DeleteDiaryResponse
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.domain.usecases.diaryRemote.DeleteDiaryUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class DeleteDiaryUseCaseTest {
    private lateinit var deleteDiaryUseCase: DeleteDiaryUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockDiaryRepository = mockk<DiaryRepository>()

    // 테스트 데이터
    private val diaryNumber = 1

    @Before
    fun setUp() {
        deleteDiaryUseCase = DeleteDiaryUseCase(mockDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockDiaryRepository)
    }

    @Test
    fun invoke_ReturnsNormalResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = DeleteDiaryResponse(code = 9000, msg = "일기가 삭제되었습니다.")
        coEvery { mockDiaryRepository.deleteDiary(diaryNumber) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = deleteDiaryUseCase(diaryNumber)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메세지($msg)가 예상 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockDiaryRepository.deleteDiary(diaryNumber) }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "일기 삭제에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요."
        coEvery { mockDiaryRepository.deleteDiary(diaryNumber) } throws RuntimeException(errorMsg)

        // When
        val actualResponseFlow = deleteDiaryUseCase(diaryNumber)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 에러 메세지(${exception.message})가 예상 에러 메시지(${errorMsg})와 다릅니다.")
        }
        coVerify { mockDiaryRepository.deleteDiary(diaryNumber) }
    }
}