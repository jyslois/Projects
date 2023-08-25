package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {

//    // Delete Diary
//    suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>> = diaryRepository.deleteDiary(diaryNumber)

    suspend operator fun invoke(diaryNumber: Int): Flow<Result<String>> {
        return diaryRepository.deleteDiary(diaryNumber).map { response ->
            when (response.code) {
                9000 -> Result.success("Success")
                else -> Result.failure(RuntimeException("일기 삭제 중 오류 발생."))
            }
        }.catch {
            emit(Result.failure(RuntimeException("일기 삭제에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))
        }
    }

}