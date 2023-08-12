package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.core.dto.SaveDiaryResponse
import com.android.mymindnotes.data.retrofit.api.diary.SaveDiaryApi
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import com.android.mymindnotes.core.dto.UserDiary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraumaDiaryRemoteDataSource @Inject constructor(
    private val saveDiaryApi: SaveDiaryApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): TraumaDiaryRemoteDataSourceInterface {

    // (서버) 일기 저장하기
    override suspend fun saveDiary(userIndex: Int, type: String?, date: String?, day: String?, situation: String?, thought: String?, emotion: String?, emotionText: String?, reflection: String?): Flow<SaveDiaryResponse> = flow {
        val userDiary = UserDiary(
            userIndex,
            type,
            date,
            day,
            situation,
            thought,
            emotion,
            emotionText,
            reflection
        )
        val result = saveDiaryApi.addDiary(userDiary)
        emit(result)
    }.flowOn(ioDispatcher)

}

interface TraumaDiaryRemoteDataSourceInterface {
    suspend fun saveDiary(userIndex: Int, type: String?, date: String?, day: String?, situation: String?, thought: String?, emotion: String?, emotionText: String?, reflection: String?): Flow<SaveDiaryResponse>
}