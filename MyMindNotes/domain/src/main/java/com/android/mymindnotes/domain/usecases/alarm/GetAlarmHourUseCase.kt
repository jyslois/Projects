package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmHourUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {

//    // 시간 불러오기
//    suspend fun getHour(): Flow<Int> = repository.getHour()

    suspend operator fun invoke(): Flow<Int> = repository.getHour()

}