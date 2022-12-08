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

import com.android.mymindnotes.databinding.ActivityNewThoughtBinding;
import com.bumptech.glide.Glide;

public class New_Thought extends AppCompatActivity {
    ActivityNewThoughtBinding binding;
    SharedPreferences thought;
    SharedPreferences.Editor thoughtEdit;
    AlertDialog alertDialog;

    // 알림 dialoguee
    void dialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("확인", null);
        alertDialog = builder.show();
        // 메시지 크기 조절
        TextView messageText = alertDialog.findViewById(android.R.id.message);
        messageText.setTextSize((float) (standardSize_X / 24));
        // 버튼 크기 조절
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 25));
        alertDialog.show();
    }

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
        binding = ActivityNewThoughtBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.recordbackground);

        // Tips
        // Tips 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("생각 작성 Tips");
        builder.setMessage(R.string.thoughtTips);
        builder.setPositiveButton("확인", null);
        // Tips 이미지 클릭 시 다이얼로그 띄우기
        binding.RecordThoughtTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.show();
            // 메시지 크기 조절
            TextView messageText = alertDialog.findViewById(android.R.id.message);
            messageText.setTextSize((float) (standardSize_X / 24));
            // 버튼 크기 조절
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 25));
            alertDialog.show();
        });

        thought = getSharedPreferences("thought", MODE_PRIVATE);
        thoughtEdit = thought.edit();

        // 이전 버튼 클릭시 이전 화면으로
        binding.RecordPreviousButton.setOnClickListener(view -> {
            // 생각 저장
            thoughtEdit.putString("thought", binding.RecordThoughtUserInput.getText().toString());
            thoughtEdit.commit();
            finish();
        });

        // 다음 버튼 클릭
        binding.RecordNextButton.setOnClickListener(view -> {
            if (binding.RecordThoughtUserInput.getText().toString().equals("")) {
                dialog("생각을 작성해 주세요.");
            } else {
                // 생각 저장
                thoughtEdit.putString("thought", binding.RecordThoughtUserInput.getText().toString());
                thoughtEdit.commit();
                Intent intent = new Intent(getApplicationContext(), New_Reflection.class);
                startActivity(intent);
            }
        });

        // 만약 생각이 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
        String thou = thought.getString("thought", "");
        if (!thou.equals("")) {
            binding.RecordThoughtUserInput.setText(thou);
        }

        // 글짜 크기 조절
        getStandardSize();
        binding.title.setTextSize((float) (standardSize_X / 21));
        binding.RecordThoughtTips.setTextSize((float) (standardSize_X / 25));
        binding.RecordThoughtUserInput.setTextSize((float) (standardSize_X / 23));
        binding.RecordPreviousButton.setTextSize((float) (standardSize_X / 23));
        binding.RecordNextButton.setTextSize((float) (standardSize_X / 23));
    }

    // backprssed 시 생각 저장 후 뒤로가기
    @Override
    public void onBackPressed() {
        // 생각 저장
        thoughtEdit.putString("thought", binding.RecordThoughtUserInput.getText().toString());
        thoughtEdit.commit();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 생각 저장
        thoughtEdit.putString("thought", binding.RecordThoughtUserInput.getText().toString());
        thoughtEdit.commit();
    }
}