package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    SharedPreferences emotionColor;
    SharedPreferences.Editor emotionColorEdit;

    // 알림 dialoguee
    void dialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("확인", null);
        alertDialog = builder.show();
        alertDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewEmotionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.recordbackground);

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
        emotionColor = getSharedPreferences("emotionColor", MODE_PRIVATE);
        emotionColorEdit = emotionColor.edit();

        // 다음 버튼 클릭, emotion 저장
        binding.RecordNextButton.setOnClickListener(view -> {
            // emotion 저장
            if (chosenEmotionId == 0) {
                dialog("감정을 선택해 주세요.");
            } else {
                switch (chosenEmotionId) {
                    case R.id.happinessButton:
                        emotionColorEdit.putInt("emotionColor", R.drawable.orange_happiness);
                        emotionColorEdit.commit();
                        emotionEdit.putString("emotion", "기쁨");
                        emotionEdit.commit();
                        break;
                    case R.id.anticipationButton:
                        emotionColorEdit.putInt("emotionColor", R.drawable.green_anticipation);
                        emotionColorEdit.commit();
                        emotionEdit.putString("emotion", "기대");
                        emotionEdit.commit();
                        break;
                    case R.id.trustButton:
                        emotionColorEdit.putInt("emotionColor", R.drawable.darkblue_trust);
                        emotionColorEdit.commit();
                        emotionEdit.putString("emotion", "신뢰");
                        emotionEdit.commit();
                        break;
                    case R.id.surpriseButton:
                        emotionColorEdit.putInt("emotionColor", R.drawable.yellow_surprise);
                        emotionColorEdit.commit();
                        emotionEdit.putString("emotion", "놀람");
                        emotionEdit.commit();
                        break;
                    case R.id.sadnessButton:
                        emotionColorEdit.putInt("emotionColor", R.drawable.grey_sadness);
                        emotionColorEdit.commit();
                        emotionEdit.putString("emotion", "슬픔");
                        emotionEdit.commit();
                        break;
                    case R.id.disgustButton:
                        emotionColorEdit.putInt("emotionColor", R.drawable.brown_disgust);
                        emotionColorEdit.commit();
                        emotionEdit.putString("emotion", "혐오");
                        emotionEdit.commit();
                        break;
                    case R.id.fearButton:
                        emotionColorEdit.putInt("emotionColor", R.drawable.black_fear);
                        emotionColorEdit.commit();
                        emotionEdit.putString("emotion", "공포");
                        emotionEdit.commit();
                        break;
                    case R.id.angerButton:
                        emotionColorEdit.putInt("emotionColor", R.drawable.red_anger);
                        emotionColorEdit.commit();
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
            AlertDialog alertDialog = builder.show();
            alertDialog.show();
        });

        // 감정 선택
        emotionGroup1 = binding.emotions1;
        emotionGroup2 = binding.emotions2;

        // 감정 선택 시 radiogroup 별로 선택 해제되기 && 선택시 이벤트
        emotionGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 & isChecking) {
                isChecking = false;
                emotionGroup2.clearCheck();
                chosenEmotionId = checkedId;
            }
            isChecking = true;
        });

        emotionGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 & isChecking) {
                isChecking = false;
                emotionGroup1.clearCheck();
                chosenEmotionId = checkedId;
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

    }


    DialogInterface.OnClickListener dialogListener = (dialog, which) -> {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            // 기록 삭제
            emotionColorEdit.clear();
            emotionColorEdit.commit();
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
            finish();
        }
    };

    // 뒤로 가기 버튼 누를 시, 알람창 띄우기
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("입력한 기록이 사라져요. 정말 종료하시겠어요?");
        builder.setNegativeButton("종료", dialogListener);
        builder.setPositiveButton("계속 작성", null);
        AlertDialog alertDialog = builder.show();
        alertDialog.show();
    }


}
