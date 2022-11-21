package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.mymindnotes.databinding.ActivityRecordResultBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    SharedPreferences type;
    SharedPreferences.Editor typeEdit;
    SharedPreferences emotionColor;
    SharedPreferences.Editor emotionColorEdit;

    SharedPreferences arrayList;
    SharedPreferences.Editor arrayListEdit;
    ArrayList<Record> recordList;

    @Override
    protected void onResume() {
        super.onResume();

        Gson gson = new Gson();
        String json = arrayList.getString("arrayList", "");
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        recordList = gson.fromJson(json, type);

        int index = recordList.size() - 1;

        // 상황 텍스트 뿌리기
        binding.ResultSituationUserInput.setText(recordList.get(index).situation);
        // 생각 텍스트 뿌리기
        binding.ResultThoughtUserInput.setText(recordList.get(index).thought);
        // 감정 뿌리기
        binding.resultEmotionText.setText(recordList.get(index).emotionWord);
        // 감정 텍스트 뿌리기
        binding.ResultEmotionUserInput.setText(recordList.get(index).emotionText);
        // 회고 텍스트 뿌리기
        binding.ResultReflectionUserInput.setText(recordList.get(index).reflection);

        // 만약 감정 텍스트나 회고 텍스트가 비어 있다면, 나타나지 않게 하기. 비어 있지 않다면, 보이게 하기.
        if (binding.ResultEmotionUserInput.getText().toString().equals("")) {
            binding.ResultEmotionUserInput.setVisibility(View.GONE);
        } else {
            binding.ResultEmotionUserInput.setVisibility(View.VISIBLE);
        }
        if (binding.ResultReflectionUserInput.getText().toString().equals("")) {
            binding.ResultReflectionUserInput.setVisibility(View.GONE);
            binding.ResultReflectionTitle.setVisibility(View.GONE);
        } else {
            binding.ResultReflectionUserInput.setVisibility(View.VISIBLE);
            binding.ResultReflectionTitle.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainmenubackground).into(binding.background);


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
        type = getSharedPreferences("type", MODE_PRIVATE);
        typeEdit = type.edit();
        emotionColor = getSharedPreferences("emotionColor", MODE_PRIVATE);
        emotionColorEdit = emotionColor.edit();
        arrayList = getSharedPreferences("recordList", MODE_PRIVATE);
        arrayListEdit = arrayList.edit();


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
            typeEdit.clear();
            typeEdit.commit();
            emotionColorEdit.clear();
            emotionColorEdit.commit();
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
        });

        // 수정 버튼 클릭 시
        binding.ResultEditButton.setOnClickListener(view -> {
            Gson gson = new Gson();
            String json = arrayList.getString("arrayList", "");
            Type type = new TypeToken<ArrayList<Record>>() {
            }.getType();
            recordList = gson.fromJson(json, type);

            int index = recordList.size() - 1;
            Intent intento = new Intent(getApplicationContext(), Diary_Result_Edit.class);
            intento.putExtra("date", recordList.get(index).date);
            intento.putExtra("type", recordList.get(index).type);
            intento.putExtra("situation", recordList.get(index).situation);
            intento.putExtra("thought", recordList.get(index).thought);
            intento.putExtra("emotion", recordList.get(index).emotionWord);
            intento.putExtra("emotionText", recordList.get(index).emotionText);
            intento.putExtra("reflection", recordList.get(index).reflection);
            intento.putExtra("index", index);
            startActivity(intento);
        });

        Gson gson = new Gson();
        String json = arrayList.getString("arrayList", "");
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        recordList = gson.fromJson(json, type);

        int index = recordList.size() - 1;

        // 타입 뿌리기
        binding.type.setText(recordList.get(index).type);

        // 오늘 날짜 뿌리기
        // 날짜 세팅
        Date date = recordList.get(index).date;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd E요일");
        String getTime = mFormat.format(date);
        binding.date.setText(getTime);

        // 상황 텍스트 뿌리기
        binding.ResultSituationUserInput.setText(recordList.get(index).situation);
        // 생각 텍스트 뿌리기
        binding.ResultThoughtUserInput.setText(recordList.get(index).thought);
        // 감정 뿌리기
        binding.resultEmotionText.setText(recordList.get(index).emotionWord);
        // 감정 텍스트 뿌리기
        binding.ResultEmotionUserInput.setText(recordList.get(index).emotionText);
        // 회고 텍스트 뿌리기
        binding.ResultReflectionUserInput.setText(recordList.get(index).reflection);

        // 만약 감정 텍스트나 회고 텍스트가 비어 있다면, 나타나지 않게 하기.
        if (binding.ResultEmotionUserInput.getText().toString().equals("")) {
            binding.ResultEmotionUserInput.setVisibility(View.GONE);
        }
        if (binding.ResultReflectionUserInput.getText().toString().equals("")) {
            binding.ResultReflectionUserInput.setVisibility(View.GONE);
            binding.ResultReflectionTitle.setVisibility(View.GONE);
        }
    }

    // 뒤로 가기 버튼 누를 시, 메인 페이지로 돌아가기 (모든 기록 삭제)
    @Override
    public void onBackPressed() {
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
        typeEdit.clear();
        typeEdit.commit();
        emotionColorEdit.clear();
        emotionColorEdit.commit();
        Intent intent = new Intent(getApplicationContext(), MainPage.class);
        startActivity(intent);
    }



    @Override
    protected void onStop() {
        super.onStop();
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
        typeEdit.clear();
        typeEdit.commit();
        emotionColorEdit.clear();
        emotionColorEdit.commit();
    }
}