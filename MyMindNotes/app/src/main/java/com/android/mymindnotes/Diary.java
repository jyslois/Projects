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
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Diary extends AppCompatActivity {
    ActivityDiaryBinding binding;

    SharedPreferences arrayList;
    SharedPreferences.Editor arrayListEdit;

    ArrayList<Record> recordList;
    static RecyclerView diaryView;
    DiaryAdaptor adaptor;

    String getTime;

    ArrayList<Record> emotionRecordList;
    Boolean isEmotionRecordListChecked = false;
    ArrayList<Record> traumaRecordList;
    Boolean isTraumaRecordListChecked = false;


    // 다시 화면으로 돌아왔을 때 데이터 업데이트 시켜주기
    @Override
    public void onResume() {
        super.onResume();

        // 수정된 내용 적용
        Gson gson = new Gson();
        String json = arrayList.getString("arrayList", "");
        Type type = new TypeToken<ArrayList<Record>>() {
        }.getType();
        recordList = gson.fromJson(json, type);


        // 최신순/오래된순에서 수정 후 돌아왔을 때 최신순/오래된순 정렬 유지
        if (!arrayList.getString("arrayList", "").equals("")) {
                if (binding.sortDateButton.getText().toString().equals("최신순")) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    diaryView.setLayoutManager(linearLayoutManager);
                } else {
                    Collections.sort(recordList, Record.DateOldComparator);
                }
            adaptor.updateItemList(recordList);
            }


        // 만약 감정일기 모음에서 클릭이나 수정 후 돌아온 거라면
        if (!arrayList.getString("arrayList", "").equals("")) {
            if (isEmotionRecordListChecked) {
                emotionRecordList = new ArrayList<>();
                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).type.equals("오늘의 마음 일기")) {
                        emotionRecordList.add(recordList.get(i));
                    }
                }
                adaptor.updateItemList(emotionRecordList);
                }
        }

        // 만약 트라우마일기 모음에서 클릭이나 수정 후 돌아온 거라면
        if (!arrayList.getString("arrayList", "").equals("")) {
            if (isTraumaRecordListChecked) {
                traumaRecordList = new ArrayList<>();
                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).type.equals("트라우마 일기")) {
                        traumaRecordList.add(recordList.get(i));
                    }
                }
                adaptor.updateItemList(traumaRecordList);
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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


        // 날짜별 최신순/오래된순 정렬
        if (!arrayList.getString("arrayList", "").equals("")) {
            binding.sortDateButton.setOnClickListener(view -> {
                isEmotionRecordListChecked = false;
                isTraumaRecordListChecked = false;
                adaptor.updateItemList(recordList);
                if (binding.sortDateButton.getText().toString().equals("오래된순")) {
                    binding.sortDateButton.setText("최신순");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    diaryView.setLayoutManager(linearLayoutManager);
                } else {
                    binding.sortDateButton.setText("오래된순");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                    linearLayoutManager.setReverseLayout(false);
                    linearLayoutManager.setStackFromEnd(false);
                    diaryView.setLayoutManager(linearLayoutManager);
                }
                adaptor.updateItemList(recordList);
            });
        }

        // 감정일기 모음
        if (!arrayList.getString("arrayList", "").equals("")) {
            binding.sortEmotionDiaryButton.setOnClickListener(view -> {
                isEmotionRecordListChecked = true;
                isTraumaRecordListChecked = false;
                emotionRecordList = new ArrayList<>();

                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).type.equals("오늘의 마음 일기")) {
                        emotionRecordList.add(recordList.get(i));
                    }
                }

                adaptor.updateItemList(emotionRecordList);
            });
        }


        // 트라우마 일기 모음
        if (!arrayList.getString("arrayList", "").equals("")) {
            binding.sortTraumaButton.setOnClickListener(view -> {
                isEmotionRecordListChecked = false;
                isTraumaRecordListChecked = true;
                traumaRecordList = new ArrayList<>();

                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).type.equals("트라우마 일기")) {
                        traumaRecordList.add(recordList.get(i));
                    }
                }
                adaptor.updateItemList(traumaRecordList);
            });
        }


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

        public DiaryAdaptor(ArrayList<Record> arrayList) {
            this.recordList = arrayList;
        }

        // 데이터셋을 업데이트하기 위한 메서드 (onResume)에서 사용
        public void updateItemList(ArrayList<Record> arrayList) {
            this.recordList = arrayList;
            notifyDataSetChanged();
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
            Date date = recordList.get(position).date;
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd E요일");
            getTime = mFormat.format(date);
            viewHolder.binding.date.setText(getTime);

            // 감정 이름 세팅
            String emotion = recordList.get(position).emotionWord;
            viewHolder.binding.emotionWord.setText(emotion);

            // 상황 세팅
            String situation = recordList.get(position).situation;
            viewHolder.binding.situation.setText(situation);

            // 타입 세팅
            String type = recordList.get(position).type;
            viewHolder.binding.type.setText(type);

            viewHolder.binding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), Diary_Result.class);
                intent.putExtra("type", recordList.get(position).type);
                intent.putExtra("date", getTime);
                intent.putExtra("situation", recordList.get(position).situation);
                intent.putExtra("thought", recordList.get(position).thought);
                intent.putExtra("emotion", recordList.get(position).emotionWord);
                intent.putExtra("emotionText", recordList.get(position).emotionText);
                intent.putExtra("reflection", recordList.get(position).reflection);
                intent.putExtra("index", position);
                startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            if (recordList == null) {
                return 0;
            }
            return recordList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_diary, menu);

        return true;
    }

}
