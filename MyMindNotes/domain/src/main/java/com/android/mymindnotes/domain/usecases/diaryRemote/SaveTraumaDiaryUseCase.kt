package com.android.mymindnotes.domain.usecases.diaryRemote

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDateUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDayUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryTypeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveTraumaDiaryUseCase @Inject constructor(
    private val traumaDiaryRepository: TraumaDiaryRepository,
    private val saveTraumaDiaryReflectionUseCase: SaveTraumaDiaryReflectionUseCase,
    private val saveTraumaDiaryTypeUseCase: SaveTraumaDiaryTypeUseCase,
    private val saveTraumaDiaryRecordDateUseCase: SaveTraumaDiaryRecordDateUseCase,
    private val saveTraumaDiaryRecordDayUseCase: SaveTraumaDiaryRecordDayUseCase,
    private val clearTraumaDiaryTempRecordsUseCase: ClearTraumaDiaryTempRecordsUseCase
) {

    suspend operator fun invoke(reflection: String?, type: String, date: String, day: String): Flow<Result<String?>> {

        saveTraumaDiaryReflectionUseCase(reflection)
        saveTraumaDiaryTypeUseCase(type)
        saveTraumaDiaryRecordDateUseCase(date)
        saveTraumaDiaryRecordDayUseCase(day)

        return flow {
            try {
                val response = traumaDiaryRepository.saveDiary().first()
                when (response.code) {
                    6001 -> emit(Result.failure(RuntimeException(response.msg)))
                    6000 -> {
                        clearTraumaDiaryTempRecordsUseCase()
                        emit(Result.success(response.msg))
                    }
                }
            } catch (e: Exception) {
                emit(Result.failure(RuntimeException("일기 저장에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")))
            }
        }
    }
}

