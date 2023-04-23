package com.android.sowon.data

import com.android.sowon.R
import com.android.sowon.data.retrofit.model.Lecture

// 임시 - returns initial list of classes
class LectureList () {
    fun getLectureList(): List<Lecture> {
        return listOf(
            Lecture(
                1, "기초", R.drawable.example1, "기초 수업 초급반", "지연"
            ),
            Lecture(
                2, "기초", R.drawable.example2, "기초 수업 중급반", "지연"
            ),
            Lecture(
                3, "기초", R.drawable.example3, "기초 수업 고급반", "지연"
            ),
            Lecture(
                4, "카카오톡", R.drawable.kakaoexample1, "카카오톡 기초반", "지연"
            ),
            Lecture(
                5, "카카오톡", R.drawable.kakaoexample2, "카카오톡 심화반", "지연"
            ),
            Lecture(
                6, "배달의 민족", R.drawable.baeminexample1, "배달의민족 기초반", "지연"
            ),
            Lecture(
                7, "배달의 민족", R.drawable.baeminexample2, "배달의민족 심화반", "지연"
            )
        )
    }
}
