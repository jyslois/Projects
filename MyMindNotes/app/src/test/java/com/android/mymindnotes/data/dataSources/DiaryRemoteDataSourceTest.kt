package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
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
    private val fakeGetDiaryListApi = FakeGetDiaryListApi()
    private val fakeDeleteDiaryApi = FakeDeleteDiaryApi()
    private val fakeUpdateDiaryApi = FakeUpdateDiaryApi()
    private val ioDispatcher = StandardTestDispatcher()
    private val spyGetDiaryListApi = spyk(fakeGetDiaryListApi)

    @Before
    fun setUp() {
        diaryRemoteDataSource = DiaryRemoteDataSource(
            getDiaryListApi = spyGetDiaryListApi,
            deleteDiaryApi =  fakeDeleteDiaryApi,
            updateDiaryApi = fakeUpdateDiaryApi,
            ioDispatcher = ioDispatcher

        )
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `using fake class and spyk to test getDiaryList NormalResponse`() = runTest(ioDispatcher) {
        // Given
        val expectedDiaryList = arrayListOf(
            Diary("2023-01-01", "월요일", 1, "행복", "정말 좋은 하루", "좋은 일이 많았다.", "집에서 휴식", "좋은 생각", "오늘의 마음 일기", 1),
            Diary("2023-01-02", "화요일", 2, "공포", "무서웠다", "힘들었다.", "동물원에서 키가 큰 기린을 봤다", "목이 정말 기네", "트라우마 일기", 1)
        )
        val response = GetDiaryListResponse(code = 7000, msg = "일기 목록을 성공적으로 불러왔습니다", diaryList = expectedDiaryList)
        coEvery { spyGetDiaryListApi.getAllDiary(1) } returns response

        // When
        val returnedDiaryList = diaryRemoteDataSource.getDiaryList(1)

        // Then
        returnedDiaryList.collect {
            assertEquals("반환된 일기 목록이 예상과 다릅니다.", expectedDiaryList, it.diaryList)
        }
        coVerify { spyGetDiaryListApi.getAllDiary(1) }
    }

    @Test
    fun `using mockk to test getDiaryList_NormalResponse`() = runTest(ioDispatcher) {
        // Given
        val getDiaryListApi = mockk<GetDiaryListApi>()
        diaryRemoteDataSource = DiaryRemoteDataSource(
            getDiaryListApi = getDiaryListApi,
            deleteDiaryApi =  fakeDeleteDiaryApi,
            updateDiaryApi = fakeUpdateDiaryApi,
            ioDispatcher = ioDispatcher

        )
        val expectedDiaryList = arrayListOf(
            Diary("2023-01-01", "월요일", 1, "행복", "정말 좋은 하루", "좋은 일이 많았다.", "집에서 휴식", "좋은 생각", "오늘의 마음 일기", 1),
            Diary("2023-01-02", "화요일", 2, "공포", "무서웠다", "힘들었다.", "동물원에서 키가 큰 기린을 봤다", "목이 정말 기네", "트라우마 일기", 1)
        )
        val response = GetDiaryListResponse(code = 7000, msg = "일기 목록을 성공적으로 불러왔습니다", diaryList = expectedDiaryList)
        coEvery { getDiaryListApi.getAllDiary(1) } returns response

        // When
        val returnedDiaryList = diaryRemoteDataSource.getDiaryList(1)

        // Then
        // 정상적으로 데이터가 반환되었는지 확인
        returnedDiaryList.collect {
            assertEquals(7000, it.code)
            assertEquals("반환된 일기 목록이 예상과 다릅니다.", expectedDiaryList, it.diaryList)
        }
        coVerify { getDiaryListApi.getAllDiary(1) }
    }

    @Test
    fun getDiaryList_EmptyResponse() = runTest(ioDispatcher) {


        val response = diaryRemoteDataSource.getDiaryList(1).first()

        // 데이터가 비어있는지 확인
        assertTrue("일기 목록이 비어있지 않습니다", response.diaryList.isEmpty())
    }

    @Test
    fun getDiaryList_ErrorResponse(): Unit = runTest(ioDispatcher) {


        // 예외가 발생하는지 확인
//        assertFailsWith<Exception> {
//            // Flow는 Cold Stream이므로 Lazy(지연). 소비자가 데이터를 명시적으로 요청하기 전까지 데이터는 생성되지 않는다
//            diaryRemoteDataSource.getDiaryList(1).toList()
//        }

        // 혹은 에러 메시지 비교
        val exception = assertFailsWith<Exception> {
            // Flow는 Cold Stream이므로 Lazy(지연). 소비자가 데이터를 명시적으로 요청하기 전까지 데이터는 생성되지 않는다
            diaryRemoteDataSource.getDiaryList(1).toList()
        }
        assertEquals("오류 발생", exception.message)
    }

//    @Test
//    fun deleteDiary() {
//    }

    @Test
    fun deleteDiary_NormalResponse() = runTest(ioDispatcher) {
        // 상태를 NORMAL로 설정
        fakeDeleteDiaryApi.currentResponseType = FakeDeleteDiaryApi.ResponseType.NORMAL

        // 반환된 코드가 9000인지 확인
        val expectedCode = 9000

        val returnedCode = diaryRemoteDataSource.deleteDiary(1).first().code

        assertEquals("반환된 코드($returnedCode)가 예상 코드($expectedCode)와 다릅니다.", expectedCode, returnedCode)
    }

    @Test
    fun deleteDiary_ErrorResponse() = runTest(ioDispatcher) {
        // 상태를 ERROR로 설정
        fakeDeleteDiaryApi.currentResponseType = FakeDeleteDiaryApi.ResponseType.ERROR

        // 에러가 발생했는지 확인
        assertFailsWith<Exception> {
            diaryRemoteDataSource.deleteDiary(1).first()
        }
    }

//    @Test
//    fun updateDiary() {
//    }

    @Test
    fun updateDiary_NormsalResponse() = runTest(ioDispatcher) {
        // 상태 설정
        fakeUpdateDiaryApi.currentResponseType = FakeUpdateDiaryApi.ResponseType.NORMAL

        // 반환된 코드가 8000인지 확인
        val expectedCode = 8000
        val returnedCode = diaryRemoteDataSource.updateDiary(1, "점심으로 타코를 먹었다", "왜 이렇게 맛있지", "기쁨", "", "맛있어서 행복한 하루").first().code

        assertEquals("반환된 코드($returnedCode)가 예상 코드($expectedCode)와 다릅니다.", expectedCode, returnedCode)
    }

    @Test
    fun updateDiary_InvalidResponse() = runTest(ioDispatcher) {
        // 상태 설정
        fakeUpdateDiaryApi.currentResponseType = FakeUpdateDiaryApi.ResponseType.INVALID

        // 반한된 코드가 8001인지 확인
        val expectedCode = 8001
        val returnedCode = diaryRemoteDataSource.updateDiary(1, "", "", "기쁨", "", "맛있어서 행복한 하루").first().code

        assertEquals("반환된 코드($returnedCode)가 예상 코드($expectedCode)와 다릅니다.", expectedCode, returnedCode)
    }

    @Test
    fun updateDiary_ErrorResponse() = runTest(ioDispatcher) {
        // 상태 설정
        fakeUpdateDiaryApi.currentResponseType = FakeUpdateDiaryApi.ResponseType.ERROR

        // 예외가 발생했는지 확인
        assertFailsWith<Exception> {
            diaryRemoteDataSource.updateDiary(1, "점심으로 타코를 먹었다", "왜 이렇게 맛있지", "기쁨", "", "맛있어서 행복한 하루").first()
        }
    }
}