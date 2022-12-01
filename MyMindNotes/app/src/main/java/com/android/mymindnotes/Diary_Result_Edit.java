package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityDiaryResultEditBinding;
import com.android.mymindnotes.model.DiaryEdit;
import com.android.mymindnotes.model.UserDiary;
import com.android.mymindnotes.retrofit.RecordDiaryApi;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.android.mymindnotes.retrofit.UpdateDiaryApi;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Diary_Result_Edit extends AppCompatActivity {
    ActivityDiaryResultEditBinding binding;
    String type;
    String situation;
    String thought;
    String emotion;
    String emotionText;
    String reflection;
    String date;
    int diaryNumber;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryResultEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // emotion instruction button
        binding.emotioInstructionButton.setOnClickListener(view -> {
            Intent emotion = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(emotion);
        });

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground2).into(binding.background);


        // 데이터 세팅
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        situation = intent.getStringExtra("situation");
        thought = intent.getStringExtra("thought");
        emotion = intent.getStringExtra("emotion");
        emotionText = intent.getStringExtra("emotionText");
        reflection = intent.getStringExtra("reflection");
        diaryNumber = intent.getIntExtra("diaryNumber", 0);
        date = intent.getStringExtra("date");

        // 화면에 뿌리기
        binding.date.setText(date);
        binding.type.setText(type);
        binding.editSituation.setText(situation);
        binding.editThought.setText(thought);
        binding.editEmotion.setText(emotion);
        binding.editEmotionText.setText(emotionText);
        binding.editReflection.setText(reflection);

        // 타입에 따라 회고 입력란 힌트 다르게 설정
        if (type.equals("오늘의 마음 일기")) {
            binding.editReflection.setHint("(선택) 나는 왜 그런 마음이 들었을까요?");
        } else if (type.equals("트라우마 일기")) {
            binding.editReflection.setHint("지금의 내게 어떤 영향을 미치고 있나요?");
        }

        // 수정 (완료) 버튼
        binding.editButton.setOnClickListener(view -> {
            situation = binding.editSituation.getText().toString();
            thought = binding.editThought.getText().toString();
            emotion = binding.editEmotion.getText().toString();
            emotionText = binding.editEmotionText.getText().toString();
            reflection = binding.editReflection.getText().toString();

            // 일기 수정 네트워크 통신
            if (type.equals("트라우마 일기")) {
                if (reflection.equals("")) {
                    Toast.makeText(this, "회고를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    editdiary();
                }
            } else {
                editdiary();
            }

        });
    }

    // 네트워크 통신: 일기 수정
    public void editdiary() {
            // Retrofit 객체 생성
            RetrofitService retrofitService = new RetrofitService();
            // Retrofit 객체에 인터페이스(Api) 등록, Call 객체 반환하는 Service 객체 생성
            UpdateDiaryApi updateDiaryApi = retrofitService.getRetrofit().create(UpdateDiaryApi.class);
            // Call 객체 획득
            DiaryEdit diaryEdit = new DiaryEdit(situation, thought, emotion, emotionText, reflection);
            Call<Map<String, Object>> call = updateDiaryApi.updateDiary(diaryNumber, diaryEdit);
            // 네트워킹 시도
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 8001) {
                        Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 8000) {
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
    }

    DialogInterface.OnClickListener dialogListener = (dialog, which) -> {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            finish();
        }
    };

    // 뒤로 가기 버튼 누를 시, 알람창 띄우기
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("수정한 기록이 사라져요. 정말 돌아가시겠어요?");
        builder.setNegativeButton("종료", dialogListener);
        builder.setPositiveButton("계속 수정", null);
        alertDialog = builder.create();
        alertDialog.show();
    }
}