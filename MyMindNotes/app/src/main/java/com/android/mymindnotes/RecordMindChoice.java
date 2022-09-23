package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityRecordMindChoiceBinding;

public class RecordMindChoice extends AppCompatActivity {
    ActivityRecordMindChoiceBinding binding;
    SharedPreferences nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRecordMindChoiceBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // 닉네임 설정
        nickName = getSharedPreferences("nickName", Activity.MODE_PRIVATE);
        String nick = nickName.getString("nickName", "");
        binding.nickName.setText(nick + " 님,");


        // 오늘의 마음 일기 버튼 클릭 시
        binding.newMindButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), New_Emotion.class);
            startActivity(intent);
        });

        // 트라우마 일기 버튼 클릭 시
        binding.oldMindButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Old_Situation.class);
            startActivity(intent);
        });



    }

}