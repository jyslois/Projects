package com.android.mymindnotes.data.dataSources

import android.content.SharedPreferences
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaDate
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaDay
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaEmotion
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaEmotionColor
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaEmotionText
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaReflection
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaSituation
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaThought
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.TraumaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TraumaDiaryLocalDataSource @Inject constructor(
    @TraumaEmotion private val emotion_sharedPreferences: SharedPreferences,
    @TraumaEmotionText private val emotionText_sharedPreferences: SharedPreferences,
    @TraumaSituation private val situation_sharedPreferences: SharedPreferences,
    @TraumaThought private val thought_sharedPreferences: SharedPreferences,
    @TraumaReflection private val reflection_sharedPreferences: SharedPreferences,
    @TraumaType private val type_sharedPreferences: SharedPreferences,
    @TraumaEmotionColor private val emotionColor_sharedPreferences: SharedPreferences,
    @TraumaDate private val date_sharedPreferences: SharedPreferences,
    @TraumaDay private val day_sharedPreferences: SharedPreferences,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): TraumaDiaryLocalDataSourceInterface {
    // Save methods
    // EmotionColor
    override suspend fun saveEmotionColor(color: Int) {
        withContext(ioDispatcher) {
            if (color == null) {
                emotionColor_sharedPreferences.edit().putInt("emotionColor", -1).commit()
            } else {
                emotionColor_sharedPreferences.edit().putInt("emotionColor", color).commit()
            }
        }
    }

    // Emotion
    override suspend fun saveEmotion(emotion: String?) {
        withContext(ioDispatcher) {
            emotion_sharedPreferences.edit().putString("emotion", emotion).commit()
        }
    }

    // EmotionText
    override suspend fun saveEmotionText(emotionText: String?) {
        withContext(ioDispatcher) {
            emotionText_sharedPreferences.edit().putString("emotionDescription", emotionText).commit()
        }
    }

    // Situation
    override suspend fun saveSituation(situation: String) {
        withContext(ioDispatcher) {
            situation_sharedPreferences.edit().putString("situation", situation).commit()
        }
    }

    // Thought
    override suspend fun saveThought(thought: String?) {
        withContext(ioDispatcher) {
            thought_sharedPreferences.edit().putString("thought", thought).commit()
        }
    }

    // Reflection
    override suspend fun saveReflection(reflection: String?) {
        withContext(ioDispatcher) {
            reflection_sharedPreferences.edit().putString("reflection", reflection).commit()
        }
    }

    // Type
    override suspend fun saveType(type: String) {
        withContext(ioDispatcher) {
            type_sharedPreferences.edit().putString("type", type).commit()
        }
    }

    // Date
    override suspend fun saveDate(date: String) {
        withContext(ioDispatcher) {
            date_sharedPreferences.edit().putString("date", date).commit()
        }
    }

    // Day
    override suspend fun saveDay(day: String) {
        withContext(ioDispatcher) {
            day_sharedPreferences.edit().putString("day", day).commit()
        }
    }

    // Get methods
    // Emotion
    override val getEmotion: Flow<String?> = flow {
        val emotion = emotion_sharedPreferences.getString("emotion", "")
        emit(emotion)
    }.flowOn(ioDispatcher)

    // EmotionText
    override val getEmotionText: Flow<String?> = flow {
        val emotionText = emotionText_sharedPreferences.getString("emotionDescription", "")
        emit(emotionText)
    }.flowOn(ioDispatcher)

    // Situation
    override val getSituation: Flow<String?> = flow {
        val situation = situation_sharedPreferences.getString("situation", "")
        emit(situation)
    }.flowOn(ioDispatcher)

    // Thought
    override val getThought: Flow<String?> = flow {
        val thought = thought_sharedPreferences.getString("thought", "")
        emit(thought)
    }.flowOn(ioDispatcher)

    // Reflection
    override val getReflection: Flow<String?> = flow {
        val reflection = reflection_sharedPreferences.getString("reflection", "")
        emit(reflection)
    }.flowOn(ioDispatcher)

    // Type
    // Reflection
    override val getType: Flow<String?> = flow {
        val type = type_sharedPreferences.getString("type", "")
        emit(type)
    }.flowOn(ioDispatcher)

    // Date
    override val getDate: Flow<String?> = flow {
        val date = date_sharedPreferences.getString("date", "")
        emit(date)
    }.flowOn(ioDispatcher)

    // Day
    override val getDay: Flow<String?> = flow {
        val day = day_sharedPreferences.getString("day", "")
        emit(day)
    }.flowOn(ioDispatcher)


    // Clear methods
    override suspend fun clearTraumaDiaryTempRecords() {
        withContext(ioDispatcher) {
            emotionColor_sharedPreferences.edit().clear().apply()
            emotion_sharedPreferences.edit().clear().apply()
            emotionText_sharedPreferences.edit().clear().apply()
            situation_sharedPreferences.edit().clear().apply()
            thought_sharedPreferences.edit().clear().apply()
            reflection_sharedPreferences.edit().clear().apply()
            type_sharedPreferences.edit().clear().apply()
            date_sharedPreferences.edit().clear().apply()
            day_sharedPreferences.edit().clear().commit()
        }
    }

}

interface TraumaDiaryLocalDataSourceInterface {
    suspend fun saveEmotionColor(color: Int)
    suspend fun saveEmotion(emotion: String?)
    suspend fun saveEmotionText(emotionText: String?)
    suspend fun saveSituation(situation: String)
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