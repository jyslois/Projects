package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.core.dto.DiaryEdit
import com.android.mymindnotes.core.dto.UpdateDiaryResponse
import com.android.mymindnotes.data.retrofit.api.diary.UpdateDiaryApi
import java.lang.Exception

class FakeUpdateDiaryApi : UpdateDiaryApi {

    enum class ResponseType {
        NORMAL, INVALID, ERROR
    }

    var currentResponseType = ResponseType.NORMAL
    override suspend fun updateDiary(diary_number: Int, diaryEdit: DiaryEdit): UpdateDiaryResponse {
        return when (currentResponseType) {
            ResponseType.NORMAL -> UpdateDiaryResponse(code = 8000, "일기를 수정하였습니다.")
            ResponseType.INVALID -> UpdateDiaryResponse(code = 8001, "필수 사항 미기입. 일기 수정에 실패했습니다.")
            ResponseType.ERROR -> throw Exception("오류 발생")
        }
    }

}
