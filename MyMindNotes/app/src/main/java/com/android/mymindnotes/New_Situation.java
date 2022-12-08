package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityNewSituationBinding;
import com.bumptech.glide.Glide;

public class New_Situation extends AppCompatActivity {
    ActivityNewSituationBinding binding;
    SharedPreferences situation;
    SharedPreferences.Editor situationEdit;

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
        binding = ActivityNewSituationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.newsituationbackground);

        situation = getSharedPreferences("situation", Activity.MODE_PRIVATE);
        situationEdit = situation.edit();

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
            // 메시지 크기 조절
            TextView messageText = alertDialog.findViewById(android.R.id.message);
            messageText.setTextSize((float) (standardSize_X / 24));
            // 버튼 크기 조절
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 25));
            alertDialog.show();
        });

        // 이전 버튼 클릭시 상황 저장 후 이전 화면으로
        binding.RecordPreviousButton.setOnClickListener(view -> {
            situationEdit.putString("situation", binding.RecordSituationUserInput.getText().toString());
            situationEdit.commit();
            finish();
        });

        // 다음 버튼 클릭
        binding.RecordNextButton.setOnClickListener(view -> {
            if (binding.RecordSituationUserInput.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "상황을 작성해 주세요", Toast.LENGTH_SHORT).show();
            } else {
                // 상황 저장
                situationEdit.putString("situation", binding.RecordSituationUserInput.getText().toString());
                situationEdit.commit();
                Intent intent = new Intent(getApplicationContext(), New_Thought.class);
                startActivity(intent);
            }
        });

        // 만약 상황이 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
        String sit = situation.getString("situation", "");
        if (!sit.equals("")) {
            binding.RecordSituationUserInput.setText(sit);
        }

        // 글짜 크기 조절
        getStandardSize();
        binding.title.setTextSize((float) (standardSize_X / 21));
        binding.RecordSituationTips.setTextSize((float) (standardSize_X / 25));
        binding.RecordSituationUserInput.setTextSize((float) (standardSize_X / 23));
        binding.RecordPreviousButton.setTextSize((float) (standardSize_X / 23));
        binding.RecordNextButton.setTextSize((float) (standardSize_X / 23));

    }

    // 백 클릭시 상황 저장 후 이전 화면으로
    @Override
    public void onBackPressed() {
        // 상황 저장
        situationEdit.putString("situation", binding.RecordSituationUserInput.getText().toString());
        situationEdit.commit();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 상황 저장
        situationEdit.putString("situation", binding.RecordSituationUserInput.getText().toString());
        situationEdit.commit();
    }
}