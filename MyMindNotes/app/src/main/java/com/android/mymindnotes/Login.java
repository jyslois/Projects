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
import com.android.mymindnotes.model.UserInfo;
import com.android.mymindnotes.model.UserInfoLogin;
import com.android.mymindnotes.retrofit.JoinApi;
import com.android.mymindnotes.retrofit.LoginApi;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    Button loginButton;
    public CheckBox autoSave;
    CheckBox autoLogin;
    EditText email;
    EditText password;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;

    // 로그인 성공 시 nickname과 userindex 저장
    SharedPreferences nickName;
    SharedPreferences.Editor nickNameEdit;
    SharedPreferences userindex;
    SharedPreferences.Editor userindexEdit;

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
        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);
        userindexEdit = userindex.edit();


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
            // 만약 체크 박스가 체크되어 있거나, 로그인 상태 유지 체크 박스가 체크되어 있다면,
            if (autoSave.isChecked() || autoLogin.isChecked()) {
                // 상태 저장
                autoSaveEdit.putBoolean("autoSaveCheck", true);
                autoSaveEdit.apply();
                // 아이디와 비밀번호 저장
                autoSaveEdit.putString("id", email.getText().toString());
                autoSaveEdit.putString("password", password.getText().toString());
            } else {
                // 체크되어 있지 않다면,
                // 상태 저장, 아이디와 비밀번호 값을 ""로 저장
                autoSaveEdit.putBoolean("autoSaveCheck", false);
                autoSaveEdit.apply();
                autoSaveEdit.putString("id", "");
                autoSaveEdit.putString("password", "");
            }
            autoSaveEdit.commit();
        });



        // 만약 로그인 상태 유지 값이 true로 저장되어 있다면
        if (auto.getBoolean("autoLoginCheck", false)) {
            // 체크박스 상태도 체크로 표시 (아이디 비밀번호 저장, 로그인 상태 유지 둘 다 체크)
            autoLogin.setChecked(true);
            autoSave.setChecked(true);
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
                autoSave.setChecked(true);
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
            // 비밀번호가 6자리 이하라면
            } else if (binding.password.getText().toString().length() < 6){
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT);
                toast.show();
            // 올바르게 적었다면
            } else {
                // 만약 자동 로그인 버튼이 체크되어 있다면
                if (autoLogin.isChecked()) {
                    // 상태 저장
                    autoSaveEdit.putBoolean("autoLoginCheck", true);
                    autoSaveEdit.apply();
                    autoSave.setChecked(true);
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

                login();
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


    // 로그인 - 백그라운드 쓰레드에서 네트워크 코드 작업
    public void login() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Retrofit 객체 생성
                RetrofitService retrofitService = new RetrofitService();
                // Retrofit 객체에 Service 인터페이스 등록
                LoginApi loginApi = retrofitService.getRetrofit().create(LoginApi.class);
                // Call 객체 획득
                UserInfoLogin userInfoLogin = new UserInfoLogin(email.getText().toString(), password.getText().toString());
                Call<Map<String, Object>> call = loginApi.login(userInfoLogin);
                // 네트워킹 시도
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 5001 || Double.parseDouble(String.valueOf(response.body().get("code"))) == 5003 || Double.parseDouble(String.valueOf(response.body().get("code"))) == 5005) {
                            Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 5000) {
                            // 회원 번호 저장
                            userindexEdit.putInt("userindex", (int) Double.parseDouble(String.valueOf((response.body().get("user_index")))));
                            userindexEdit.commit();
                            // 닉네임 저장
                            nickNameEdit.putString("nickName", String.valueOf((response.body().get("nickname"))));
                            nickNameEdit.commit();
                            // 환영 메시지 띄우기
                            Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                            toast.show();
                            // 화면 전환
                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }
        });
        thread.start();
    }

}