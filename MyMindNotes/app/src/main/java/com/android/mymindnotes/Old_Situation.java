package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.android.mymindnotes.databinding.ActivityOldSituationBinding;

public class Old_Situation extends AppCompatActivity {

    ActivityOldSituationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldSituationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Tips
        // Tips 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("상황 작성 Tips");
        builder.setMessage(R.string.situationTips);
        builder.setPositiveButton("확인", null);
        // Tips 이미지 클릭 시 다이얼로그 띄우기
        binding.RecordSituationTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });


        // 다음 버튼 클릭
        binding.RecordNextButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Old_Thought.class);
            startActivity(intent);
        });
    }
}