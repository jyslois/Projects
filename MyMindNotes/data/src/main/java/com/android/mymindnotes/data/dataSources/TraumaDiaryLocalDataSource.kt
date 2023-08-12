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
class TraumaDiaryLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TraumaDiaryLocalDataSourceInterface {

    // DataStore Keys (TraumaDiary에 해당하는 DataStore keys)
    private val traumaEmotionColorKey = intPreferencesKey("traumaEmotionColor")
    private val traumaEmotionKey = stringPreferencesKey("traumaEmotion")
    private val traumaEmotionTextKey = stringPreferencesKey("traumaEmotionDescription")
    private val traumaSituationKey = stringPreferencesKey("traumaSituation")
    private val traumaThoughtKey = stringPreferencesKey("traumaThought")
    private val traumaReflectionKey = stringPreferencesKey("traumaReflection")
    private val traumaTypeKey = stringPreferencesKey("traumaType")
    private val traumaDateKey = stringPreferencesKey("traumaDate")
    private val traumaDayKey = stringPreferencesKey("traumaDay")

    // Save methods
    // EmotionColor
    override suspend fun saveEmotionColor(color: Int?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[traumaEmotionColorKey] = color ?: -1
            }
        }
    }

    // Emotion
    override suspend fun saveEmotion(emotion: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                emotion?.let { preferences[traumaEmotionKey] = it }
            }
        }
    }

    // EmotionText
    override suspend fun saveEmotionText(emotionText: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                emotionText?.let { preferences[traumaEmotionTextKey] = it }
            }
        }
    }

    // Situation
    override suspend fun saveSituation(situation: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                situation?.let { preferences[traumaSituationKey] = it }
            }
        }
    }

    // Thought
    override suspend fun saveThought(thought: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                thought?.let { preferences[traumaThoughtKey] = it }
            }
        }
    }

    // Reflection
    override suspend fun saveReflection(reflection: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                reflection?.let { preferences[traumaReflectionKey] = it }
            }
        }
    }

    // Type
    override suspend fun saveType(type: String) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[traumaTypeKey] = type
            }
        }
    }

    // Date
    override suspend fun saveDate(date: String) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[traumaDateKey] = date
            }
        }
    }

    // Day
    override suspend fun saveDay(day: String) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[traumaDayKey] = day
            }
        }
    }

    // Get methods
    // Emotion
    override val getEmotion: Flow<String?> = dataStore.data.map { preferences ->
        preferences[traumaEmotionKey]
    }.flowOn(ioDispatcher)

    // EmotionText
    override val getEmotionText: Flow<String?> = dataStore.data.map { preferences ->
        preferences[traumaEmotionTextKey]
    }.flowOn(ioDispatcher)


    // Situation
    override val getSituation: Flow<String?> = dataStore.data.map { preferences ->
        preferences[traumaSituationKey]
    }.flowOn(ioDispatcher)


    // Thought
    override val getThought: Flow<String?> = dataStore.data.map { preferences ->
        preferences[traumaThoughtKey]
    }.flowOn(ioDispatcher)


    // Reflection
    override val getReflection: Flow<String?> = dataStore.data.map { preferences ->
        preferences[traumaReflectionKey]
    }.flowOn(ioDispatcher)


    // Type
    // Reflection
    override val getType: Flow<String?> = dataStore.data.map { preferences ->
        preferences[traumaTypeKey]
    }.flowOn(ioDispatcher)

    // Date
    override val getDate: Flow<String?> = dataStore.data.map { preferences ->
        preferences[traumaDateKey]
    }.flowOn(ioDispatcher)

    // Day
    override val getDay: Flow<String?> = dataStore.data.map { preferences ->
        preferences[traumaDayKey]
    }.flowOn(ioDispatcher)



    // Clear methods
    override suspend fun clearTraumaDiaryTempRecords() {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences.remove(traumaEmotionColorKey)
                preferences.remove(traumaEmotionKey)
                preferences.remove(traumaEmotionTextKey)
                preferences.remove(traumaSituationKey)
                preferences.remove(traumaThoughtKey)
                preferences.remove(traumaReflectionKey)
                preferences.remove(traumaTypeKey)
                preferences.remove(traumaDateKey)
                preferences.remove(traumaDayKey)
            }
        }
    }

}

interface TraumaDiaryLocalDataSourceInterface {
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
    suspend fun clearTraumaDiaryTempRecords()
}