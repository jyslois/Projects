package com.android.mymindnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;
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
    AlertDialog alertDialog;

    // 알림 dialoguee
    void dialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("확인", null);
        alertDialog = builder.show();
        // 메시지 크기 조절
        TextView messageText = alertDialog.findViewById(android.R.id.message);
        messageText.setTextSize((float) (standardSize_X / 24));
        // 버튼 크기 조절
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 25));
        alertDialog.show();
    }

    // 화면 크기에 따른 글자 크기 조절
    int standardSize_X, standardSize_Y;
    float density;

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }
    public void getStandardSize() {
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground).into(binding.background);

        // 액션 바 타이틀
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.changepassword_actionbartext); // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.sendEmailButton.setOnClickListener(view -> {
            email = binding.emailInput.getText().toString();

            if (email.equals("")) {
                dialog("임시 비밀번호를 보낼 이메일을 입력해 주세요.");
            } else {
                // 메세지 띄우기
                Toast toast = Toast.makeText(this, "처리 중......", Toast.LENGTH_SHORT);
                toast.show();
                // 임시 비밀번호 (랜덤)
                int leftLimit = 48; // numeral '0'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 10;
                Random random = new Random();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    randomPassword = random.ints(leftLimit, rightLimit + 1)
                            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                            .limit(targetStringLength)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                }

                changePassword();
            }


        });

        // 글짜 크기 조절
        getStandardSize();
        binding.emailInput.setTextSize((float) (standardSize_X / 22));
        binding.sendEmailButton.setTextSize((float) (standardSize_X / 25));
        binding.instruction.setTextSize((float) (standardSize_X / 26));
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
                   dialog((String) response.body().get("msg"));
                } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 3006) {
                    // 전송 완료 알림 띄우기
                    dialog((String) response.body().get("msg"));
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                dialog("네트워크 연결에 실패했습니다. 다시 시도해 주세요.");
            }
        });

    }
}