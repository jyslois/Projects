package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(diaryNumber: Int, situation: String, thought: String, emotion: String, emotionDescription: String?, reflection: String?
    ): Flow<Result<String?>> {

        return flow {
            try {
                val response = diaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection).first()
                when (response.code) {
                    8000 -> emit(Result.success(response.msg))
                    8001 -> emit(Result.failure(RuntimeException(response.msg)))
                }
            } catch (e: Exception) {
                emit(Result.failure(RuntimeException("일기 수정 실패. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }

}