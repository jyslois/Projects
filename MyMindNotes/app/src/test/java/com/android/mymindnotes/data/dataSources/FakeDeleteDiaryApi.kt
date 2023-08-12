package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.core.dto.DeleteDiaryResponse
import com.android.mymindnotes.data.retrofit.api.diary.DeleteDiaryApi

class FakeDeleteDiaryApi : DeleteDiaryApi {

    enum class ResponseType {
        NORMAL, ERROR
    }

    var currentResponseType = ResponseType.NORMAL
    override suspend fun deleteDiary(diary_number: Int): DeleteDiaryResponse {
        return when (currentResponseType) {
            ResponseType.NORMAL -> DeleteDiaryResponse(code = 9000, "일기를 삭제하였습니다.")
            ResponseType.ERROR -> throw Exception("오류 발생")
        }
    }

}
