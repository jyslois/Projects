package com.android.sowon.data.retrofit.model

data class Lecture(
    val id: Long,
    val type: String,
    val image: Int,
    val title: String,
    val lecturer: String
): java.io.Serializable // required to pass an instance of the class as an extra in an intent
// 직렬화 된 데이터 클래스 인스턴스를 전달하려면 해당 클래스가 Serializable 또는 Parcelable을 구현해야 한다.