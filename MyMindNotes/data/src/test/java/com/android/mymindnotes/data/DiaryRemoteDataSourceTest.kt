package com.android.mymindnotes.data

import com.android.mymindnotes.core.dto.DeleteDiaryResponse
import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.core.dto.DiaryEdit
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.core.dto.UpdateDiaryResponse
import com.android.mymindnotes.data.dataSources.DiaryRemoteDataSource
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

    // getDiaryList()
    @Test
    fun getDiaryList_ReturnsExpectedDiaryList() = runTest(ioDispatcher) {
        // Given
        val expectedDiaryList = arrayListOf(
            Diary("2023-01-01", "월요일", 1, "행복", "정말 좋은 하루", "좋은 일이 많았다.", "집에서 휴식", "좋은 생각", "오늘의 마음 일기", 1),
            Diary("2023-01-02", "화요일", 2, "공포", "무서웠다", "힘들었다.", "동물원에서 키가 큰 기린을 봤다", "목이 정말 기네", "트라우마 일기", 1)
        )
        val expectedResponse = GetDiaryListResponse(code = 7000, msg = "일기목록을 성공적으로 불러왔습니다.", diaryList = expectedDiaryList)
        coEvery { mockGetDiaryListApi.getAllDiary(1) } returns expectedResponse

        // When
        val returnedResponse = diaryRemoteDataSource.getDiaryList(1)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse.code, it.code)
            assertEquals("반환된 일기 목록이 예상된 목록과 다릅니다. 예상: ${expectedResponse.diaryList}, 반환: ${it.diaryList}", expectedResponse.diaryList, it.diaryList)
        }
        coVerify { mockGetDiaryListApi.getAllDiary(1) }
    }

    @Test
    fun getDiaryList_ReturnsEmptyDiaryList() = runTest(ioDispatcher) {
        // Given
        val expectedResponse = GetDiaryListResponse(code = 7000, msg = "일기목록을 성공적으로 불러왔습니다.", diaryList = arrayListOf())
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
    fun getDiaryList_ThrowsRuntimeException(): Unit = runTest(ioDispatcher) {
        // Given
        coEvery { mockGetDiaryListApi.getAllDiary(1) } throws RuntimeException("일기목록 불러오던 중 오류 발생")

        // When
        val returnedResponse = diaryRemoteDataSource.getDiaryList(1)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockGetDiaryListApi.getAllDiary(1) }
    }


    // deleteDiary()
    @Test
    fun deleteDiary_ReturnsSuccessfulDeletionCode() = runTest(ioDispatcher) {
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
    fun deleteDiary_ThrowsRuntimeException() = runTest(ioDispatcher) {
        // Given
        coEvery { mockDeleteDiaryApi.deleteDiary(1) } throws RuntimeException("일기 삭제 중 오류 발생")

        // When
        val returnedResponse = diaryRemoteDataSource.deleteDiary(1)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockDeleteDiaryApi.deleteDiary(1) }
    }


    // updateDiary()
    @Test
    fun updateDiary_ReturnsSuccessfulUpdateCode() = runTest(ioDispatcher) {
        // Given
        val expectedResponse = UpdateDiaryResponse(code = 8000, msg = "일기를 수정하였습니다.")
        val diaryEdit = DiaryEdit(situation = "상황 수정", thought = "생각 수정", emotion = "기쁨", emotionDescription = "감정 설명 수정", reflection = "회고 수정")
        coEvery { mockUpdateDiaryApi.updateDiary(diary_number = 1, diaryEdit = diaryEdit) } returns expectedResponse

        // When
        val returnedResponse = diaryRemoteDataSource.updateDiary(diaryNumber = 1, diary = diaryEdit)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상 코드(${expectedResponse.code})와 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockUpdateDiaryApi.updateDiary(1, diaryEdit) }
    }

    @Test
    fun updateDiary_ReturnsUnsuccesfulUpdateCode() = runTest(ioDispatcher) {
        // Given
        val expectedResponse = UpdateDiaryResponse(code = 8001, msg = "형식을 준수하지 않아 일기 수정에 실패하였습니다.")
        val diaryEdit = DiaryEdit(situation = "상황 수정", thought = "생각 수정", emotion = "", emotionDescription = "감정 설명 수정", reflection = "회고 수정")
        coEvery { mockUpdateDiaryApi.updateDiary(diary_number = 1, diaryEdit = diaryEdit) } returns expectedResponse

        // When
        val returnedResponse = diaryRemoteDataSource.updateDiary(diaryNumber = 1, diary = diaryEdit)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상 코드(${expectedResponse.code})와 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockUpdateDiaryApi.updateDiary(1, diaryEdit) }
    }

    @Test
    fun updateDiary_ThrowsRuntimeException() = runTest(ioDispatcher) {
        // Given
        val diaryEdit = DiaryEdit(situation = "상황 수정", thought = "생각 수정", emotion = "기쁨", emotionDescription = "감정 설명 수정", reflection = "회고 수정")
        coEvery { mockUpdateDiaryApi.updateDiary(diary_number = 1, diaryEdit = diaryEdit) } throws RuntimeException("일기 수정 중 오류 발생")

        // When
        val returnedResponse = diaryRemoteDataSource.updateDiary(diaryNumber = 1, diary = diaryEdit)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockUpdateDiaryApi.updateDiary(1, diaryEdit) }
    }
}