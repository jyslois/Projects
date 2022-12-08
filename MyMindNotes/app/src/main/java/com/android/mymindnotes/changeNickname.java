package com.android.mymindnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.widget.TextView;

import com.android.mymindnotes.databinding.ActivityChangeNicknameBinding;
import com.android.mymindnotes.model.ChangeUserNickname;
import com.android.mymindnotes.retrofit.ChangeNicknameApi;
import com.android.mymindnotes.retrofit.CheckNicknameApi;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeNickname extends AppCompatActivity {
    ActivityChangeNicknameBinding binding;
    // 중복 확인
    AlertDialog alertDialog;
    boolean nicknameCheck;

    // 회원 번호 불러오기
    SharedPreferences userindex;

    // 닉네임
    String nickname;

    // 화면 크기에 따른 글자 크기 조절
    int standardSize_X, standardSize_Y;
    float density;

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

    // 닉네임 중복 체크 확인완료 dialogue
    void confirmNicknameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("사용 가능한 닉네임입니다.");
        builder.setPositiveButton("확인", null);
        alertDialog = builder.show();
        // 메시지 크기 조절
        TextView messageText = alertDialog.findViewById(android.R.id.message);
        messageText.setTextSize((float) (standardSize_X / 24));
        // 버튼 크기 조절
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 25));
        alertDialog.show();
        nicknameCheck = true;
        binding.checkNicknameButton.setText("확인완료");
        binding.checkNicknameButton.setBackgroundColor(Color.parseColor("#FFDDD5")); // String으로된 Color값을 Int로 바꾸기
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeNicknameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        // 액션 바 타이틀
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.changenickname_actionbartext); // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 글짜 크기 세팅
        getStandardSize();
        binding.nickNameText.setTextSize((float) (standardSize_X / 23));
        binding.nickNameInput.setTextSize((float) (standardSize_X / 23));
        binding.checkNicknameButton.setTextSize((float) (standardSize_X / 23));
        binding.changeNicknameButton.setTextSize((float) (standardSize_X / 22));


        // 회원번호 불러오기
        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);

        // 닉네임 형식 체크
        String nicknamePattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$";

        // 닉네임 중복 체크
        binding.checkNicknameButton.setOnClickListener(view -> {
            nickname = binding.nickNameInput.getText().toString();
            if (nickname.equals("")) {
                dialog("닉네임을 입력해 주세요");
                // 닉네임 형식 체크
            } else if(!Pattern.matches(nicknamePattern, nickname)) {
                dialog("특수문자를 제외한 2~10자여야 합니다.");
            } else {
                if (nicknameCheck == false) {
                    // 네트워크 통신(닉네임이 중복됐는지 체크)
                    checkNickname();
                }
            }
        });

        // 중복확인 후에 닉네임 input이 일어나면 다시 중복체크 해야 하므로
        binding.nickNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nicknameCheck = false;
                binding.checkNicknameButton.setText("중복확인");
                binding.checkNicknameButton.setBackgroundColor(Color.parseColor("#C3BE9F98")); // String으로된 Color값을 Int로 바꾸기
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 닉네임 변경 버튼 클릭
        binding.changeNicknameButton.setOnClickListener(view -> {
            // 중복확인을 하지 않았다면
            if (nicknameCheck == false) {
                dialog("닉네임 중복확인을 해주세요");
            } else {
                nickname = binding.nickNameInput.getText().toString();
                // 닉네임 수정 네트워크 코드
                changeNickname();
            }
        });

    }

    // Up 버튼 눌렀을 때 액션
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // 네트워크 통신: 닉네임 중복 체크
    public void checkNickname() {
                // Retrofit 객체 생성
                RetrofitService retrofitService = new RetrofitService();
                // Retrofit 객체에 Service 인터페이스 등록
                CheckNicknameApi checkNicknameApi = retrofitService.getRetrofit().create(CheckNicknameApi.class);
                // Call 객체 획득
                Call<Map<String, Object>> call = checkNicknameApi.checkNickname(binding.nickNameInput.getText().toString());
                // 네트워킹 시도
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 1003) {
                            dialog((String) response.body().get("msg"));
                        } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 1002) {
                            confirmNicknameDialog();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        dialog("네트워크 연결에 실패했습니다. 다시 시도해 주세요.");
                    }
                });

    }


    // 네트워크 통신: 닉네임 변경
    public void changeNickname() {
            // Retrofit 객체 생성
            RetrofitService retrofitService = new RetrofitService();
            // Retrofit 객체에 인터페이스(Api) 등록, Call 객체 반환하는 Service 객체 생성
            ChangeNicknameApi changeNicknameApi = retrofitService.getRetrofit().create(ChangeNicknameApi.class);
            // Call 객체 획득
            ChangeUserNickname changeUserNickname = new ChangeUserNickname(userindex.getInt("userindex", 0), nickname);
            Call<Map<String, Object>> call = changeNicknameApi.updateUserNickname(changeUserNickname);
            // 네트워킹 시도
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                    // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                    if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 3001) {
                        dialog((String) response.body().get("msg"));
                    } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 3000) {
                        // 화면 전환
                        Intent intent = new Intent(getApplicationContext(), MainPage.class);
                        startActivity(intent);
                    }
                }
                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    dialog("네트워크 연결에 실패했습니다. 다시 시도해 주세요.");
                }
            });
    }

}