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
    override suspend fun saveDiary(diaryInfo: UserDiary): Flow<SaveDiaryResponse> = flow {
        val result = saveDiaryApi.addDiary(diaryInfo)
        emit(result)
    }.flowOn(ioDispatcher)

}

interface TraumaDiaryRemoteDataSourceInterface {
    suspend fun saveDiary(diaryInfo: UserDiary): Flow<SaveDiaryResponse>
}