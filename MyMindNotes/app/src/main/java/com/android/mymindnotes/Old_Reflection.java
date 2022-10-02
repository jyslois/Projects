package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityOldReflectionBinding;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Old_Reflection extends AppCompatActivity {
    ActivityOldReflectionBinding binding;
    SharedPreferences reflection;
    SharedPreferences.Editor reflectionEdit;
    SharedPreferences type;
    SharedPreferences.Editor typeEdit;
    SharedPreferences date;
    SharedPreferences.Editor dateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldReflectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.recordbackground).into(binding.traumareflectionbackground);

        // Tips
        // Tips 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("회고 작성 Tips");
        builder.setMessage(R.string.traumaReflectionTips);
        builder.setPositiveButton("확인", null);
        // Tips 이미지 클릭 시 다이얼로그 띄우기
        binding.RecordReflectionTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        reflection = getSharedPreferences("reflection", MODE_PRIVATE);
        reflectionEdit = reflection.edit();
        type = getSharedPreferences("type", MODE_PRIVATE);
        typeEdit = type.edit();
        date = getSharedPreferences("date", MODE_PRIVATE);
        dateEdit = date.edit();

        // 이전 버튼 클릭시 이전 화면으로
        binding.RecordPreviousButton.setOnClickListener(view -> {
            // 회고 저장
            reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
            reflectionEdit.commit();
            finish();
        });


        // 저장 버튼 클릭
        binding.RecordSaveButton.setOnClickListener(view -> {
            if (binding.RecordReflectionUserInput.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "회고를 작성해 주세요", Toast.LENGTH_SHORT).show();
            } else {
                // 회고 저장
                reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
                reflectionEdit.commit();
                // 타입 저장
                typeEdit.putString("type", "트라우마 일기");
                typeEdit.commit();
                // 오늘 날짜 저장
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd E요일");
                String getTime = mFormat.format(date);
                dateEdit.putString("date", getTime);
                dateEdit.commit();

                Intent intent = new Intent(getApplicationContext(), Record_Result.class);
                startActivity(intent);
            }
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