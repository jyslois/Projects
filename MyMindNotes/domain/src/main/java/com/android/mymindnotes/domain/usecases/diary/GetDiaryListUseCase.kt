package com.android.mymindnotes.domain.usecases.diary

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.core.model.DiaryListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDiaryListUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {

//    suspend fun getDiaryList(): Flow<Map<String, Object>> = diaryRepository.getDiaryList()

    suspend operator fun invoke(): Flow<Result<DiaryListResponse>> {
        return diaryRepository.getDiaryList().map {
            Result.success(it)
        }.catch {
            emit(Result.failure(RuntimeException("일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))
        }
    }

}