package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.DuplicateCheckDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.DuplicateCheckRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class DuplicateCheckRepositoryImpl @Inject constructor(
    private val duplicateCheckDataSource: DuplicateCheckDataSource
) : DuplicateCheckRepository {

    // SharedFlow
    // 이메일 중복 체크 결과 저장 플로우
    private val _emailCheckResult = MutableSharedFlow<Map<String, Object>>()
    override val emailCheckResult = _emailCheckResult.asSharedFlow()

    override suspend fun checkEmail(emailInput: String) {
        val result = duplicateCheckDataSource.checkEmailApi.checkEmail(emailInput)
        _emailCheckResult.emit(result)
    }

}