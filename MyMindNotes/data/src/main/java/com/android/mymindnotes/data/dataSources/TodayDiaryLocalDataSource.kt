package com.android.mymindnotes.data.dataSources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodayDiaryLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TodayDiaryLocalDataSourceInterface {

    // DataStore Keys
    private val todayEmotionColorKey = intPreferencesKey("todayEmotionColor")
    private val todayEmotionKey = stringPreferencesKey("todayEmotion")
    private val todayEmotionTextKey = stringPreferencesKey("todayEmotionDescription")
    private val todaySituationKey = stringPreferencesKey("todaySituation")
    private val todayThoughtKey = stringPreferencesKey("todayThought")
    private val todayReflectionKey = stringPreferencesKey("todayReflection")
    private val todayTypeKey = stringPreferencesKey("todayType")
    private val todayDateKey = stringPreferencesKey("todayDate")
    private val todayDayKey = stringPreferencesKey("todayDay")

    // Save methods
    // EmotionColor
    override suspend fun saveEmotionColor(color: Int?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[todayEmotionColorKey] = color ?: -1
            }
        }
    }

    // Emotion
    override suspend fun saveEmotion(emotion: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                emotion?.let { preferences[todayEmotionKey] = it }
            }
        }
    }

    // EmotionText
    override suspend fun saveEmotionText(emotionText: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                emotionText?.let { preferences[todayEmotionTextKey] = it }
            }
        }
    }

    // Situation
    override suspend fun saveSituation(situation: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                situation?.let { preferences[todaySituationKey] = it }
            }
        }
    }

    // Thought
    override suspend fun saveThought(thought: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                thought?.let { preferences[todayThoughtKey] = it }
            }
        }
    }

    // Reflection
    override suspend fun saveReflection(reflection: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                reflection?.let { preferences[todayReflectionKey] = it }
            }
        }
    }

    // Type
    override suspend fun saveType(type: String) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[todayTypeKey] = type
            }
        }
    }

    // Date
    override suspend fun saveDate(date: String) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[todayDateKey] = date
            }
        }
    }

    // Day
    override suspend fun saveDay(day: String) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[todayDayKey] = day
            }
        }
    }

    // Get methods
    // Emotion
    override val getEmotion: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[todayEmotionKey]
        }.flowOn(ioDispatcher)


    // EmotionText
    override val getEmotionText: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[todayEmotionTextKey]
        }.flowOn(ioDispatcher)

    // Situation
    override val getSituation: Flow<String?> = dataStore.data.map { preferences ->
        preferences[todaySituationKey]
    }.flowOn(ioDispatcher)

    // Thought
    override val getThought: Flow<String?> = dataStore.data.map { preferences ->
        preferences[todayThoughtKey]
    }.flowOn(ioDispatcher)

    // Reflection
    override val getReflection: Flow<String?> = dataStore.data.map { preferences ->
        preferences[todayReflectionKey]
    }.flowOn(ioDispatcher)

    // Type
    // Reflection
    override val getType: Flow<String?> = dataStore.data.map { preferences ->
        preferences[todayTypeKey]
    }.flowOn(ioDispatcher)

    // Date
    override val getDate: Flow<String?> = dataStore.data.map { preferences ->
        preferences[todayDateKey]
    }.flowOn(ioDispatcher)

    // Day
    override val getDay: Flow<String?> = dataStore.data.map { preferences ->
        preferences[todayDayKey]
    }.flowOn(ioDispatcher)


    // Clear method
    override suspend fun clearTodayDiaryTempRecords() {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences.remove(todayEmotionColorKey)
                preferences.remove(todayEmotionKey)
                preferences.remove(todayEmotionTextKey)
                preferences.remove(todaySituationKey)
                preferences.remove(todayThoughtKey)
                preferences.remove(todayReflectionKey)
                preferences.remove(todayTypeKey)
                preferences.remove(todayDateKey)
                preferences.remove(todayDayKey)
            }
        }
    }

}

interface TodayDiaryLocalDataSourceInterface {
    suspend fun saveEmotionColor(color: Int?)
    suspend fun saveEmotion(emotion: String?)
    suspend fun saveEmotionText(emotionText: String?)
    suspend fun saveSituation(situation: String?)
    suspend fun saveThought(thought: String?)
    suspend fun saveReflection(reflection: String?)
    suspend fun saveType(type: String)
    suspend fun saveDate(date: String)
    suspend fun saveDay(day: String)
    val getEmotion: Flow<String?>
    val getEmotionText: Flow<String?>
    val getSituation: Flow<String?>
    val getThought: Flow<String?>
    val getReflection: Flow<String?>
    val getType: Flow<String?>
    val getDate: Flow<String?>
    val getDay: Flow<String?>
    suspend fun clearTodayDiaryTempRecords()
}