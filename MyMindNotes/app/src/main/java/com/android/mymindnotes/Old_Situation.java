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
import android.widget.TextView;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityOldSituationBinding;
import com.bumptech.glide.Glide;

public class Old_Situation extends AppCompatActivity {
    ActivityOldSituationBinding binding;
    AlertDialog alertDialog;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldSituationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.newsituationbackground);

        // Tips
        // Tips 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("상황 작성 Tips");
        builder.setMessage(R.string.situationTips);
        builder.setPositiveButton("확인", null);
        // Tips 이미지 클릭 시 다이얼로그 띄우기
        binding.RecordSituationTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.show();
            TextView messageText = alertDialog.findViewById(android.R.id.message);
            // 메시지 크기 조절
            messageText.setTextSize((float) (standardSize_X / 24));
            // 버튼 크기 조절
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 24));
            alertDialog.show();
        });

        // 다음 버튼 클릭
        binding.RecordNextButton.setOnClickListener(view -> {
            if (binding.RecordSituationUserInput.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "상황을 작성해 주세요", Toast.LENGTH_SHORT).show();
            } else {
                // 상황 저장
                situationEdit.putString("situation", binding.RecordSituationUserInput.getText().toString());
                situationEdit.commit();
                Intent intent = new Intent(getApplicationContext(), Old_Thought.class);
                startActivity(intent);
            }
        });

        // 글짜 크기 조절
        getStandardSize();
        binding.title.setTextSize((float) (standardSize_X / 21));
        binding.RecordSituationTips.setTextSize((float) (standardSize_X / 25));
        binding.RecordSituationUserInput.setTextSize((float) (standardSize_X / 23));
        binding.RecordNextButton.setTextSize((float) (standardSize_X / 23));

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

        // 만약 상황이 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
        String sit = situation.getString("situation", "");
        if (!sit.equals("")) {
            binding.RecordSituationUserInput.setText(sit);
        }

    }

    // 뒤로 가기 버튼 누를 시, 알람창 띄우기
    DialogInterface.OnClickListener dialogListener = (dialog, which) -> {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            // 저장한 것 삭제
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("입력한 기록이 사라져요. 정말 종료하시겠어요?");
        builder.setNegativeButton("종료", dialogListener);
        builder.setPositiveButton("계속 작성", null);
        alertDialog = builder.show();
        // 메시지 크기 조절
        TextView messageText = alertDialog.findViewById(android.R.id.message);
        messageText.setTextSize((float) (standardSize_X / 24));
        // 버튼 크기 조절
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 25));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize((float) (standardSize_X / 25));
        alertDialog.show();
    }
}