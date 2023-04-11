package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.DiaryDataSource
import com.android.mymindnotes.data.datasources.MemberSharedPreferencesDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.DiaryRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import com.bumptech.glide.Glide.init
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
    private val _getDiaryListError = MutableSharedFlow<Boolean>()
    override val getDiaryListError = _getDiaryListError.asSharedFlow()

    private val _deleteDiaryError = MutableSharedFlow<Boolean>()
    override val deleteDiaryError = _deleteDiaryError.asSharedFlow()

    // Get Diary List
    override suspend fun getDiaryList(): Flow<Map<String, Object>> {
        val userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
        return diaryDataSource.getDiaryList(userIndex)
    }

    // Delete Diary
    override suspend fun deleteDiary(diaryNumber: Int): Flow<Map<String, Object>> = diaryDataSource.deleteDiary(diaryNumber)

    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                // 에러 메시지 collect & emit
                diaryDataSource.getDiaryListError.collect {
                    _getDiaryListError.emit(it)
                }
            }

            launch {
                diaryDataSource.deleteDiaryError.collect {
                    _deleteDiaryError.emit(it)
                }
            }
        }
    }

}