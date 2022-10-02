package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityOldThoughtBinding;
import com.bumptech.glide.Glide;

public class Old_Thought extends AppCompatActivity {
    ActivityOldThoughtBinding binding;
    SharedPreferences thought;
    SharedPreferences.Editor thoughtEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldThoughtBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.recordbackground).into(binding.traumabackground);

        thought = getSharedPreferences("thought", MODE_PRIVATE);
        thoughtEdit = thought.edit();

        // Tips
        // Tips 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("생각 작성 Tips");
        builder.setMessage(R.string.traumaThoughtTips);
        builder.setPositiveButton("확인", null);

        // Tips 이미지 클릭 시 다이얼로그 띄우기
        binding.RecordThoughtTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

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
                Toast.makeText(getApplicationContext(), "생각을 작성해 주세요", Toast.LENGTH_SHORT).show();
            } else {
                // 생각 저장
                thoughtEdit.putString("thought", binding.RecordThoughtUserInput.getText().toString());
                thoughtEdit.commit();
                Intent intent = new Intent(getApplicationContext(), Old_Emotion.class);
                startActivity(intent);
            }
        });

        // 만약 생각이 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
        String thou = thought.getString("thought", "");
        if (!thou.equals("")) {
            binding.RecordThoughtUserInput.setText(thou);
        }
    }

    // backprssed 시 생각 저장 후 뒤로가기
    @Override
    public void onBackPressed() {
        // 생각 저장
        thoughtEdit.putString("thought", binding.RecordThoughtUserInput.getText().toString());
        thoughtEdit.commit();
        finish();
    }
}