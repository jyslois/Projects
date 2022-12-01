package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityDiaryResultBinding;
import com.android.mymindnotes.model.UserDiary;
import com.android.mymindnotes.retrofit.DeleteDiaryApi;
import com.android.mymindnotes.retrofit.GetDiaryApi;
import com.android.mymindnotes.retrofit.GetDiaryListApi;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Diary_Result extends AppCompatActivity {
    ActivityDiaryResultBinding binding;
    SharedPreferences arrayList;
    SharedPreferences.Editor arrayListEdit;
    String type;
    String date;
    String situation;
    String thought;
    String emotion;
    String emotionText;
    String reflection;
    int index;
    int diaryNumber;
    SharedPreferences userindex;
    ArrayList<UserDiary> diarylist;

    // 다시 돌아왔을 때 수정된 데이터로 보이기
    @Override
    protected void onResume() {
        super.onResume();

        refreshDiary();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList = getSharedPreferences("recordList", MODE_PRIVATE);
        arrayListEdit = arrayList.edit();
        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground2).into(binding.background);

        // 목록으로 돌아가기 버튼 클릭 시 전 페이지로
        binding.backtoListButton.setOnClickListener(view -> {
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
        index = intent.getIntExtra("index", index);
        diaryNumber = intent.getIntExtra("diaryNumber", 0);

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
            intento.putExtra("diaryNumber", diaryNumber);
            intento.putExtra("index", index);
            startActivity(intento);
        });

        // 삭제 버튼 클릭 시
        binding.deleteButton.setOnClickListener(view -> {
            deleteDiary();
        });



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

    public void deleteDiary() {
        Thread thread = new Thread(() -> {
            RetrofitService retrofitService = new RetrofitService();
            DeleteDiaryApi deleteDiaryApi = retrofitService.getRetrofit().create(DeleteDiaryApi.class);
            Call<Map<String, Object>> call = deleteDiaryApi.deleteDiary(diaryNumber);
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 9000) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        });
        thread.start();
    }




    // 업데이트 (네트워크 통신)
    public void refreshDiary() {
        Thread thread = new Thread(() -> {
            RetrofitService retrofitService = new RetrofitService();
            GetDiaryListApi getDiaryListApi = retrofitService.getRetrofit().create(GetDiaryListApi.class);
            Call<Map<String, Object>> call = getDiaryListApi.getAllDiary(userindex.getInt("userindex", 0));
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 7000) {

                        // 서버로부터 리스트 받아와서 저장하기
                        // https://ppizil.tistory.com/4
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<UserDiary>>() {
                        }.getType();
                        String jsonResult = gson.toJson(response.body().get("diaryList"));
                        diarylist = gson.fromJson(jsonResult, type);
                        // 데이터 세팅
                        situation = diarylist.get(index).getSituation();
                        thought = diarylist.get(index).getThought();
                        emotion = diarylist.get(index).getEmotion();
                        emotionText = diarylist.get(index).getEmotionDescription();
                        reflection = diarylist.get(index).getReflection();


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
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        });
        thread.start();
    }

}