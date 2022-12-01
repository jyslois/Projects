package com.android.mymindnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityChangePasswordBinding;
import com.android.mymindnotes.model.ChangeUserPassword;
import com.android.mymindnotes.model.UserInfoLogin;
import com.android.mymindnotes.retrofit.ChangePasswordApi;
import com.android.mymindnotes.retrofit.LoginApi;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    ActivityChangePasswordBinding binding;

    // 회원 번호 불러오기
    SharedPreferences userindex;

    // 바뀐 비밀번호 저장해두기
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;

    // 비밀번호
    String passwordInput;
    String passwordRetypeInput;
    String originalPasswordInput;

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


        // 회원번호 불러오기
        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);

        // 바뀐 비밀번호 저장해두기
        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();


        // 비밀번호 형식 체크
        String passwordPattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}";

        // 비밀번호 변경 버튼 클릭 이벤트
        binding.changePasswordButton.setOnClickListener(view -> {
            passwordInput = binding.passwordInput.getText().toString();
            passwordRetypeInput = binding.passwordReypeInput.getText().toString();
            originalPasswordInput = binding.originalPasswordInput.getText().toString();
            if (originalPasswordInput.equals("") || passwordInput.equals("") || passwordRetypeInput.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
            } else if (!passwordInput.equals(passwordRetypeInput)) {
                Toast toast = Toast.makeText(getApplicationContext(), "새로운 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT);
                toast.show();
            } else if (!Pattern.matches(passwordPattern, passwordInput)) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호는 영문+숫자 조합 6자~20자여야 합니다", Toast.LENGTH_SHORT);
                toast.show();
                // 비밀번호와 비밀번호 확인란이 일치하지 않으면
            } else {
                // 네트워크 통신, 비밀번호 변경
                changePassword();
            }
        });
    }

    // 네트워크 통신: 비밀번호 변경
    public void changePassword() {
                // Retrofit 객체 생성
                RetrofitService retrofitService = new RetrofitService();
                // Retrofit 객체에 인터페이스(Api) 등록, Call 객체 반환하는 Service 객체 생성
                ChangePasswordApi changePasswordApi = retrofitService.getRetrofit().create(ChangePasswordApi.class);
                // Call 객체 획득
                ChangeUserPassword changeUserPassword = new ChangeUserPassword(userindex.getInt("userindex", 0), passwordInput, originalPasswordInput);
                Call<Map<String, Object>> call = changePasswordApi.updateUserPassword(changeUserPassword);
                // 네트워킹 시도
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 3005 || Double.parseDouble(String.valueOf(response.body().get("code"))) == 3003) {
                            Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 3002) {
                            // 비밀번호 재저장
                            autoSaveEdit.putString("password", passwordInput);
                            autoSaveEdit.commit();
                            // 변경 완료 알림 띄우기
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
}