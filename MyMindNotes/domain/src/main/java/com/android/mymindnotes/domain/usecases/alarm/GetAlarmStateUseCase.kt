package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmStateUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(): Flow<Boolean> = repository.getAlarmState()

}