package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.android.mymindnotes.databinding.ActivityRecordMindChoiceBinding;
import com.bumptech.glide.Glide;

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
        binding.nickNameText.setText(nick + " 님,");


        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground2).into(binding.choicebackground);


        // 오늘의 마음 일기 버튼 클릭 시
        binding.todayEmotionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), New_Emotion.class);
            startActivity(intent);
        });

        // 트라우마 일기 버튼 클릭 시
        binding.traumaButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Old_Situation.class);
            startActivity(intent);
        });



    }

}