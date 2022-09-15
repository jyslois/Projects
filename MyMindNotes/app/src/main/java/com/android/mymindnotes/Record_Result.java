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

        // 종료 버튼 클릭 시 mainpage로
        binding.ResultEndButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
        });
    }
}