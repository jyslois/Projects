package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityRecordResultBinding;
import com.android.mymindnotes.model.UserDiary;
import com.android.mymindnotes.model.retrofit.GetDiaryListApi;
import com.android.mymindnotes.data.retrofit.RetrofitService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    ArrayList<UserDiary> diarylist;
    SharedPreferences userindex;

    int index;
    int diaryNumber;

    // 화면 크기에 따른 글자 크기 조절
    int standardSize_X, standardSize_Y;
    float density;

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }
    public void getStandardSize() {
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }


    @Override
    protected void onResume() {
        super.onResume();

        getDiary();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground4).into(binding.background);

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

        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);


        // 화면 세팅
        getDiary();

        // 글짜 크기 조절
        getStandardSize();
        binding.type.setTextSize((float) (standardSize_X / 25));
        binding.date.setTextSize((float) (standardSize_X / 25));
        binding.ResultSituationTitle.setTextSize((float) (standardSize_X / 19));
        binding.ResultThoughtTitle.setTextSize((float) (standardSize_X / 19));
        binding.ResultEmotionTitle.setTextSize((float) (standardSize_X / 19));
        binding.ResultReflectionTitle.setTextSize((float) (standardSize_X / 19));
        binding.ResultSituationUserInput.setTextSize((float) (standardSize_X / 22));
        binding.ResultThoughtUserInput.setTextSize((float) (standardSize_X / 22));
        binding.ResultEmotionUserInput.setTextSize((float) (standardSize_X / 22));
        binding.ResultEmotionText.setTextSize((float) (standardSize_X / 22));
        binding.ResultReflectionUserInput.setTextSize((float) (standardSize_X / 22));
        binding.ResultEditButton.setTextSize((float) (standardSize_X / 23));
        binding.ResultEndButton.setTextSize((float) (standardSize_X / 23));

        // 수정 버튼 클릭 시
        binding.ResultEditButton.setOnClickListener(view -> {
            Intent intento = new Intent(getApplicationContext(), Diary_Result_Edit.class);
            intento.putExtra("date", diarylist.get(index).getDate() + " " + diarylist.get(index).getDay());
            intento.putExtra("type", diarylist.get(index).getType());
            intento.putExtra("situation", diarylist.get(index).getSituation());
            intento.putExtra("thought", diarylist.get(index).getThought());
            intento.putExtra("emotion", diarylist.get(index).getEmotion());
            intento.putExtra("emotionText", diarylist.get(index).getEmotionDescription());
            intento.putExtra("reflection", diarylist.get(index).getReflection());
            intento.putExtra("diaryNumber", diaryNumber);
            startActivity(intento);
        });

        // 메인으로 돌아가기 버튼 클릭 시 mainpage로 (모든 기록 삭제)
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


    }

    // 네트워크 통신: 일기 목록 얻어서 일기 가져오기
    public void getDiary() {
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

                        index = diarylist.size() - 1;

                        // 다이어리 번호 저장
                        diaryNumber = diarylist.get(index).getDiary_number();

                        // 타입 뿌리기
                        binding.type.setText(diarylist.get(index).getType());

                        // 날짜 뿌리기
                        binding.date.setText(diarylist.get(index).getDate() + " " + diarylist.get(index).getDay());

                        // 상황 텍스트 뿌리기
                        binding.ResultSituationUserInput.setText(diarylist.get(index).getSituation());
                        // 생각 텍스트 뿌리기
                        binding.ResultThoughtUserInput.setText(diarylist.get(index).getThought());
                        // 감정 뿌리기
                        binding.ResultEmotionText.setText(diarylist.get(index).getEmotion());
                        // 감정 텍스트 뿌리기
                        binding.ResultEmotionUserInput.setText(diarylist.get(index).getEmotionDescription());
                        // 회고 텍스트 뿌리기
                        binding.ResultReflectionUserInput.setText(diarylist.get(index).getReflection());

                        // 만약 감정 텍스트나 회고 텍스트가 비어 있다면, 나타나지 않게 하기.
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