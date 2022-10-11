package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityNewReflectionBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class New_Reflection extends AppCompatActivity {
    ActivityNewReflectionBinding binding;
    SharedPreferences reflection;
    SharedPreferences.Editor reflectionEdit;
    SharedPreferences type;
    SharedPreferences.Editor typeEdit;
    SharedPreferences dates;
    SharedPreferences.Editor datesEdit;

    SharedPreferences emotion;
    SharedPreferences emotionText;
    SharedPreferences situation;
    SharedPreferences thought;
    SharedPreferences emotionColor;

    SharedPreferences arrayList;
    SharedPreferences.Editor arrayListEdit;

    ArrayList<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewReflectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.recordbackground).into(binding.newreflectionbackground);

        // Tips
        // Tips 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("회고 작성 Tips");
        builder.setMessage(R.string.reflectionTips);
        builder.setPositiveButton("확인", null);
        // Tips 이미지 클릭 시 다이얼로그 띄우기
        binding.RecordReflectionTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        reflection = getSharedPreferences("reflection", MODE_PRIVATE);
        reflectionEdit = reflection.edit();
        type = getSharedPreferences("type", MODE_PRIVATE);
        typeEdit = type.edit();
        dates = getSharedPreferences("date", MODE_PRIVATE);
        datesEdit = dates.edit();

        emotion = getSharedPreferences("emotion", MODE_PRIVATE);
        emotionText = getSharedPreferences("emotionText", MODE_PRIVATE);
        emotionColor = getSharedPreferences("emotionColor", MODE_PRIVATE);
        situation = getSharedPreferences("situation", MODE_PRIVATE);
        thought = getSharedPreferences("thought", MODE_PRIVATE);

        arrayList = getSharedPreferences("recordList", MODE_PRIVATE);
        arrayListEdit = arrayList.edit();

        // 이전 버튼 클릭시 이전 화면으로
        binding.RecordPreviousButton.setOnClickListener(view -> {
            // 회고 저장
            reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
            reflectionEdit.commit();
            finish();
        });

        // 저장 버튼 클릭
        binding.RecordSaveButton.setOnClickListener(view -> {
            // 회고 저장
            reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
            reflectionEdit.commit();
            // 타입 저장
            typeEdit.putString("type", "오늘의 마음 일기");
            typeEdit.commit();
            // 오늘 날짜 저장
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd E요일");
            String getTime = mFormat.format(date);
            datesEdit.putString("date", getTime);
            datesEdit.commit();

            // 최종 기록 저장
            // 만약 SharedPreferences에 저장된 arrayList가 있다면,
            if (arrayList.getString("arrayList", "") != "") {
                // SharedPreferences에 저장된 arrayList를 recordList로 가져오기
                Gson gson = new Gson();
                String json = arrayList.getString("arrayList", "");
                Type type = new TypeToken<ArrayList<Record>>() {
                }.getType();
                recordList = gson.fromJson(json, type);
            } else {
                // 없다면 새 recordList 만들기
                recordList = new ArrayList<>();
            }

            // recordList에 데이터 저장(인스턴트 추가)
            recordList.add(new Record(emotionColor.getInt("emotionColor", R.drawable.purple_etc), dates.getString("date", "0000-00-00 0요일"),
                    type.getString("type", "ㅇㅇ의 일기"), emotion.getString("emotion", "감정"), situation.getString("situation", "상황"),
                    thought.getString("thought", "생각"), emotionText.getString("emotionText", ""), reflection.getString("reflection", "")));

            // 업데이트 된 recordList를 SharedPreferences에 저장
            Gson gson = new Gson();
            String json = gson.toJson(recordList);
            arrayListEdit.putString("arrayList", json);
            arrayListEdit.commit();

            Intent intent = new Intent(getApplicationContext(), Record_Result.class);
            startActivity(intent);
        });

        // 만약 회고가 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
        String refle = reflection.getString("reflection", "");
        if (!refle.equals("")) {
            binding.RecordReflectionUserInput.setText(refle);
        }
    }

    // backprssed 시 회고 저장 후 뒤로가기
    @Override
    public void onBackPressed() {
        // 생각 저장
        reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
        reflectionEdit.commit();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
        reflectionEdit.commit();
    }
}