package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Diary extends AppCompatActivity {
    ActivityDiaryBinding binding;

    SharedPreferences reflection;
    SharedPreferences type;
    SharedPreferences dates;
    SharedPreferences emotion;
    SharedPreferences emotionText;
    SharedPreferences situation;
    SharedPreferences thought;
    SharedPreferences emotionColor;

    SharedPreferences arrayList;
    SharedPreferences.Editor arrayListEdit;

    ArrayList<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emotion = getSharedPreferences("emotion", MODE_PRIVATE);
        emotionText = getSharedPreferences("emotionText", MODE_PRIVATE);
        emotionColor = getSharedPreferences("emotionColor", MODE_PRIVATE);
        situation = getSharedPreferences("situation", MODE_PRIVATE);
        thought = getSharedPreferences("thought", MODE_PRIVATE);
        reflection = getSharedPreferences("reflection", MODE_PRIVATE);
        type = getSharedPreferences("type", MODE_PRIVATE);
        dates = getSharedPreferences("date", MODE_PRIVATE);

        arrayList = getSharedPreferences("recordList", MODE_PRIVATE);
        arrayListEdit = arrayList.edit();

        // 만약 SharedPreferences에 저장된 arrayList가 있다면,
        if (!arrayList.getString("arrayList", "").equals("")) {
            // SharedPreferences에 저장된 arrayList를 recordList로 가져오기
            Gson gson = new Gson();
            String json = arrayList.getString("arrayList", "");
            Type type = new TypeToken<ArrayList<Record>>() {
            }.getType();
            recordList = gson.fromJson(json, type);
        } else {
            // 없다면 새 recordList 만들기 (이 경우, 빈 리스트가 화면에 뿌려져서 화면엔 아무것도 나오지 않는다)
            recordList = new ArrayList<>();
        }


        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground).into(binding.background);

        binding.diaryView.setLayoutManager(new LinearLayoutManager(this));
        // recordList에 어뎁터 연결
        binding.diaryView.setAdapter(new DiaryAdaptor(recordList));
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
        private ArrayList<Record> recordList;

        public DiaryAdaptor(ArrayList<Record> recordList) {
            this.recordList = recordList;
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
            int emotionCircle = recordList.get(position).emotionCircle;
            viewHolder.binding.emotionCircle.setImageResource(emotionCircle);

            // 날짜 세팅
            String date = recordList.get(position).date;
            viewHolder.binding.date.setText(date);

            // 감정 이름 세팅
            String emotion = recordList.get(position).emotionWord;
            viewHolder.binding.emotionWord.setText(emotion);

            // 상황 세팅
            String situation = recordList.get(position).situation;
            viewHolder.binding.situation.setText(situation);

            // 타입 세팅
            String type = recordList.get(position).type;
            viewHolder.binding.type.setText(type);

        }

        @Override
        public int getItemCount() {
            return recordList.size();
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