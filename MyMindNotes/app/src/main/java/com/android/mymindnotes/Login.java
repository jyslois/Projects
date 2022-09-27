package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityLoginBinding;
import com.bumptech.glide.Glide;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    Button loginButton;
    public CheckBox autoSave;
    CheckBox autoLogin;
    EditText email;
    EditText password;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;
    SharedPreferences nickName;
    SharedPreferences.Editor nickNameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground).into(binding.background);

        // 로그인 정보 저장 & 자동 로그인 구현 관련
        loginButton = binding.loginButton;
        autoSave = binding.autoSaveButton;
        autoLogin = binding.autoLoginButton;
        email = binding.email;
        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();
        password = binding.password;
        nickName = getSharedPreferences("nickName", Activity.MODE_PRIVATE);
        nickNameEdit = nickName.edit();


        // 만약 autoSaveCheck 값이 true로 저장되어 있다면
        if (auto.getBoolean("autoSaveCheck", false)) {
            // 체크박스 상태도 체크로 표시하기
            autoSave.setChecked(true);
            // 저장된 아이디와 암호를 가져와 뷰에 뿌리기
            email.setText(auto.getString("id", ""));
            password.setText(auto.getString("password", ""));
        } else {
            autoSave.setChecked(false);
        }

        // 아이디/비밀번호 저장 체크 박스 클릭 시
        autoSave.setOnClickListener(view -> {
            // 만약 체크 박스가 체크되어 있다면
            if (autoSave.isChecked()) {
                // 상태 저장
                autoSaveEdit.putBoolean("autoSaveCheck", true);
                autoSaveEdit.apply();
                // 아이디와 비밀번호 저장
                autoSaveEdit.putString("id", email.getText().toString());
                autoSaveEdit.putString("password", password.getText().toString());
            } else {
                // 체크되어 있지 않다면 상태 저장, 아이디와 비밀번호 값을 ""로 저장
                autoSaveEdit.putBoolean("autoSaveCheck", false);
                autoSaveEdit.apply();
                autoSaveEdit.putString("id", "");
                autoSaveEdit.putString("password", "");
            }
            autoSaveEdit.commit();
        });


        // 만약 로그인 상태 유지 값이 true로 저장되어 있다면
        if (auto.getBoolean("autoLoginCheck", false)) {
            // 체크박스 상태도 체크로 표시
            autoLogin.setChecked(true);
            // 만약 이메일과 패스워드가 비어 있지 않다면, 자동 로그인 하기
            if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                startActivity(intent);
            }
        } else {
            autoLogin.setChecked(false);
        }


        // 자동 로그인 체크 버튼 클릭 이벤트
        autoLogin.setOnClickListener(view -> {
            // 만약 체크 박스가 체크되어 있다면
            if (autoLogin.isChecked()) {
                autoSaveEdit.putBoolean("autoLoginCheck", true);
                autoSaveEdit.apply();
            } else {
                // 체크되어 있지 않다면
                autoSaveEdit.putBoolean("autoLoginCheck", false);
                autoSaveEdit.apply();
            }
            autoSaveEdit.commit();
        });


        // 로그인 버튼 클릭 시
        loginButton.setOnClickListener(view -> {
            String emailInput = email.getText().toString();
            String passwordInput = password.getText().toString();

            // 만약 이메일/페스워드를 적지 않았다면
            if (emailInput.equals("") && passwordInput.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT);
                toast.show();
            } else if (emailInput.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT);
                toast.show();
            } else if (passwordInput.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT);
                toast.show();
                // 바른 이메일 형식을 입력하지 않았다면ㄴ
            } else if(!emailInput.contains("@")) {
                Toast toast = Toast.makeText(getApplicationContext(), "올바른 이메일 형식으로 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
            // 올바르게 적었다면
            } else {
                // 만약 자동 로그인 버튼이 체크되어 있다면
                if (autoLogin.isChecked()) {
                    // 상태 저장
                    autoSaveEdit.putBoolean("autoLoginCheck", true);
                    autoSaveEdit.apply();
                }

                // 아이디/비밀번호 저장 버튼이 체크되어 있다면
                if (autoSave.isChecked()) {
                    // 체크박스 상태 저장
                    autoSaveEdit.putBoolean("autoSaveCheck", true);
                    autoSaveEdit.apply();
                    // 아이디와 비밀번호 저장
                    autoSaveEdit.putString("id", emailInput);
                    autoSaveEdit.putString("password", passwordInput);
                } else {
                    autoSaveEdit.putBoolean("autoCheck", false);
                    autoSaveEdit.apply();
                    autoSaveEdit.putString("id", null);
                    autoSaveEdit.putString("password", null);
                }
                autoSaveEdit.commit();

                // 만약 닉네임이 설정되지 않은 상태라면, 이메일 앞부분(아이디)을 닉네임으로 저장
                if (nickName.getString("nickName", "").equals("")) {
                    int index = emailInput.indexOf("@");
                    String emailBeforeAt = emailInput.substring(0, index);
                    nickNameEdit.putString("nickName", emailBeforeAt);
                    nickNameEdit.apply();
                }

                // 로그인 성공
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();

        // 만약 체크 박스가 체크되어 있다면
        if (autoSave.isChecked()) {
            autoSaveEdit.putBoolean("autoSaveCheck", true);
            autoSaveEdit.apply();
            // 아이디와 비밀번호 저장
            autoSaveEdit.putString("id", email.getText().toString());
            autoSaveEdit.putString("password", password.getText().toString());
        } else {
            // 체크되어 있지 않다면 아이디와 비밀번호 값을 null 저장
            autoSaveEdit.putBoolean("autoSaveCheck", false);
            autoSaveEdit.apply();
            autoSaveEdit.putString("id", "");
            autoSaveEdit.putString("password", "");
        }
        autoSaveEdit.apply();

    }


}