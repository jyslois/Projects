package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDateUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDayUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryTypeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SaveTraumaDiaryUseCase @Inject constructor(
    private val traumaDiaryRepository: TraumaDiaryRepository,
    private val saveTraumaDiaryReflectionUseCase: SaveTraumaDiaryReflectionUseCase,
    private val saveTraumaDiaryTypeUseCase: SaveTraumaDiaryTypeUseCase,
    private val saveTraumaDiaryRecordDateUseCase: SaveTraumaDiaryRecordDateUseCase,
    private val saveTraumaDiaryRecordDayUseCase: SaveTraumaDiaryRecordDayUseCase,
    private val clearTraumaDiaryTempRecordsUseCase: ClearTraumaDiaryTempRecordsUseCase
) {

//    // (서버) 일기 저장
//    suspend fun saveDiary(): Flow<Map<String, Object>> {
//        return repository.saveDiary()
//    }

    suspend operator fun invoke(reflection: String?, type: String, date: String, day: String): Flow<Result<String>> {

        saveTraumaDiaryReflectionUseCase(reflection)
        saveTraumaDiaryTypeUseCase(type)
        saveTraumaDiaryRecordDateUseCase(date)
        saveTraumaDiaryRecordDayUseCase(day)

        return traumaDiaryRepository.saveDiary().map {
            when (it.code) {
                6001 -> Result.failure(RuntimeException(it.msg))
                6000 -> {
                    clearTraumaDiaryTempRecordsUseCase()
                    Result.success("Success")
                }
                else -> Result.failure(RuntimeException("일기 저장 중 오류 발생."))
            }
        }.catch {
            emit(Result.failure(RuntimeException("일기 저장에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))
        }
    }
}

