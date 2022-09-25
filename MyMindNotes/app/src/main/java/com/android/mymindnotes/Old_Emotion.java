package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityOldEmotionBinding;

public class Old_Emotion extends AppCompatActivity {
    ActivityOldEmotionBinding binding;
    SharedPreferences emotion;
    SharedPreferences.Editor emotionEdit;
    int chosenEmotionId = 0;
    RadioGroup emotionGroup1;
    RadioGroup emotionGroup2;
    boolean isChecking = true;
    SharedPreferences emotionText;
    SharedPreferences.Editor emotionTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldEmotionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 어떤 감정인지 모르겠어요, 도와주세요 버튼 클릭 -> 감정 설명서 페이지로 이동
        binding.RecordEmotionHelpButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        });

        emotion = getSharedPreferences("emotion", Activity.MODE_PRIVATE);
        emotionEdit = emotion.edit();
        emotionText = getSharedPreferences("emotionText", Activity.MODE_PRIVATE);
        emotionTextEdit = emotionText.edit();

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

            // 감정Text 저장
            emotionTextEdit.putString("emotionText", binding.RecordEmotionUserInput.getText().toString());
            emotionTextEdit.commit();

            Intent intent = new Intent(getApplicationContext(), Old_Reflection.class);
            startActivity(intent);
        }});

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
                }
                isChecking = true;
            }
        });

        emotionGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 & isChecking) {
                isChecking = false;
                emotionGroup1.clearCheck();
                chosenEmotionId = checkedId;
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
            }
            isChecking = true;
        });

        // 만약 감정이 저장된 상태라면, 화면으로 다시 돌아왔을 때 체크 표시가 돼 있게 뿌리기
        String emo = emotion.getString("emotion", "");

        switch (emo) {
            case "기쁨":
                binding.happinessButton.setChecked(true);
                break;
            case "기대":
                binding.anticipationButton.setChecked(true);
                break;
            case "신뢰":
                binding.trustButton.setChecked(true);
                break;
            case "놀람":
                binding.surpriseButton.setChecked(true);
                break;
            case "슬픔":
                binding.sadnessButton.setChecked(true);
                break;
            case "혐오":
                binding.disgustButton.setChecked(true);
                break;
            case "공포":
                binding.fearButton.setChecked(true);
                break;
            case "분노":
                binding.angerButton.setChecked(true);
                break;
        }

        // 만약 감정 text가 저장된 상태라면, 화면으로 다시 돌아왔을 때 그대로 뿌리기
        String emoText = emotionText.getString("emotionText", "");
        if (!emoText.equals("")) {
            binding.RecordEmotionUserInput.setText(emoText);
        }

        // 뒤로가기 버튼 누르면 저장 후 전에 화면으로 이동
        binding.RecordPreviousButton.setOnClickListener(view -> {
            // 감정Text 저장
            emotionTextEdit.putString("emotionText", binding.RecordEmotionUserInput.getText().toString());
            emotionTextEdit.commit();
            finish();
        });
    }

    // backpressed 누르면 감정 Text 저장
    @Override
    public void onBackPressed() {
        // 감정Text 저장
        emotionTextEdit.putString("emotionText", binding.RecordEmotionUserInput.getText().toString());
        emotionTextEdit.commit();
        finish();
    }
}