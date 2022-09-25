package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.mymindnotes.databinding.ActivityRecordResultBinding;

public class Record_Result extends AppCompatActivity {
    ActivityRecordResultBinding binding;
    SharedPreferences emotion;
    SharedPreferences.Editor emotionEdit;
    SharedPreferences emotionText;
    SharedPreferences.Editor emotionTextEdit;
    SharedPreferences situation;
    SharedPreferences.Editor situationEdit;
    SharedPreferences thought;
    SharedPreferences.Editor thoughtEdit;
    SharedPreferences reflection;
    SharedPreferences.Editor reflectionEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emotion = getSharedPreferences("emotion", Activity.MODE_PRIVATE);
        emotionEdit = emotion.edit();
        emotionText = getSharedPreferences("emotionText", Activity.MODE_PRIVATE);
        emotionTextEdit = emotionText.edit();
        situation = getSharedPreferences("situation", MODE_PRIVATE);
        situationEdit = situation.edit();
        thought = getSharedPreferences("thought", MODE_PRIVATE);
        thoughtEdit = thought.edit();
        reflection = getSharedPreferences("reflection", MODE_PRIVATE);
        reflectionEdit = reflection.edit();

        // 이전 버튼 클릭 시 전 페이지로
        binding.ResultPreviousButton.setOnClickListener(view -> {
            finish();
        });

        // 종료 버튼 클릭 시 mainpage로 (모든 기록 삭제)
        binding.ResultEndButton.setOnClickListener(view -> {
            emotionEdit.clear();
            emotionEdit.commit();
            emotionTextEdit.clear();
            emotionTextEdit.commit();
            situationEdit.clear();
            situationEdit.commit();
            thoughtEdit.clear();
            thoughtEdit.commit();
            reflectionEdit.clear();
            reflectionEdit.commit();
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
        });

        // 수정 버튼 클릭 시 (미구현)
        binding.ResultEditButton.setOnClickListener(view -> {

        });

        // 감정 뿌리기
        binding.resultEmotionText.setText(emotion.getString("emotion", ""));
        // 감정 텍스트 뿌리기
        binding.ResultEmotionUserInput.setText(emotionText.getString("emotionText", ""));
        // 상황 텍스트 뿌리기
        binding.ResultSituationUserInput.setText(situation.getString("situation", ""));
        // 생각 텍스트 뿌리기
        binding.ResultThoughtUserInput.setText(thought.getString("thought", ""));
        // 회고 텍스트 뿌리기
        binding.ResultReflectionUserInput.setText(reflection.getString("reflection", ""));

        // 만약 감정 텍스트나 회고 텍스트가 비어 있다면, 나타나지 않게 하기.
        if (binding.ResultEmotionUserInput.getText().toString().equals("")) {
            binding.ResultEmotionUserInput.setVisibility(View.GONE);
        }
        if (binding.ResultReflectionUserInput.getText().toString().equals("")) {
            binding.ResultReflectionUserInput.setVisibility(View.GONE);
            binding.ResultReflectionIcon.setVisibility(View.GONE);
            binding.ResultReflectionTitle.setVisibility(View.GONE);
        }

    }
}