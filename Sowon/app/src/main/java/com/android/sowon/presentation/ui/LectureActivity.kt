package com.android.sowon.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.sowon.databinding.ActivityLectureBinding

class LectureActivity : AppCompatActivity() {
    lateinit var binding: ActivityLectureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}