package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UseSharedPreferencesUseCase @Inject constructor(
    private val repository: SharedPreferencesRepository
) {
    // get methods
    suspend fun getAutoLogin(): Flow<Boolean> {
        return repository.getAutoLoginCheck()
    }

    suspend fun getAutoSave(): Flow<Boolean> {
        return repository.getAutoSaveCheck()
    }

    suspend fun getId(): Flow<String?> {
        return repository.getId()
    }

    suspend fun getPassword(): Flow<String?> {
        return repository.getPassword()
    }


    suspend fun getFirstTime(): Flow<Boolean> {
        return repository.getFirstTime()
    }

    // save methods
    suspend fun saveAutoLoginCheck(state: Boolean) {
        repository.saveAutoLoginCheck(state)
    }

    suspend fun saveAutoSaveCheck(state: Boolean) {
        repository.saveAutoSaveCheck(state)
    }

    suspend fun saveIdAndPassword(id: String?, password: String?) {
        repository.saveIdAndPassword(id, password)
    }

    suspend fun saveUserIndex(index: Int) {
        repository.saveUserIndex(index)
    }

    suspend fun saveFirstTime(state: Boolean) {
        repository.saveFirstTime(state)
    }

    // clear sharedpreferences
    suspend fun clearAutoSaveSharedPreferences() {
        repository.clearAutoSaveSharedPreferences()
    }

}