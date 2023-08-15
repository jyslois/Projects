package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi

// 실제 API를 호출하지 않는 대신 예상된 응답 또는 테스트 용도로 준비된 응답을 반환하는 클래스
/*
정상적인 응답: 일기 목록을 정상적으로 반환하는 경우
빈 응답: 일기 목록이 비어 있는 경우의 응답
오류 응답: 어떤 오류가 발생했을 때의 예상 응답 (이 경우는 FakeGetDiaryListApi에서는 직접 예외를 발생시키는 것으로 처리)
 */
class FakeGetDiaryListApi : GetDiaryListApi {
    override suspend fun getAllDiary(user_index: Int): GetDiaryListResponse {
        return GetDiaryListResponse(
            diaryList = arrayListOf(),
            code = 7000,
            msg = "일기 목록을 성공적으로 불러왔습니다."
        )
    }
}

