package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityJoinBinding;
import com.bumptech.glide.Glide;

public class Join extends AppCompatActivity {
    ActivityJoinBinding binding;
    SharedPreferences nickName;
    SharedPreferences.Editor nickNameEdit;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground).into(binding.background);

        nickName = getSharedPreferences("nickName", Activity.MODE_PRIVATE);
        nickNameEdit = nickName.edit();

        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();

        // 회원 가입 버튼 클릭 이벤트
        binding.joinButton.setOnClickListener(view -> {
            String nickNameInput = binding.nickNameInput.getText().toString();
            String emailInput = binding.emailInput.getText().toString();
            String passwordInput = binding.passwordInput.getText().toString();
            String passwordReTypeInput = binding.passwordRetypeInput.getText().toString();

            // 만약 이메일이나 페스워드를 적지 않았다면
            if (emailInput.equals("") || passwordInput.equals("") || passwordReTypeInput.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
            // 이메일 형식이 잘못되었다면
            } else if(!emailInput.contains("@")) {
                Toast toast = Toast.makeText(getApplicationContext(), "올바른 이메일 형식으로 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
            // 만약 비밀번호가 6자리 미만이라면
            } else if(binding.passwordInput.getText().toString().length() < 6) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호는 최소 6자리여야 합니다", Toast.LENGTH_SHORT);
                toast.show();
            // 비밀번호와 비밀번호 확인란이 일치하지 않으면
            } else if (!binding.passwordInput.getText().toString().equals(binding.passwordRetypeInput.getText().toString())) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // 닉네임 저장
                // 만약 닉네임이 입력란이 빈칸이라면, 이메일 앞부분(아이디)을 닉네임으로 저장
                if (binding.nickNameInput.getText().toString().equals("")) {
                    int index = emailInput.indexOf("@");
                    String emailBeforeAt = emailInput.substring(0, index);
                    nickNameEdit.putString("nickName", emailBeforeAt);
                } else {
                    nickNameEdit.putString("nickName", nickNameInput);
                }
                nickNameEdit.commit();

                // 아이디와 비밀번호 저장
                autoSaveEdit.putString("id", emailInput);
                autoSaveEdit.putString("password", passwordInput);
                autoSaveEdit.commit();

                // 아이디/비밀번호 저장 체크 박스 상태를 true로 저장
                autoSaveEdit.putBoolean("autoSaveCheck", true);
                autoSaveEdit.commit();

                // 메인 화면 전환
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                startActivity(intent);
            }
        });
    }

}