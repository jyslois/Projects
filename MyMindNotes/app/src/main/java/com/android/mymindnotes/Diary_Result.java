package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.mymindnotes.databinding.ActivityDiaryResultBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Diary_Result extends AppCompatActivity {
    ActivityDiaryResultBinding binding;
    SharedPreferences arrayList;
    SharedPreferences.Editor arrayListEdit;
    ArrayList<Record> recordList;
    String type;
    String date;
    String situation;
    String thought;
    String emotion;
    String emotionText;
    String reflection;
    int index;

    // 다시 돌아왔을 때 수정된 데이터로 보이기
    @Override
    protected void onResume() {
        super.onResume();

        Gson gson = new Gson();
        String json = arrayList.getString("arrayList", "");
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        recordList = gson.fromJson(json, type);

        // 데이터 세팅
        Intent intent = getIntent();
        index = intent.getIntExtra("index", index);
        situation = recordList.get(index).situation;
        thought = recordList.get(index).thought;
        emotion = recordList.get(index).emotionWord;
        emotionText = recordList.get(index).emotionText;
        reflection = recordList.get(index).reflection;
//        situation = intent.getStringExtra("situation");
//        thought = intent.getStringExtra("thought");
//        emotion = intent.getStringExtra("emotion");
//        emotionText = intent.getStringExtra("emotionText");
//        reflection = intent.getStringExtra("reflection");


        // 상황 텍스트 뿌리기
        binding.ResultSituationUserInput.setText(situation);
        // 생각 텍스트 뿌리기
        binding.ResultThoughtUserInput.setText(thought);
        // 감정 뿌리기
        binding.resultEmotionText.setText(emotion);
        // 감정 텍스트 뿌리기
        binding.ResultEmotionUserInput.setText(emotionText);
        // 회고 텍스트 뿌리기
        binding.ResultReflectionUserInput.setText(reflection);

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
        binding = ActivityDiaryResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList = getSharedPreferences("recordList", MODE_PRIVATE);
        arrayListEdit = arrayList.edit();

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground).into(binding.background);

        // 목록으로 돌아가기 버튼 클릭 시 전 페이지로
        binding.backtoListButton.setOnClickListener(view -> {
            finish();
        });


        // 수정 버튼 클릭 시
        binding.editButton.setOnClickListener(view -> {
            Intent intento = new Intent(getApplicationContext(), Diary_Result_Edit.class);
            intento.putExtra("date", date);
            intento.putExtra("type", type);
            intento.putExtra("situation", situation);
            intento.putExtra("thought", thought);
            intento.putExtra("emotion", emotion);
            intento.putExtra("emotionText", emotionText);
            intento.putExtra("reflection", reflection);
            // 데이터 세팅을 위해 가져오기
            Intent intent = getIntent();
            index = intent.getIntExtra("index", index);
            intento.putExtra("index", index);
            startActivity(intento);
        });

        // 삭제 버튼 클릭 시
        binding.deleteButton.setOnClickListener(view -> {

            Gson gson = new Gson();
            String json = arrayList.getString("arrayList", "");
            Type type = new TypeToken<ArrayList<Record>>() {
            }.getType();
            recordList = gson.fromJson(json, type);

            recordList.remove(index);

            // 업데이트 된 recordList를 SharedPreferences에 저장
            json = gson.toJson(recordList);
            arrayListEdit.putString("arrayList", json);
            arrayListEdit.commit();

            finish();
        });

        // 데이터 세팅을 위해 가져오기
        Intent intent = getIntent();
        index = intent.getIntExtra("index", index);
        // 데이터 세팅
        type = intent.getStringExtra("type");
        date = intent.getStringExtra("date");
        situation = intent.getStringExtra("situation");
        thought = intent.getStringExtra("thought");
        emotion = intent.getStringExtra("emotion");
        emotionText = intent.getStringExtra("emotionText");
        reflection = intent.getStringExtra("reflection");

        // 타입 뿌리기
        binding.type.setText(type);
        // 오늘 날짜 뿌리기
        binding.date.setText(date);

        // 상황 텍스트 뿌리기
        binding.ResultSituationUserInput.setText(situation);
        // 생각 텍스트 뿌리기
        binding.ResultThoughtUserInput.setText(thought);
        // 감정 뿌리기
        binding.resultEmotionText.setText(emotion);
        // 감정 텍스트 뿌리기
        binding.ResultEmotionUserInput.setText(emotionText);
        // 회고 텍스트 뿌리기
        binding.ResultReflectionUserInput.setText(reflection);

        // 만약 감정 텍스트나 회고 텍스트가 비어 있다면, 나타나지 않게 하기.
        if (binding.ResultEmotionUserInput.getText().toString().equals("")) {
            binding.ResultEmotionUserInput.setVisibility(View.GONE);
        }
        if (binding.ResultReflectionUserInput.getText().toString().equals("")) {
            binding.ResultReflectionUserInput.setVisibility(View.GONE);
            binding.ResultReflectionTitle.setVisibility(View.GONE);
        }

    }
}