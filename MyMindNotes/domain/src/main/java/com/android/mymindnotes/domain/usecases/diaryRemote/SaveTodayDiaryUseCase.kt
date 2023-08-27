package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDateUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDayUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryTypeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveTodayDiaryUseCase @Inject constructor(
    private val todayDiaryRepository: TodayDiaryRepository,
    private val saveTodayDiaryReflectionUseCase: SaveTodayDiaryReflectionUseCase,
    private val saveTodayDiaryTypeUseCase: SaveTodayDiaryTypeUseCase,
    private val saveTodayDiaryRecordDateUseCase: SaveTodayDiaryRecordDateUseCase,
    private val saveTodayDiaryRecordDayUseCase: SaveTodayDiaryRecordDayUseCase,
    private val clearTodayDiaryTempRecordsUseCase: ClearTodayDiaryTempRecordsUseCase
) {
    suspend operator fun invoke(reflection: String?, type: String, date: String, day: String): Flow<Result<String?>> {

        saveTodayDiaryReflectionUseCase(reflection)
        saveTodayDiaryTypeUseCase(type)
        saveTodayDiaryRecordDateUseCase(date)
        saveTodayDiaryRecordDayUseCase(day)

        return flow {
            try {
                val response = todayDiaryRepository.saveDiary().first()
                when (response.code) {
                    6001 -> emit(Result.failure(RuntimeException(response.msg)))
                    6000 -> {
                        clearTodayDiaryTempRecordsUseCase()
                        emit(Result.success(response.msg))
                    }
                }
            } catch (e: Exception) {
                emit(Result.failure(RuntimeException("일기 저장에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))
            }
        }
    }

}