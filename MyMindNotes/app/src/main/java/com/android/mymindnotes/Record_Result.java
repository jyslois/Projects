package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityRecordResultBinding;

public class Record_Result extends AppCompatActivity {
    ActivityRecordResultBinding binding;
    SharedPreferences emotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 이전 버튼 클릭 시 전 페이지로
        binding.ResultPreviousButton.setOnClickListener(view -> {
            finish();
        });

        // 종료 버튼 클릭 시 mainpage로
        binding.ResultEndButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
        });

        // 수정 버튼 클릭 시 (미구현)
        binding.ResultEditButton.setOnClickListener(view -> {

        });

        // 감정 뿌리기
        emotion = getSharedPreferences("emotion", Activity.MODE_PRIVATE);
        binding.resultEmotionText.setText(emotion.getString("emotion", "감정"));
    }
}