package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi

// 실제 API를 호출하지 않는 대신 예상된 응답 또는 테스트 용도로 준비된 응답을 반환하는 클래스
/*
정상적인 응답: 일기 목록을 정상적으로 반환하는 경우
빈 응답: 일기 목록이 비어 있는 경우의 응답
오류 응답: 어떤 오류가 발생했을 때의 예상 응답 (이 경우는 FakeGetDiaryListApi에서는 직접 예외를 발생시키는 것으로 처리)
 */
class FakeGetDiaryListApi : GetDiaryListApi {

    // 테스트용 일기 데이터
    private val fakeDiaries = arrayListOf(
        Diary("2023-01-01", "월요일", 1, "행복", "정말 좋은 하루", "좋은 일이 많았다.", "집에서 휴식", "좋은 생각", "오늘의 마음 일기", 1),
        Diary("2023-01-02", "화요일", 2, "공포", "무서웠다", "힘들었다.", "동물원에서 키가 큰 기린을 봤다", "목이 정말 기네", "트라우마 일기", 1)
    )

    // 테스트의 종류에 따라서 상태를 바꿀 수 있는 enum
    enum class ResponseType {
        NORMAL, EMPTY, ERROR
    }

    // 현재 상태를 저장하는 변수
    var currentResponseType = ResponseType.NORMAL

    override suspend fun getAllDiary(user_index: Int): GetDiaryListResponse {
        return when(currentResponseType) {
            ResponseType.NORMAL -> GetDiaryListResponse(diaryList = fakeDiaries, code = 7000, msg = "일기 목록을 성공적으로 불러왔습니다.") // 일기가 있을 때
            ResponseType.EMPTY -> GetDiaryListResponse(diaryList = arrayListOf(), code = 7000, msg = "일기 목록을 성공적으로 불러왔습니다.") // 일기가 없을 때 (빈 응답)
            ResponseType.ERROR -> throw Exception("오류 발생")  // 오류 응답
        }
    }
}
