package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(diaryNumber: Int): Flow<Result<String?>> {

        return flow {
            try {
                val response = diaryRepository.deleteDiary(diaryNumber).first()
                if (response.code == 9000) {
                    emit(Result.success(response.msg))
                }
            } catch (e: Exception) {
                emit(Result.failure(RuntimeException("일기 삭제에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))

            }
        }
    }
}