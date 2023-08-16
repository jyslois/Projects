package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.core.dto.DeleteDiaryResponse
import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.core.dto.DiaryEdit
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.core.dto.UpdateDiaryResponse
import com.android.mymindnotes.data.retrofit.api.diary.DeleteDiaryApi
import com.android.mymindnotes.data.retrofit.api.diary.UpdateDiaryApi
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher

import org.junit.After
import org.junit.Before
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class DiaryRemoteDataSourceTest {

    private lateinit var diaryRemoteDataSource: DiaryRemoteDataSource
    private val mockGetDiaryListApi = mockk<GetDiaryListApi>()
    private val mockDeleteDiaryApi = mockk<DeleteDiaryApi>()
    private val mockUpdateDiaryApi = mockk<UpdateDiaryApi>()
    private val ioDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        diaryRemoteDataSource = DiaryRemoteDataSource(
            getDiaryListApi = mockGetDiaryListApi,
            deleteDiaryApi =  mockDeleteDiaryApi,
            updateDiaryApi = mockUpdateDiaryApi,
            ioDispatcher = ioDispatcher

        )
    }

    @After
    fun tearDown() {
        clearMocks(mockGetDiaryListApi, mockDeleteDiaryApi, mockUpdateDiaryApi)
    }

    @Test
    fun `test getDiaryList_NormalResponse`() = runTest(ioDispatcher) {
        // Given
        val expectedDiaryList = arrayListOf(
            Diary("2023-01-01", "월요일", 1, "행복", "정말 좋은 하루", "좋은 일이 많았다.", "집에서 휴식", "좋은 생각", "오늘의 마음 일기", 1),
            Diary("2023-01-02", "화요일", 2, "공포", "무서웠다", "힘들었다.", "동물원에서 키가 큰 기린을 봤다", "목이 정말 기네", "트라우마 일기", 1)
        )
        val expectedResponse = GetDiaryListResponse(code = 7000, msg = "일기 목록을 성공적으로 불러왔습니다.", diaryList = expectedDiaryList)
        coEvery { mockGetDiaryListApi.getAllDiary(1) } returns expectedResponse

        // When
        val returnedResponse = diaryRemoteDataSource.getDiaryList(1)

        // Then
        returnedResponse.collect {
            assertEquals(7000, it.code)
            assertEquals("반환된 일기 목록이 예상과 다릅니다.", expectedDiaryList, it.diaryList)
        }
        coVerify { mockGetDiaryListApi.getAllDiary(1) }
    }

    @Test
    fun `test getDiaryList_EmptyResponse`() = runTest(ioDispatcher) {
        // Given
        val expectedResponse = GetDiaryListResponse(code = 7000, msg = "일기 목록을 성공적으로 불러왔습니다.", diaryList = arrayListOf())
        coEvery { mockGetDiaryListApi.getAllDiary(1) } returns expectedResponse

        // When
        val returnedResponse = diaryRemoteDataSource.getDiaryList(1)

        // Then
        returnedResponse.collect {
            assertTrue("일기 목록이 비어 있지 않습니다", it.diaryList.isEmpty())
        }
        coVerify { mockGetDiaryListApi.getAllDiary(1) }
    }

    @Test
    fun `test getDiaryList_ErrorResponse`(): Unit = runTest(ioDispatcher) {
        // Given
        coEvery { mockGetDiaryListApi.getAllDiary(1) } throws RuntimeException("오류 발생")

        // When
        val returnedResponse = diaryRemoteDataSource.getDiaryList(1)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockGetDiaryListApi.getAllDiary(1) }
    }



    @Test
    fun `test deleteDiary_NormalResponse`() = runTest(ioDispatcher) {
        // Given
        val expectedResponse = DeleteDiaryResponse(code = 9000, msg = "일기를 삭제했습니다.")
        coEvery { mockDeleteDiaryApi.deleteDiary(1) } returns expectedResponse

        // When
        val returnedResponse = diaryRemoteDataSource.deleteDiary(1)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상 코드(${expectedResponse.code})와 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockDeleteDiaryApi.deleteDiary(1) }
    }

    @Test
    fun `test deleteDiary_ErrorResponse`() = runTest(ioDispatcher) {
        // Given
        coEvery { mockDeleteDiaryApi.deleteDiary(1) } throws RuntimeException("오류 발생")

        // When
        val returnedResponse = diaryRemoteDataSource.deleteDiary(1)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockDeleteDiaryApi.deleteDiary(1) }
    }


    @Test
    fun `test updateDiary_NormalResponse`() = runTest(ioDispatcher) {
        // Given
        val expectedResponse = UpdateDiaryResponse(code = 8000, msg = "일기를 수정하였습니다.")
        val diaryEdit = DiaryEdit(situation = "상황 수정", thought = "생각 수정", emotion = "기쁨", emotionDescription = "감정 설명 수정", reflection = "회고 수정")
        coEvery { mockUpdateDiaryApi.updateDiary(diary_number = 1, diaryEdit = diaryEdit) } returns expectedResponse

        // When
        val returnedResponse = diaryRemoteDataSource.updateDiary(diaryNumber = 1, situation = "상황 수정", thought = "생각 수정", emotion = "기쁨", emotionDescription = "감정 설명 수정", reflection = "회고 수정")

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상 코드(${expectedResponse.code})와 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockUpdateDiaryApi.updateDiary(1, diaryEdit) }
    }

    @Test
    fun updateDiary_InvalidResponse() = runTest(ioDispatcher) {


        // 반한된 코드가 8001인지 확인
        val expectedCode = 8001
        val returnedCode = diaryRemoteDataSource.updateDiary(1, "", "", "기쁨", "", "맛있어서 행복한 하루").first().code

        assertEquals("반환된 코드($returnedCode)가 예상 코드($expectedCode)와 다릅니다.", expectedCode, returnedCode)
    }

    @Test
    fun updateDiary_ErrorResponse() = runTest(ioDispatcher) {

        // 예외가 발생했는지 확인
        assertFailsWith<Exception> {
            diaryRemoteDataSource.updateDiary(1, "점심으로 타코를 먹었다", "왜 이렇게 맛있지", "기쁨", "", "맛있어서 행복한 하루").first()
        }
    }
}