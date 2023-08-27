package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDiaryListUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(): Flow<Result<GetDiaryListResponse>> {

        return flow {
            try {
                val response = diaryRepository.getDiaryList().first()
                if (response.code == 7000) {
                    emit(Result.success(response))
                }
            } catch (e: Exception) {
                emit(Result.failure(RuntimeException("일기 목록을 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))
            }
        }
    }
}