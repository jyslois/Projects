package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDateUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDayUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryTypeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SaveTodayDiaryUseCase @Inject constructor(
    private val todayDiaryRepository: TodayDiaryRepository,
    private val saveTodayDiaryReflectionUseCase: SaveTodayDiaryReflectionUseCase,
    private val saveTodayDiaryTypeUseCase: SaveTodayDiaryTypeUseCase,
    private val saveTodayDiaryRecordDateUseCase: SaveTodayDiaryRecordDateUseCase,
    private val saveTodayDiaryRecordDayUseCase: SaveTodayDiaryRecordDayUseCase,
    private val clearTodayDiaryTempRecordsUseCase: ClearTodayDiaryTempRecordsUseCase
) {

//    // (서버) 일기 저장
//    suspend fun saveDiary(): Flow<Map<String, Object>> {
//        return repository.saveDiary()
//    }

    suspend operator fun invoke(reflection: String?, type: String, date: String, day: String): Flow<Result<String>> {

        saveTodayDiaryReflectionUseCase(reflection)
        saveTodayDiaryTypeUseCase(type)
        saveTodayDiaryRecordDateUseCase(date)
        saveTodayDiaryRecordDayUseCase(day)

        return todayDiaryRepository.saveDiary().map {
            when (it.code) {
                6001 -> Result.failure(RuntimeException(it.msg))
                6000 -> {
                    clearTodayDiaryTempRecordsUseCase()
                    Result.success("Success")
                }
                else -> Result.failure(RuntimeException("일기 저장 중 오류 발생."))
            }
        }.catch {
            emit(Result.failure(RuntimeException("일기 저장에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))
        }
    }

}