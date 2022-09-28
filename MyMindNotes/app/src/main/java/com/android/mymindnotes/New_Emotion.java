package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityNewEmotionBinding;
import com.bumptech.glide.Glide;

public class New_Emotion extends AppCompatActivity {
    ActivityNewEmotionBinding binding;
    RadioGroup emotionGroup1;
    RadioGroup emotionGroup2;
    boolean isChecking = true;
    int chosenEmotionId = 0;
    SharedPreferences emotion;
    SharedPreferences.Editor emotionEdit;
    SharedPreferences emotionText;
    SharedPreferences.Editor emotionTextEdit;
    AlertDialog alertDialog;
    SharedPreferences situation;
    SharedPreferences.Editor situationEdit;
    SharedPreferences thought;
    SharedPreferences.Editor thoughtEdit;
    SharedPreferences reflection;
    SharedPreferences.Editor reflectionEdit;
    SharedPreferences type;
    SharedPreferences.Editor typeEdit;
    SharedPreferences date;
    SharedPreferences.Editor dateEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewEmotionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.recordbackground).into(binding.newemotionbackground);

        // 어떤 감정인지 모르겠어요, 도와주세요 버튼 클릭 -> 감정 설명서 페이지로 이동
        binding.RecordEmotionHelpButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        });

        emotion = getSharedPreferences("emotion", Activity.MODE_PRIVATE);
        emotionEdit = emotion.edit();
        emotionText = getSharedPreferences("emotionText", Activity.MODE_PRIVATE);
        emotionTextEdit = emotionText.edit();
        situation = getSharedPreferences("situation", Activity.MODE_PRIVATE);
        situationEdit = situation.edit();
        thought = getSharedPreferences("thought", MODE_PRIVATE);
        thoughtEdit = thought.edit();
        reflection = getSharedPreferences("reflection", MODE_PRIVATE);
        reflectionEdit = reflection.edit();
        type = getSharedPreferences("type", MODE_PRIVATE);
        typeEdit = type.edit();
        date = getSharedPreferences("date", MODE_PRIVATE);
        dateEdit = date.edit();


        // 다음 버튼 클릭, emotion 저장
        binding.RecordNextButton.setOnClickListener(view -> {
            // emotion 저장
            if (chosenEmotionId == 0) {
                Toast.makeText(getApplicationContext(), "감정을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            } else {
                switch (chosenEmotionId) {
                    case R.id.happinessButton:
                        emotionEdit.putString("emotion", "기쁨");
                        emotionEdit.commit();
                        break;
                    case R.id.anticipationButton:
                        emotionEdit.putString("emotion", "기대");
                        emotionEdit.commit();
                        break;
                    case R.id.trustButton:
                        emotionEdit.putString("emotion", "신뢰");
                        emotionEdit.commit();
                        break;
                    case R.id.surpriseButton:
                        emotionEdit.putString("emotion", "놀람");
                        emotionEdit.commit();
                        break;
                    case R.id.sadnessButton:
                        emotionEdit.putString("emotion", "슬픔");
                        emotionEdit.commit();
                        break;
                    case R.id.disgustButton:
                        emotionEdit.putString("emotion", "혐오");
                        emotionEdit.commit();
                        break;
                    case R.id.fearButton:
                        emotionEdit.putString("emotion", "공포");
                        emotionEdit.commit();
                        break;
                    case R.id.angerButton:
                        emotionEdit.putString("emotion", "분노");
                        emotionEdit.commit();
                        break;

                }
                // 감정Text 저장
                emotionTextEdit.putString("emotionText", binding.RecordEmotionUserInput.getText().toString());
                emotionTextEdit.commit();
                Intent intent = new Intent(getApplicationContext(), New_Situation.class);
                startActivity(intent);
            }

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

        // 감정 선택
        emotionGroup1 = binding.emotions1;
        emotionGroup2 = binding.emotions2;

        // 감정 선택 시 radiogroup 별로 선택 해제되기 && 선택시 이벤트
        emotionGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 & isChecking) {
                    isChecking = false;
                    emotionGroup2.clearCheck();
                    chosenEmotionId = checkedId;
                }
                isChecking = true;
            }
        });

        emotionGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 & isChecking) {
                isChecking = false;
                emotionGroup1.clearCheck();
                chosenEmotionId = checkedId;
            }
            isChecking = true;
        });


    }


    // 뒤로 가기 버튼 누를 시, 알람창 띄우기
    DialogInterface.OnClickListener dialogListener = (dialog, which) -> {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            // 기록 삭제
            emotionEdit.clear();
            emotionEdit.commit();
            situationEdit.clear();
            situationEdit.commit();
            thoughtEdit.clear();
            thoughtEdit.commit();
            reflectionEdit.clear();
            reflectionEdit.commit();
            typeEdit.clear();
            typeEdit.commit();
            dateEdit.clear();
            dateEdit.commit();
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("입력한 기록이 사라져요. 정말 종료하시겠어요?");
        builder.setPositiveButton("종료", dialogListener);
        builder.setNegativeButton("계속 작성", null);
        alertDialog = builder.create();
        alertDialog.show();
        }

}