package com.android.mymindnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityChangePasswordBinding;
import com.bumptech.glide.Glide;

import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        // 액션 바 타이틀
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.changepassword_actionbartext); // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // 비밀번호 형식 체크
        String passwordPattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}";

        // 비밀번호 변경 버튼 클릭 이벤트
        binding.changePasswordButton.setOnClickListener(view -> {
            if (binding.originalPasswordInput.getText().toString().equals("") || binding.passwordInput.getText().toString().equals("") || binding.passwordReypeInput.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
            } else if (!binding.passwordInput.getText().toString().equals(binding.passwordReypeInput.getText().toString())) {
                Toast toast = Toast.makeText(getApplicationContext(), "새로운 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT);
                toast.show();
            } else if (!Pattern.matches(passwordPattern, binding.passwordInput.getText().toString())) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호는 영문+숫자 조합 6자~20자여야 합니다", Toast.LENGTH_SHORT);
                toast.show();
                // 비밀번호와 비밀번호 확인란이 일치하지 않으면
            } else {
                // 네트워크 통신, 비밀번호 변경
                changePassword();
            }
        });
    }

    // 비밀번호 변경 네트워크 통신
    public void changePassword() {

    }
}