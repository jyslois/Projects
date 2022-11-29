package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityRecordMindChoiceBinding;
import com.android.mymindnotes.retrofit.GetUserInfoApi;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordMindChoice extends AppCompatActivity {
    ActivityRecordMindChoiceBinding binding;
    SharedPreferences userindex;
    String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRecordMindChoiceBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);

        getUserNickname();

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground2).into(binding.choicebackground);


        // 오늘의 마음 일기 버튼 클릭 시
        binding.todayEmotionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), New_Emotion.class);
            startActivity(intent);
        });

        // 트라우마 일기 버튼 클릭 시
        binding.traumaButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Old_Situation.class);
            startActivity(intent);
        });

    }

    // 회원 정보 가져오기 네트워크 통신
    public void getUserNickname() {
        Thread thread = new Thread(() -> {
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
                    // 닉네임 세팅
                    nick = (String) response.body().get("nickname");
                    binding.nickNameText.setText(nick + " 님,");
                }
                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        });
        thread.start();
    }

}