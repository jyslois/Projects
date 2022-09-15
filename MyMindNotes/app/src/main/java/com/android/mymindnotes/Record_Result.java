package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityRecordResultBinding;

public class Record_Result extends AppCompatActivity {
    ActivityRecordResultBinding binding;

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
    }
}