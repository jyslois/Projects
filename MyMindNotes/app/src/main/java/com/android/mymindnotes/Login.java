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

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    Button loginButton;
    CheckBox autoSave;
    CheckBox autoLogin;
    EditText email;
    EditText password;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // 로그인 정보 저장 & 자동 로그인 구현 관련
        loginButton = binding.loginButton;
        autoSave = binding.autoSaveButton;
        autoLogin = binding.autoLoginButton;
        email = binding.email;
        password = binding.passward;
        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();


        // 만약 autoSaveCheck 값이 true로 저장되어 있다면
        if (auto.getBoolean("autoSaveCheck", false)) {
            // 체크박스 상태도 체크로 표시해서 가져오기
            autoSave.setChecked(true);
            // 저장된 아이디와 암호를 가져와 세팅하기
            email.setText(auto.getString("id", ""));
            password.setText(auto.getString("password", ""));
        } else {
            autoSave.setChecked(false);
        }

        // 로그인 정보 저장 체크 박스 클릭 시
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


        // 만약 자동 로그인 값이 true로 저장되어 있다면
        if (auto.getBoolean("autoLoginCheck", false)) {
            // 체크박스 상태도 체크로 표시하기
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
            // 만약 자동 로그인 버튼이 체크되어 있다면
            if (autoLogin.isChecked()) {
                // 상태 저장
                autoSaveEdit.putBoolean("autoLoginCheck", true);
                autoSaveEdit.apply();
            }
            // 만약 이메일/페스워드를 적지 않았다면
            if (email.getText().toString().equals("") && password.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT);
                toast.show();
            } else if (email.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT);
                toast.show();
            } else if (password.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT);
                toast.show();
            // 둘 다 적었다면
            } else {
                // 로그인 정보 저장 버튼이 체크되어 있다면
                if (autoSave.isChecked()) {
                    // 상태 저장
                    autoSaveEdit.putBoolean("autoSaveCheck", true);
                    autoSaveEdit.apply();
                    // 아이디와 비밀번호 저장
                    autoSaveEdit.putString("id", email.getText().toString());
                    autoSaveEdit.putString("password", password.getText().toString());
                } else {
                    autoSaveEdit.putBoolean("autoCheck", false);
                    autoSaveEdit.apply();
                    autoSaveEdit.putString("id", null);
                    autoSaveEdit.putString("password", null);
                }
                autoSaveEdit.commit();

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
        autoSaveEdit.commit();
    }


}