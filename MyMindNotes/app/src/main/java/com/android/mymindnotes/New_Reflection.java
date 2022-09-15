package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityNewReflectionBinding;

public class New_Reflection extends AppCompatActivity {
    ActivityNewReflectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewReflectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Tips
        // Tips 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("회고 작성 Tips");
        builder.setMessage(R.string.reflectionTips);
        builder.setPositiveButton("확인", null);
        // Tips 이미지 클릭 시 다이얼로그 띄우기
        binding.RecordReflectionTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        // 이전 버튼 클릭시 이전 화면으로
        binding.RecordPreviousButton.setOnClickListener(view -> {
            finish();
        });

        // 저장 버튼 클릭
        binding.RecordSaveButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Record_Result.class);
            startActivity(intent);
        });

    }
}