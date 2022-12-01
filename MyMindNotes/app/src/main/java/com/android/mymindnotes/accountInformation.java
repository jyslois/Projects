package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.mymindnotes.retrofit.DeleteUserApi;
import com.android.mymindnotes.retrofit.GetUserInfoApi;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInformation extends AppCompatActivity {
    com.android.mymindnotes.databinding.ActivityAccountInformationBinding binding;
    AlertDialog alertDialog;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;
    SharedPreferences userindex;
    String email;
    String nickname;
    int birthyear;

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.android.mymindnotes.databinding.ActivityAccountInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();

        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);
        getUserInfo();

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        // 변경 버튼 이벤트 처리
        binding.changePasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
            startActivity(intent);
        });

        binding.changeNicknameButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangeNickname.class);
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
        // 탈퇴 눌렀을 때 이벤트 처리
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            deleteUser();
        }
    };

    // 네트워크 통신: 회원 정보 가져오기
    public void getUserInfo() {
            // Retrofit 객체 생성
            RetrofitService retrofitService = new RetrofitService();
            // Retrofit 객체에 인터페이스(Api) 등록, Call 객체 반환하는 Service 객체 생성
            GetUserInfoApi getUserInfoApi = retrofitService.getRetrofit().create(GetUserInfoApi.class);
            // Call 객체 획득
            Call<Map<String, Object>> call = getUserInfoApi.getUserInfo(userindex.getInt("userindex", 0));
            // 네트워킹 시도
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    // 이메일, 닉네임, 생년 세팅
                    binding.email.setText(String.valueOf(response.body().get("email")));
                    binding.nickname.setText((String.valueOf(response.body().get("nickname"))));
                    int birthyear = (int) Double.parseDouble(String.valueOf((response.body().get("birthyear"))));
                    binding.birthyear.setText(String.valueOf(birthyear));
                }
                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
    }

    // 네트워크 통신: 회원 탈퇴 처리
    public void deleteUser() {
                // Retrofit 객체 생성
                RetrofitService retrofitService = new RetrofitService();
                // Retrofit 객체에 Service 인터페이스 등록
                DeleteUserApi deleteUserApi = retrofitService.getRetrofit().create(DeleteUserApi.class);
                // Call 객체 획득
                Call<Map<String, Object>> call = deleteUserApi.deleteUser(userindex.getInt("userindex", 0));
                // 네트워킹 시도
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 4000) {
                            // 탈퇴 완료 메시지 띄우기
                            Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                            toast.show();
                            // 저장된 것 모두 지우기
                            autoSaveEdit.clear();
                            autoSaveEdit.commit();
                            // 화면 전환
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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