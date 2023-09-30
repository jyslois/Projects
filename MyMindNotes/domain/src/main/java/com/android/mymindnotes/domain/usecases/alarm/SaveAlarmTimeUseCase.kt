package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class SaveAlarmTimeUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(time: String) = repository.saveTime(time)

}