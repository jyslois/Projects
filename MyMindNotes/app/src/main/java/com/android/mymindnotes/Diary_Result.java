package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.mymindnotes.databinding.ActivityDiaryResultBinding;
import com.android.mymindnotes.databinding.ActivityRecordResultBinding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList = getSharedPreferences("recordList", MODE_PRIVATE);
        arrayListEdit = arrayList.edit();

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground).into(binding.background);

        // 데이터 세팅을 위해 가져오기
        Intent intent = getIntent();

        // 목록으로 돌아가기 버튼 클릭 시 전 페이지로
        binding.backtoListButton.setOnClickListener(view -> {
            finish();
        });


        // 수정 버튼 클릭 시 (미구현)
        binding.editButton.setOnClickListener(view -> {

        });

        // 삭제 버튼 클릭 시 (미구현
        binding.deleteButton.setOnClickListener(view -> {

            Gson gson = new Gson();
            String json = arrayList.getString("arrayList", "");
            Type type = new TypeToken<ArrayList<Record>>() {
            }.getType();
            recordList = gson.fromJson(json, type);

            int index = intent.getIntExtra("index", 0);
            recordList.remove(index);

            // 업데이트 된 recordList를 SharedPreferences에 저장
            json = gson.toJson(recordList);
            arrayListEdit.putString("arrayList", json);
            arrayListEdit.commit();

            finish();

//            Intent tointent = new Intent(getApplicationContext(), Diary.class);
//            startActivity(tointent);
        });


        // 데이터 세팅
        // 타입 뿌리기
        binding.type.setText(intent.getStringExtra("type"));
        // 오늘 날짜 뿌리기
        binding.date.setText(intent.getStringExtra("date"));

        // 상황 텍스트 뿌리기
        binding.ResultSituationUserInput.setText(intent.getStringExtra("situation"));
        // 생각 텍스트 뿌리기
        binding.ResultThoughtUserInput.setText(intent.getStringExtra("thought"));
        // 감정 뿌리기
        binding.resultEmotionText.setText(intent.getStringExtra("emotion"));
        // 감정 텍스트 뿌리기
        binding.ResultEmotionUserInput.setText(intent.getStringExtra("emotionText"));
        // 회고 텍스트 뿌리기
        binding.ResultReflectionUserInput.setText(intent.getStringExtra("reflection"));

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