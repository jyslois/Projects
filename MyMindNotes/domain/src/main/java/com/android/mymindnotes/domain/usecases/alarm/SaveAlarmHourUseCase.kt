package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class SaveAlarmHourUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(hour: Int) = repository.saveHour(hour)

}