package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityOldEmotionBinding;

public class Old_Emotion extends AppCompatActivity {
    ActivityOldEmotionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldEmotionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 어떤 감정인지 모르겠어요, 도와주세요 버튼 클릭 -> 감정 설명서 페이지로 이동
        binding.RecordEmotionHelpButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        });

        // 다음 버튼 클릭
        binding.RecordNextButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Old_Reflection.class);
            startActivity(intent);
        });

        // Tips 다이얼로그 준비
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("감정 작성 Tips");
        builder.setMessage(R.string.emotionTips);
        builder.setPositiveButton("확인", null);

        // Tips 버튼 누르면 다이얼로그 띄우기
        binding.RecordEmotionTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
}