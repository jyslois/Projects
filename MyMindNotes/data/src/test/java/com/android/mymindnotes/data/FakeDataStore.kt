package com.android.mymindnotes.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class FakeDataStore(
    initialPreferences: Preferences = emptyPreferences()
) : DataStore<Preferences> {

    private val internalData = MutableStateFlow(initialPreferences)

    override val data: Flow<Preferences> = this.internalData

    // transform 함수를 인자로 받아서 실제로 Preferences 데이터를 수정하는 역할
    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val currentData = this.internalData.value
        // transform 함수를 호출하여 수정된 Preferences 데이터를 얻고, 그 결과를 internalData에 반영(emit)
        val updatedData = transform(currentData)
        this.internalData.emit(updatedData)
        return updatedData // 마지막으로 수정된 Preferences 데이터(updatedData)를 반환
    }

    // 테스트를 위한 도우미 메소드: 특정 키의 값을 가져오기
    fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): T {
        return internalData.value[key] ?: defaultValue
    }

    // 테스트를 위한 도우미 메소드: 특정 키의 값을 설정하기
    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        // updateData 함수를 호출하고, 인자로 전달할 transform 함수의 로직을 정의해서 전달
        // transform 함수의 로직: 현재의 Preferences 객체를 수정 가능한 MutablePreferences 객체로 만들고, 그 객체에 대해 주어진 key에 value를 설정하는 작업을 수행한 다음 그렇게 업데이트된 MutablePreferences 객체를 리턴한다
        updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[key] = value
            }
        }
    }

    // 테스트가 끝날 때마다 FakeDataStore의 상태를 초기화하는 메소드
    fun clear() {
        internalData.value = emptyPreferences()
    }
}