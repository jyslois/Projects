package com.android.sowon.data.retrofit.model

import com.android.sowon.R

// 임시 - returns initial list of classes
fun getClassList(): List<Class> {
    return listOf(
        Class(
            1, "기초", R.drawable.example1, "기초 수업 초급반", "지연"
        ),
        Class(
            2, "기초", R.drawable.example2, "기초 수업 중급반", "지연"
        ),
        Class(
            3, "기초", R.drawable.example3, "기초 수업 고급반", "지연"
        ),
        Class(
            4, "카카오톡", R.drawable.kakaoexample1, "카카오톡 기초반", "지연"
        ),
        Class(
            5, "카카오톡", R.drawable.kakaoexample2, "카카오톡 심화반", "지연"
        ),
        Class(
            6, "배달의 민족", R.drawable.baeminexample1, "배달의민족 기초반", "지연"
        ),
        Class(
            7, "배달의 민족", R.drawable.baeminexample2, "배달의민족 심화반", "지연"
        )


    )
}