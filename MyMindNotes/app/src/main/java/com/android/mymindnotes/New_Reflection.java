package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityNewReflectionBinding;

public class New_Reflection extends AppCompatActivity {
    ActivityNewReflectionBinding binding;
    SharedPreferences reflection;
    SharedPreferences.Editor reflectionEdit;

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

        reflection = getSharedPreferences("reflection", MODE_PRIVATE);
        reflectionEdit = reflection.edit();

        // 이전 버튼 클릭시 이전 화면으로
        binding.RecordPreviousButton.setOnClickListener(view -> {
            // 회고 저장
            reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
            reflectionEdit.commit();
            finish();
        });

        // 저장 버튼 클릭
        binding.RecordSaveButton.setOnClickListener(view -> {
            // 회고 저장
            reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
            reflectionEdit.commit();
            Intent intent = new Intent(getApplicationContext(), Record_Result.class);
            startActivity(intent);
        });

        // 만약 회고가 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
        String refle = reflection.getString("reflection", "");
        if (!refle.equals("")) {
            binding.RecordReflectionUserInput.setText(refle);
        }
    }

    // backprssed 시 회고 저장 후 뒤로가기
    @Override
    public void onBackPressed() {
        // 생각 저장
        reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
        reflectionEdit.commit();
        finish();
    }
}