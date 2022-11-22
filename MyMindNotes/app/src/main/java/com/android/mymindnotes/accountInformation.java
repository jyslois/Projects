package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;

public class accountInformation extends AppCompatActivity {
    com.android.mymindnotes.databinding.ActivityAccountInformationBinding binding;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.android.mymindnotes.databinding.ActivityAccountInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        // 변경 버튼 이벤트 처리
        binding.changePasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), changePassword.class);
            startActivity(intent);
        });

        binding.changeNicknameButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), changeNickname.class);
            startActivity(intent);
        });

        // 로그아웃 버튼 이벤트 처리
        binding.logoutButton.setOnClickListener(view -> {
            // 상태 저장
            autoSaveEdit.putBoolean("autoLoginCheck", false);
            autoSaveEdit.apply();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        // 계정 탈퇴 버튼 이벤트 처리
        binding.withdrawalButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 화면 밖이나 뒤로가기 눌렀을 때 다이얼로그 닫히지 않게 하기
            builder.setCancelable(false);
            builder.setMessage("정말 탈퇴하시겠습니까?");
            builder.setNegativeButton("탈퇴", dialogListener);
            builder.setPositiveButton("취소", null);
            alertDialog = builder.create();
            alertDialog.show();
        });
    }

    DialogInterface.OnClickListener dialogListener = (dialog, which) -> {
        // 탈퇴 눌렀을 때 이벤트 처리(훗날 네트워크 코드 처리해주기)
        if (which == DialogInterface.BUTTON_NEGATIVE) {
        }
    };
}