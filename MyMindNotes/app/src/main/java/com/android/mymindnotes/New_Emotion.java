package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityNewEmotionBinding;

public class New_Emotion extends AppCompatActivity {
    ActivityNewEmotionBinding binding;
    RadioGroup emotionGroup1;
    RadioGroup emotionGroup2;
    boolean isChecking = true;
    int chosenEmotionId = 0;
    SharedPreferences emotion;
    SharedPreferences.Editor emotionEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewEmotionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emotion = getSharedPreferences("emotion", Activity.MODE_PRIVATE);
        emotionEdit = emotion.edit();


        // 어떤 감정인지 모르겠어요, 도와주세요 버튼 클릭 -> 감정 설명서 페이지로 이동
        binding.RecordEmotionHelpButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        });

        // 다음 버튼 클릭
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

                    switch (checkedId) {
                        case R.id.happinessButton:
                            Toast.makeText(getApplicationContext(), "기쁨 선택", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.anticipationButton:
                            Toast.makeText(getApplicationContext(), "기대 선택", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.trustButton:
                            Toast.makeText(getApplicationContext(), "신뢰 선택", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.surpriseButton:
                            Toast.makeText(getApplicationContext(), "놀람 선택", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                isChecking = true;
            }
        });

        emotionGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 & isChecking) {
                    isChecking = false;
                    emotionGroup1.clearCheck();
                    chosenEmotionId = checkedId;

                    switch(checkedId) {
                        case R.id.sadnessButton:
                            Toast.makeText(getApplicationContext(), "슬픔 선택", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.disgustButton:
                            Toast.makeText(getApplicationContext(), "혐오 선택", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.fearButton:
                            Toast.makeText(getApplicationContext(), "공포 선택", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.angerButton:
                            Toast.makeText(getApplicationContext(), "분노 선택", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                isChecking = true;
            }
        });

    }


}