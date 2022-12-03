package com.android.mymindnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityFindPasswordBinding;
import com.android.mymindnotes.model.ChangeToTemporaryPassword;
import com.android.mymindnotes.retrofit.ChangeToTempPassword;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPassword extends AppCompatActivity {
    ActivityFindPasswordBinding binding;
    String randomPassword;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground).into(binding.background);

        // 액션 바 타이틀
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.findpassword_actionbartext); // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.sendEmailButton.setOnClickListener(view -> {
            email = binding.emailInput.getText().toString();

            // 임시 비밀번호 (랜덤)
            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                randomPassword = random.ints(leftLimit,rightLimit + 1)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
            }

            changePassword();


        });
    }

    // 네트워크 통신: 비밀번호 변경
    public void changePassword() {
        // Retrofit 객체 생성
        RetrofitService retrofitService = new RetrofitService();
        // Retrofit 객체에 인터페이스(Api) 등록, Call 객체 반환하는 Service 객체 생성
        ChangeToTempPassword changeToTempPassword = retrofitService.getRetrofit().create(ChangeToTempPassword.class);
        // Call 객체 획득
        ChangeToTemporaryPassword changeToTemporaryPassword = new ChangeToTemporaryPassword(email, randomPassword);
        Call<Map<String, Object>> call = changeToTempPassword.toTemPassword(changeToTemporaryPassword);
        // 네트워킹 시도
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 3007) {
                    Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                    toast.show();
                } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 3006) {
                    // 변경 완료 알림 띄우기
                    Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                    toast.show();
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