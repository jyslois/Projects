package com.android.mymindnotes.data.datasources

import com.android.mymindnotes.data.retrofit.CheckEmailApi
import javax.inject.Inject

class DuplicateCheckDataSource @Inject constructor(
    val checkEmailApi: CheckEmailApi
)