package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mymindnotes.databinding.ActivityDiaryBinding;
import com.android.mymindnotes.databinding.DiaryitemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Diary extends AppCompatActivity {
    ActivityDiaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground).into(binding.background);

//        // 일기 내용 클릭 시 상세 페이지 보기
//        binding.recordClickButton.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), Record_Result.class);
//            startActivity(intent);
//        });

        List<String> emotionWordList = new ArrayList<>(Arrays.asList("기쁨", "슬픔", "기쁨", "슬픔", "기쁨", "슬픔"));
        List<Integer> emotionCircleList = new ArrayList<>(Arrays.asList(R.drawable.orange_happiness, R.drawable.grey_sadness, R.drawable.orange_happiness, R.drawable.grey_sadness, R.drawable.orange_happiness, R.drawable.grey_sadness));
        List<String> dateList = new ArrayList<>(Arrays.asList("0000.00.00 월요일", "0000.00.00 화요일", "0000.00.00 월요일", "0000.00.00 화요일", "0000.00.00 월요일", "0000.00.00 화요일"));
        List<String> situationList = new ArrayList<>(Arrays.asList("상황 묘사", "상황 묘사2", "상황 묘사", "상황 묘사2", "상황 묘사", "상황 묘사2"));

        binding.diaryView.setLayoutManager(new LinearLayoutManager(this));
        binding.diaryView.setAdapter(new DiaryAdaptor(emotionWordList, emotionCircleList, dateList, situationList));
        binding.diaryView.addItemDecoration(new DiaryRecyclerViewDecoration());

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        DiaryitemBinding binding;

        ViewHolder(DiaryitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class DiaryAdaptor extends RecyclerView.Adapter<ViewHolder> {
        // 항목 구성 데이터
        private List<String> emotionWordList;
        private List<Integer> emotionCircleList;
        private List<String> dateList;
        private List<String> situationList;

        public DiaryAdaptor(List<String> emotionWordList, List<Integer> emotionCircleList, List<String> dateList, List<String> situationList) {
            this.emotionWordList = emotionWordList;
            this.emotionCircleList = emotionCircleList;
            this.dateList = dateList;
            this.situationList = situationList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGorup, int i) {
            DiaryitemBinding binding = DiaryitemBinding.inflate(LayoutInflater.from(viewGorup.getContext()), viewGorup, false);
            return new ViewHolder(binding);
        }

        // 항목 구성하기 위해서 자동 콜된다: 항목이 x개면 x번 호출된다
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // 감정 아이콘 세팅
            Integer emotionCircle = emotionCircleList.get(position);
            viewHolder.binding.emotionCircle.setImageResource(emotionCircle);

            // 날짜 세팅
            String date = dateList.get(position);
            viewHolder.binding.date.setText(date);

            // 감정 이름 세팅
            String emotion = emotionWordList.get(position);
            viewHolder.binding.emotionWord.setText(emotion);

            // 상황 세팅
            String situation = situationList.get(position);
            viewHolder.binding.situation.setText(situation);

        }

        @Override
        public int getItemCount() {
            return emotionCircleList.size();
        }
    }

    class DiaryRecyclerViewDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.set(00, 0, 0, 30);

        }
    }


    // xml로 작성한 액션바의 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_diary, menu);

        return true;
    }

}