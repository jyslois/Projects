package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.DiaryDataSource
import com.android.mymindnotes.data.datasources.MemberSharedPreferencesDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.DiaryRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryDataSource: DiaryDataSource,
    private val memberSharedPreferencesDataSource: MemberSharedPreferencesDataSource,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
): DiaryRepository {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    override val error = _error.asSharedFlow()

    // Get Diary List
    override suspend fun getDiaryList(): Flow<Map<String, Object>> {
        val userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
        return diaryDataSource.getDiaryList(userIndex)
    }

    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                // 에러 메시지 collect & emit
                diaryDataSource.error.collect {
                    _error.emit(it)
                }
            }
        }
    }

}