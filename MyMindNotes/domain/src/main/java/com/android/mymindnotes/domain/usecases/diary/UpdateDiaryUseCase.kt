package com.android.mymindnotes.domain.usecases.diary

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {

//    // Update Diary
//    suspend fun updateDiary(
//        diaryNumber: Int,
//        situation: String,
//        thought: String,
//        emotion: String,
//        emotionDescription: String?,
//        reflection: String?
//    ): Flow<Map<String, Object>> = diaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection)

    suspend operator fun invoke(
        diaryNumber: Int,
        situation: String,
        thought: String,
        emotion: String,
        emotionDescription: String?,
        reflection: String?
    ): Flow<Result<String>> {
        return diaryRepository.updateDiary(
            diaryNumber,
            situation,
            thought,
            emotion,
            emotionDescription,
            reflection
        ).map { response ->
            when (response.code) {
                8001 -> Result.failure(RuntimeException(response.message))
                8000 -> Result.success("Success")
                else -> Result.failure(RuntimeException("일기 변경 중 오류 발생"))
            }
        }.catch {
            emit(Result.failure(RuntimeException("일기 수정 실패. 인터넷 연결을 확인해 주세요.")))
        }
    }

}