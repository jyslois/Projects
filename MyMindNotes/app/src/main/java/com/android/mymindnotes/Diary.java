package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    static RecyclerView diaryView;
    DiaryAdaptor adaptor;


    // 다시 화면으로 돌아왔을 때 데이터 업데이트 시켜주기
    @Override
    public void onResume() {
        super.onResume();

        Gson gson = new Gson();
        String json = arrayList.getString("arrayList", "");
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        recordList = gson.fromJson(json, type);

        adaptor.updateItemList(recordList);
    }


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

        diaryView = binding.diaryView;
        diaryView.setLayoutManager(new LinearLayoutManager(this));
        // recordList에 어뎁터 연결
        adaptor = new DiaryAdaptor(recordList);
        diaryView.setAdapter(adaptor);
        diaryView.addItemDecoration(new DiaryRecyclerViewDecoration());

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        DiaryitemBinding binding;

        ViewHolder(DiaryitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            //아이템 클릭 시
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //존재하는 포지션인지 확인
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(getApplicationContext(), Diary_Result.class);
                        intent.putExtra("type", recordList.get(pos).type);
                        intent.putExtra("date", recordList.get(pos).date);
                        intent.putExtra("situation", recordList.get(pos).situation);
                        intent.putExtra("thought", recordList.get(pos).thought);
                        intent.putExtra("emotion", recordList.get(pos).emotionWord);
                        intent.putExtra("emotionText", recordList.get(pos).emotionText);
                        intent.putExtra("reflection", recordList.get(pos).reflection);
                        intent.putExtra("index", pos);
                        startActivity(intent);
                    }
                }
            });
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
            if (recordList == null) {
                return 0;
            }
            return recordList.size();
        }

        // 데이터셋을 업데이트하기 위한 메서드 (onResume)에서 사용
        public void updateItemList(ArrayList<Record> recordList) {
            this.recordList = recordList;
            notifyDataSetChanged();
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), MainPage.class);
//        startActivity(intent);
//    }

}