package com.android.mymindnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityDiaryResultBinding;
import com.android.mymindnotes.model.UserDiary;
import com.android.mymindnotes.model.retrofit.DeleteDiaryApi;
import com.android.mymindnotes.model.retrofit.GetDiaryListApi;
import com.android.mymindnotes.model.retrofit.RetrofitService;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Diary_Result extends AppCompatActivity {
    ActivityDiaryResultBinding binding;
    String type;
    String date;
    String situation;
    String thought;
    String emotion;
    String emotionText;
    String reflection;
    int index;
    int diaryNumber;
    SharedPreferences userindex;
    ArrayList<UserDiary> diarylist;

    // 화면 크기에 따른 글자 크기 조절
    int standardSize_X, standardSize_Y;
    float density;

    // viewpager2, tablayout
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    private String[] tabs = new String[]{"상황", "생각", "감정", "회고"};
    ViewPager2Adapter adapter;

    // 화면 크기에 따라서 글자 사이즈 조절하기
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

    private ActivityResultLauncher editResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),new ActivityResultCallback<ActivityResult>(){
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // 서버를 요청해서 갱신 처리할수도 있고, 자체적으로 갱신처리도 가능.
                refreshDiary();
            }
        }
    });


    @Override
    protected void onResume() {
        refreshDiary();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 회원 번호 저장
        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground4).into(binding.background);

        // 목록으로 돌아가기 버튼 클릭 시 전 페이지로
        binding.backtoListButton.setOnClickListener(view -> {
            finish();
        });

        // 데이터 세팅을 위해 가져오기
        Intent intent = getIntent();
        index = intent.getIntExtra("index", index);

        // 데이터 세팅
        type = intent.getStringExtra("type");
        date = intent.getStringExtra("date");
        situation = intent.getStringExtra("situation");
        thought = intent.getStringExtra("thought");
        emotion = intent.getStringExtra("emotion");
        emotionText = intent.getStringExtra("emotionText");
        reflection = intent.getStringExtra("reflection");
        index = intent.getIntExtra("index", index);
        diaryNumber = intent.getIntExtra("diaryNumber", 0);

        // 타입 뿌리기
        binding.type.setText(type + ",  ");
        // 오늘 날짜 뿌리기
        binding.date.setText(date + " ");

        // 글짜 크기 조절
        getStandardSize();
        binding.type.setTextSize((float) (standardSize_X / 25));
        binding.date.setTextSize((float) (standardSize_X / 25));
        binding.deleteButton.setTextSize((float) (standardSize_X / 24));
        binding.editButton.setTextSize((float) (standardSize_X / 24));
        binding.backtoListButton.setTextSize((float) (standardSize_X / 24));

        // viewPager2와 tablayout 세팅
        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewpager2;
        adapter = new ViewPager2Adapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(4);

        // viewPager2와 tablayout 연동하기
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(tabs[position]);
        }).attach();



        // 수정 버튼 클릭 시
        binding.editButton.setOnClickListener(view -> {
            Intent intento = new Intent(getApplicationContext(), Diary_Result_Edit.class);
            intento.putExtra("date", date);
            intento.putExtra("type", type);
            intento.putExtra("situation", situation);
            intento.putExtra("thought", thought);
            intento.putExtra("emotion", emotion);
            intento.putExtra("emotionText", emotionText);
            intento.putExtra("reflection", reflection);
            intento.putExtra("diaryNumber", diaryNumber);
            intento.putExtra("index", index);
            editResult.launch(intento);
        });

        // 삭제 버튼 클릭 시
        binding.deleteButton.setOnClickListener(view -> {
            deleteDiary();
        });



    }

    // 네트워크 통신: 일기 삭제
    public void deleteDiary() {
            RetrofitService retrofitService = new RetrofitService();
            DeleteDiaryApi deleteDiaryApi = retrofitService.getRetrofit().create(DeleteDiaryApi.class);
            Call<Map<String, Object>> call = deleteDiaryApi.deleteDiary(diaryNumber);
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 9000) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
    }




    // 네트워크 통신: 일기리스트 가져와서 업데이트된 일기 내용 뿌리기
    public void refreshDiary() {
            RetrofitService retrofitService = new RetrofitService();
            GetDiaryListApi getDiaryListApi = retrofitService.getRetrofit().create(GetDiaryListApi.class);
            Call<Map<String, Object>> call = getDiaryListApi.getAllDiary(userindex.getInt("userindex", 0));
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 7000) {
                        // 서버로부터 리스트 받아와서 저장하기
                        // https://ppizil.tistory.com/4
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<UserDiary>>() {
                        }.getType();
                        String jsonResult = gson.toJson(response.body().get("diaryList"));
                        diarylist = gson.fromJson(jsonResult, type);
                        sendFragmentsData(diarylist.get(index));

                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
    }

    // 데이터 새로고침
    private void sendFragmentsData(UserDiary diary) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int index = 0; index < fragments.size(); index++) {
            Fragment fragment = fragments.get(index);
            if (fragment instanceof SituationFragment) {
                ((SituationFragment) fragment).refreshData(diary);
                // 재새팅
                situation = diary.getSituation();
            } else if (fragment instanceof ThoughtFragment) {
                ((ThoughtFragment) fragment).refreshData(diary);
                // 재새팅
                thought = diary.getThought();
            } else if (fragment instanceof EmotionFragment) {
                ((EmotionFragment) fragment).refreshData(diary);
                // 재새팅
                emotion = diary.getEmotion();
                emotionText = diary.getEmotionDescription();
            } else if (fragment instanceof ReflectionFragment) {
                ((ReflectionFragment) fragment).refreshData(diary);
                // 재새팅
                reflection = diary.getReflection();
            }
        }
    }


    // viewPager2 어뎁터
    public class ViewPager2Adapter extends FragmentStateAdapter {

        public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                // 데이터 통째로 보내기
                case 0:
                    return SituationFragment.newInstance(getIntent().getExtras());
                case 1:
                    return ThoughtFragment.newInstance(getIntent().getExtras());
                case 2:
                    return EmotionFragment.newInstance(getIntent().getExtras());
                case 3:
                    return ReflectionFragment.newInstance(getIntent().getExtras());
            }
            return SituationFragment.newInstance(getIntent().getExtras());
        }

        @Override
        public int getItemCount() {
            return tabs.length;
        }
    }
}


