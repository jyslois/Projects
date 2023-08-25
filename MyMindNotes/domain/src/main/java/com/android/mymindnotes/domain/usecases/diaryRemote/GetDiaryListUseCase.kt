package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDiaryListUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {

//    suspend fun getDiaryList(): Flow<Map<String, Object>> = diaryRepository.getDiaryList()

    suspend operator fun invoke(): Flow<Result<GetDiaryListResponse>> {
        return diaryRepository.getDiaryList().map { response ->
            when (response.code) {
                7000 -> Result.success(response)
                else -> Result.failure(RuntimeException("일기 목록을 불러오던 중 오류 발생."))
            }
        }.catch {
            emit(Result.failure(RuntimeException("일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))
        }
    }

}