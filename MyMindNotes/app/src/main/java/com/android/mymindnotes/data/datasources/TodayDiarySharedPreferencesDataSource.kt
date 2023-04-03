package com.android.mymindnotes.data.datasources

import android.content.SharedPreferences
import com.android.mymindnotes.hilt.module.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodayDiarySharedPreferencesDataSource @Inject constructor(
    @Emotion private val emotion_sharedPreferences: SharedPreferences,
    @EmotionText private val emotionText_sharedPreferences: SharedPreferences,
    @Situation private val situation_sharedPreferences: SharedPreferences,
    @Thought private val thought_sharedPreferences: SharedPreferences,
    @Reflection private val reflection_sharedPreferences: SharedPreferences,
    @Type private val type_sharedPreferences: SharedPreferences,
    @EmotionColor private val emotionColor_sharedPreferences: SharedPreferences,
    @Date private val date_sharedPreferences: SharedPreferences,
    @Day private val day_sharedPreferences: SharedPreferences,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
    ) {

    // Save methods
    // EmotionColor
    suspend fun saveEmotionColor(color: Int) {
        withContext(ioDispatcher) {
            if (color == null) {
                emotionColor_sharedPreferences.edit().putInt("emotionColor", -1).commit()
            } else {
                emotionColor_sharedPreferences.edit().putInt("emotionColor", color).commit()
            }
        }
    }

    // Emotion
    suspend fun saveEmotion(emotion: String?) {
        withContext(ioDispatcher) {
            emotion_sharedPreferences.edit().putString("emotion", emotion).commit()
        }
    }

    // EmotionText
    suspend fun saveEmotionText(emotionText: String?) {
        withContext(ioDispatcher) {
            emotionText_sharedPreferences.edit().putString("emotionText", emotionText).commit()
        }
    }

    // Situation
    suspend fun saveSituation(situation: String?) {
        withContext(ioDispatcher) {
            situation_sharedPreferences.edit().putString("situation", situation).commit()
        }
    }

    // Thought
    suspend fun saveThought(thought: String?) {
        withContext(ioDispatcher) {
            thought_sharedPreferences.edit().putString("thought", thought).commit()
        }
    }

    // Reflection
    suspend fun saveReflection(reflection: String?) {
        withContext(ioDispatcher) {
            reflection_sharedPreferences.edit().putString("reflection", reflection).commit()
        }
    }

    // Type
    suspend fun saveType(type: String) {
        withContext(ioDispatcher) {
            type_sharedPreferences.edit().putString("type", type).commit()
        }
    }

    // Date
    suspend fun saveDate(date: String) {
        withContext(ioDispatcher) {
            date_sharedPreferences.edit().putString("date", date).commit()
        }
    }

    // Day
    suspend fun saveDay(day: String) {
        withContext(ioDispatcher) {
            day_sharedPreferences.edit().putString("day", day).commit()
        }
    }

    // Get methods
    // Emotion
    val getEmotion: Flow<String?> = flow {
        val emotion = emotion_sharedPreferences.getString("emotion", "")
        emit(emotion)
    }.flowOn(ioDispatcher)

    // EmotionText
    val getEmotionText: Flow<String?> = flow {
        val emotionText = emotionText_sharedPreferences.getString("emotionText", "")
        emit(emotionText)
    }.flowOn(ioDispatcher)

    // Situation
    val getSituation: Flow<String?> = flow {
        val situation = situation_sharedPreferences.getString("situation", "")
        emit(situation)
    }.flowOn(ioDispatcher)

    // Thought
    val getThought: Flow<String?> = flow {
        val thought = thought_sharedPreferences.getString("thought", "")
        emit(thought)
    }.flowOn(ioDispatcher)

    // Reflection
    val getReflection: Flow<String?> = flow {
        val reflection = reflection_sharedPreferences.getString("reflection", "")
        emit(reflection)
    }.flowOn(ioDispatcher)

    // Type
    // Reflection
    val getType: Flow<String?> = flow {
        val type = type_sharedPreferences.getString("type", "")
        emit(type)
    }.flowOn(ioDispatcher)

    // Date
    val getDate: Flow<String?> = flow {
        val date = date_sharedPreferences.getString("date", "")
        emit(date)
    }.flowOn(ioDispatcher)

    // Day
    val getDay: Flow<String?> = flow {
        val day = day_sharedPreferences.getString("day", "")
        emit(day)
    }.flowOn(ioDispatcher)


    // Clear methods
    // EmotionColor
    suspend fun clearEmotionColorSharedPreferences() {
        withContext(ioDispatcher) {
            emotionColor_sharedPreferences.edit().clear().commit()
        }
    }

    // Emotion
    suspend fun clearEmotionSharedPreferences() {
        withContext(ioDispatcher) {
            emotion_sharedPreferences.edit().clear().commit()
        }
    }

    // EmotionText
    suspend fun clearEmotionTextSharedPreferences() {
        withContext(ioDispatcher) {
            emotionText_sharedPreferences.edit().clear().commit()
        }
    }

    // Situation
    suspend fun clearSituationSharedPreferences() {
        withContext(ioDispatcher) {
            situation_sharedPreferences.edit().clear().commit()
        }
    }

    // Thought
    suspend fun clearThoughtSharedPreferences() {
        withContext(ioDispatcher) {
            thought_sharedPreferences.edit().clear().commit()
        }
    }

    // Reflection
    suspend fun clearReflectionSharedPreferences() {
        withContext(ioDispatcher) {
            reflection_sharedPreferences.edit().clear().commit()
        }
    }

    // Type
    suspend fun clearTypeSharedPreferences() {
        withContext(ioDispatcher) {
            type_sharedPreferences.edit().clear().commit()
        }
    }

    // Date
    suspend fun clearDateSharedPreferences() {
        withContext(ioDispatcher) {
            date_sharedPreferences.edit().clear().commit()
        }
    }

    // Day
    suspend fun clearDaySharedPreferences() {
        withContext(ioDispatcher) {
            day_sharedPreferences.edit().clear().commit()
        }
    }

}