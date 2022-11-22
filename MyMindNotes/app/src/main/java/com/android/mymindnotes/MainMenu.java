package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityMainMenuBinding;
import com.bumptech.glide.Glide;

public class MainMenu extends AppCompatActivity {
    ActivityMainMenuBinding binding;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainmenubackground).into(binding.background);

        // 버튼 클릭 이벤트

        binding.recordDiaryButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RecordMindChoice.class);
            startActivity(intent);
        });

        binding.diaryButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Diary.class);
            startActivity(intent);
        });

        binding.emotionInstructionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        });

        binding.accountsettingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), accountInformation.class);
            startActivity(intent);
        });

        binding.logoutButton.setOnClickListener(view -> {
            // 상태 저장
            autoSaveEdit.putBoolean("autoLoginCheck", false);
            autoSaveEdit.apply();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

    }
}