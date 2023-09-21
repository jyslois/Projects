package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class SaveAlarmTimeUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    // time 저장하기
    suspend operator fun invoke(time: String) = repository.saveTime(time)


}