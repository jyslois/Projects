package com.android.sowon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.sowon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 임시
        binding.kakaoLoginButton.setOnClickListener {
            // 메인 페이지로 이동
            val intent: Intent = Intent(this, MainPage::class.java)
            // 인텐트를 시스템에 전달(시작)
            startActivity(intent) }


    }
}