package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityNewReflectionBinding;
import com.android.mymindnotes.model.UserDiary;
import com.android.mymindnotes.model.retrofit.RecordDiaryApi;
import com.android.mymindnotes.data.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class New_Reflection extends AppCompatActivity {
    ActivityNewReflectionBinding binding;
    SharedPreferences reflection;
    SharedPreferences.Editor reflectionEdit;
    SharedPreferences type;
    SharedPreferences.Editor typeEdit;
    SharedPreferences emotion;
    SharedPreferences emotionText;
    SharedPreferences situation;
    SharedPreferences thought;
    SharedPreferences emotionColor;
    SharedPreferences.Editor emotionEdit;
    SharedPreferences.Editor emotionTextEdit;
    SharedPreferences.Editor situationEdit;
    SharedPreferences.Editor thoughtEdit;
    SharedPreferences.Editor emotionColorEdit;


    SharedPreferences userindex;

    String date;
    String day;
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
        binding = ActivityNewReflectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.recordbackground);

        // Tips
        // Tips 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tips);
        builder.setTitle("회고 작성 Tips");
        builder.setMessage(R.string.reflectionTips);
        builder.setPositiveButton("확인", null);
        // Tips 이미지 클릭 시 다이얼로그 띄우기
        binding.RecordReflectionTips.setOnClickListener(view -> {
            AlertDialog alertDialog = builder.show();
            // 메시지 크기 조절
            TextView messageText = alertDialog.findViewById(android.R.id.message);
            messageText.setTextSize((float) (standardSize_X / 24));
            // 버튼 크기 조절
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 25));
            alertDialog.show();
        });

        reflection = getSharedPreferences("reflection", MODE_PRIVATE);
        reflectionEdit = reflection.edit();
        type = getSharedPreferences("type", MODE_PRIVATE);
        typeEdit = type.edit();

        emotion = getSharedPreferences("emotion", MODE_PRIVATE);
        emotionEdit = emotion.edit();
        emotionText = getSharedPreferences("emotionText", MODE_PRIVATE);
        emotionTextEdit = emotionText.edit();
        emotionColor = getSharedPreferences("emotionColor", MODE_PRIVATE);
        emotionColorEdit = emotionColor.edit();
        situation = getSharedPreferences("situation", MODE_PRIVATE);
        situationEdit = situation.edit();
        thought = getSharedPreferences("thought", MODE_PRIVATE);
        thoughtEdit = thought.edit();

        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);

        // 글짜 크기 조절
        getStandardSize();
        binding.title.setTextSize((float) (standardSize_X / 21));
        binding.RecordReflectionTips.setTextSize((float) (standardSize_X / 25));
        binding.RecordReflectionUserInput.setTextSize((float) (standardSize_X / 23));
        binding.RecordEmotionHelpButton.setTextSize((float) (standardSize_X / 25));
        binding.RecordPreviousButton.setTextSize((float) (standardSize_X / 23));
        binding.RecordSaveButton.setTextSize((float) (standardSize_X / 23));

        // 감정 설명서 페이지로 이동
        binding.RecordEmotionHelpButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        });

        // 이전 버튼 클릭시 이전 화면으로
        binding.RecordPreviousButton.setOnClickListener(view -> {
            // 회고 저장
            reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
            reflectionEdit.commit();
            finish();
        });

        // 저장 버튼 클릭
        binding.RecordSaveButton.setOnClickListener(view -> {
            // 회고 저장
            reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
            reflectionEdit.commit();
            // 타입 저장
            typeEdit.putString("type", "오늘의 마음 일기");
            typeEdit.commit();
            // 날짜 저장
            long now = System.currentTimeMillis();
            Date getDate = new Date(now);
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = mFormat.format(getDate);
            // 요일 저장
            final String[] DAY = {"", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
            Calendar today = Calendar.getInstance();
            day = DAY[today.get(Calendar.DAY_OF_WEEK)];

            // 서버에 데이터 저장
            recordDiary();
        });

        // 만약 회고가 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
        String refle = reflection.getString("reflection", "");
        if (!refle.equals("")) {
            binding.RecordReflectionUserInput.setText(refle);
        }
    }

    // 네트워크 통신: 일기 저장
    public void recordDiary() {
            // Retrofit 객체 생성
            RetrofitService retrofitService = new RetrofitService();
            // Retrofit 객체에 인터페이스(Api) 등록, Call 객체 반환하는 Service 객체 생성
            RecordDiaryApi recordDiaryApi = retrofitService.getRetrofit().create(RecordDiaryApi.class);
            // Call 객체 획득
            UserDiary userDiary = new UserDiary(userindex.getInt("userindex", 0), type.getString("type", ""), date, day, situation.getString("situation", ""),
                    thought.getString("thought", ""), emotion.getString("emotion", ""), emotionText.getString("emotionText", ""), reflection.getString("reflection", "회고"));
            Call<Map<String, Object>> call = recordDiaryApi.addDiary(userDiary);
            // 네트워킹 시도
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 6001) {
                        dialog((String) response.body().get("msg"));
                    } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 6000) {
                        // 저장한 것 삭제
                        reflectionEdit.clear();
                        reflectionEdit.commit();
                        emotionColorEdit.clear();
                        emotionColorEdit.commit();
                        emotionEdit.clear();
                        emotionEdit.commit();
                        emotionTextEdit.clear();
                        emotionTextEdit.commit();
                        situationEdit.clear();
                        situationEdit.commit();
                        thoughtEdit.clear();
                        thoughtEdit.commit();
                        typeEdit.clear();
                        typeEdit.commit();
                        Toast.makeText(getApplicationContext(), R.string.successfulRecord, Toast.LENGTH_SHORT).show();
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

    // backprssed 시 회고 저장 후 뒤로가기
    @Override
    public void onBackPressed() {
        // 생각 저장
        reflectionEdit.putString("reflection", binding.RecordReflectionUserInput.getText().toString());
        reflectionEdit.commit();
        finish();
    }

}