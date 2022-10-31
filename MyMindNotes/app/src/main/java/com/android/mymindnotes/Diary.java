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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.mymindnotes.databinding.ActivityDiaryBinding;
import com.android.mymindnotes.databinding.DiaryitemBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    ArrayList<Record> singleEmotionList;
    Boolean isSingleEmotionListChecked = false;

    ArrayList<Integer> indexListEmotion;
    ArrayList<Integer> indexListTrauma;
    ArrayList<Integer> indexListSingleEmotion;

    Spinner spinner;
    String[] emotionArray;
    String singleEmotion;

    Date date;


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
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                    linearLayoutManager.setReverseLayout(false);
                    linearLayoutManager.setStackFromEnd(false);
                    diaryView.setLayoutManager(linearLayoutManager);
                }
            adaptor.updateItemList(recordList);
        }


        // 만약 마음일기 모음에서 클릭이나 수정 후 돌아온 거라면
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

        // 감정별 정렬에서 클릭이나 수정 후 돌아온 거라면
        if (!arrayList.getString("arrayList", "").equals("")) {
            if (isSingleEmotionListChecked) {
                singleEmotionList = new ArrayList<>();
                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).emotionWord.equals(singleEmotion)) {
                        singleEmotionList.add(recordList.get(i));
                    }
                }
                adaptor.updateItemList(singleEmotionList);
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

        indexListEmotion = new ArrayList<>();
        indexListTrauma = new ArrayList<>();
        indexListSingleEmotion = new ArrayList<>();

        emotionArray = getResources().getStringArray(R.array.emotions_array);

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
                binding.sortEmotionButton.setText("감정별");
                isEmotionRecordListChecked = false;
                isTraumaRecordListChecked = false;
                isSingleEmotionListChecked = false;
                indexListSingleEmotion.clear();
                indexListEmotion.clear();
                indexListTrauma.clear();
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

        // 마음일기 모음
        if (!arrayList.getString("arrayList", "").equals("")) {
            binding.sortEmotionDiaryButton.setOnClickListener(view -> {
                binding.sortEmotionButton.setText("감정별");
                isEmotionRecordListChecked = true;
                isTraumaRecordListChecked = false;
                isSingleEmotionListChecked = false;
                indexListSingleEmotion.clear();
                indexListTrauma.clear();
                emotionRecordList = new ArrayList<>();

                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).type.equals("오늘의 마음 일기")) {
                        emotionRecordList.add(recordList.get(i));
                        indexListEmotion.add(i);
                    }
                }

                adaptor.updateItemList(emotionRecordList);

                // 옆에 최신순/오래된순 버튼의 텍스트에 따라서 All 클릭 시에 오리지널 리스트 일기 정렬되기
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                if (binding.sortDateButton.getText().equals("오래된순")) {
                    linearLayoutManager.setReverseLayout(false);
                    linearLayoutManager.setStackFromEnd(false);
                } else if (binding.sortDateButton.getText().equals("최신순")) {
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                }
                diaryView.setLayoutManager(linearLayoutManager);
            });
        }


        // 트라우마 일기 모음
        if (!arrayList.getString("arrayList", "").equals("")) {
            binding.sortTraumaButton.setOnClickListener(view -> {
                binding.sortEmotionButton.setText("감정별");
                isTraumaRecordListChecked = true;
                isEmotionRecordListChecked = false;
                isSingleEmotionListChecked = false;
                indexListSingleEmotion.clear();
                indexListEmotion.clear();
                traumaRecordList = new ArrayList<>();

                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).type.equals("트라우마 일기")) {
                        traumaRecordList.add(recordList.get(i));
                        indexListTrauma.add(i);
                    }
                }
                adaptor.updateItemList(traumaRecordList);
            });
        }

        // 감정별 정렬
        binding.sortEmotionButton.setOnClickListener(view -> {
            spinner = binding.emotionSpinner;
            // strings.xml에 있는 string array (emotions_array)를 사용해서 ArrayAdaptor 만들기
            // 이 메서드의 세 번째 인수는 선택된 항목이 스피너 컨트롤에 나타나는 방식을 정의하는 레이아웃 리소스,
            // simple_spinner_item 레이아웃은 플랫폼에서 제공. 스피너의 모양에 관해 자체적인 레이아웃을 직접 정의하고자 하지 않을 경우 사용해야 하는 기본 레이아웃.
            ArrayAdapter<CharSequence> spinnerAdaptor = ArrayAdapter.createFromResource(this, R.array.emotions_array, android.R.layout.simple_spinner_item);
            // 어뎁터가 스피너 선택 항목을 표시하는 데 사용해야 하는 레이아웃을 지정. simple_spinner_dropdown_item은 플랫폼에서 정의하는 표준 레이아웃.)
            spinnerAdaptor.setDropDownViewResource(R.layout.spinneritem);
            // 어뎁터를 Spinner에 적용
            spinner.setAdapter(spinnerAdaptor);
            // 버튼 크기만큼 width 설정하기
            spinner.setDropDownWidth(binding.sortEmotionButton.getWidth());
            // 스피너가 클릭되는 것으로 처리해서 드롭다운 메뉴가 나타나게 하기
            spinner.performClick();

            // 아이템 클릭 이벤트
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    binding.sortEmotionButton.setText(emotionArray[position]);
                    switch (emotionArray[position]) {
                        case "All" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            adaptor.updateItemList(recordList);

                            // 옆에 최신순/오래된순 버튼의 텍스트에 따라서 All 클릭 시에 오리지널 리스트 일기 정렬되기
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            if (binding.sortDateButton.getText().equals("오래된순")) {
                                linearLayoutManager.setReverseLayout(false);
                                linearLayoutManager.setStackFromEnd(false);
                            } else if (binding.sortDateButton.getText().equals("최신순")) {
                                linearLayoutManager.setReverseLayout(true);
                                linearLayoutManager.setStackFromEnd(true);
                            }
                            diaryView.setLayoutManager(linearLayoutManager);
                        }
                        break;
                        case "기쁨" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            singleEmotionList = new ArrayList<>();

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            singleEmotion = emotionArray[position];

                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).emotionWord.equals("기쁨")) {
                                    singleEmotionList.add(recordList.get(i));
                                    indexListSingleEmotion.add(i);
                                }
                            }

                            adaptor.updateItemList(singleEmotionList);
                        }
                        break;
                        case "기대" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            singleEmotionList = new ArrayList<>();

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            singleEmotion = emotionArray[position];

                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).emotionWord.equals("기대")) {
                                    singleEmotionList.add(recordList.get(i));
                                    indexListSingleEmotion.add(i);
                                }
                            }

                            adaptor.updateItemList(singleEmotionList);
                        }
                        break;
                        case "신뢰" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            singleEmotionList = new ArrayList<>();

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            singleEmotion = emotionArray[position];

                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).emotionWord.equals("신뢰")) {
                                    singleEmotionList.add(recordList.get(i));
                                    indexListSingleEmotion.add(i);
                                }
                            }

                            adaptor.updateItemList(singleEmotionList);
                        }
                        break;
                        case "놀람" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            singleEmotionList = new ArrayList<>();

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            singleEmotion = emotionArray[position];

                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).emotionWord.equals("놀람")) {
                                    singleEmotionList.add(recordList.get(i));
                                    indexListSingleEmotion.add(i);
                                }
                            }

                            adaptor.updateItemList(singleEmotionList);
                        }
                        break;
                        case "슬픔" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            singleEmotionList = new ArrayList<>();

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            singleEmotion = emotionArray[position];

                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).emotionWord.equals("슬픔")) {
                                    singleEmotionList.add(recordList.get(i));
                                    indexListSingleEmotion.add(i);
                                }
                            }

                            adaptor.updateItemList(singleEmotionList);
                        }
                        break;
                        case "혐오" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            singleEmotionList = new ArrayList<>();

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            singleEmotion = emotionArray[position];

                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).emotionWord.equals("혐오")) {
                                    singleEmotionList.add(recordList.get(i));
                                    indexListSingleEmotion.add(i);
                                }
                            }

                            adaptor.updateItemList(singleEmotionList);
                        }
                        break;
                        case "공포" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            singleEmotionList = new ArrayList<>();

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            singleEmotion = emotionArray[position];

                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).emotionWord.equals("공포")) {
                                    singleEmotionList.add(recordList.get(i));
                                    indexListSingleEmotion.add(i);
                                }
                            }

                            adaptor.updateItemList(singleEmotionList);
                        }
                        break;
                        case "분노" : {
                            isEmotionRecordListChecked = false;
                            isTraumaRecordListChecked = false;
                            isSingleEmotionListChecked = true;

                            singleEmotionList = new ArrayList<>();

                            indexListTrauma.clear();
                            indexListEmotion.clear();
                            indexListSingleEmotion.clear();

                            singleEmotion = emotionArray[position];

                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).emotionWord.equals("분노")) {
                                    singleEmotionList.add(recordList.get(i));
                                    indexListSingleEmotion.add(i);
                                }
                            }

                            adaptor.updateItemList(singleEmotionList);
                        }
                        break;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });


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
            date = recordList.get(position).date;
            final SimpleDateFormat[] mFormat = {new SimpleDateFormat("yyyy-MM-dd E요일")};
            getTime = mFormat[0].format(date);
            viewHolder.binding.date.setText(getTime);
            Log.e("timecheck", getTime);

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
                date = recordList.get(position).date;
                mFormat[0] = new SimpleDateFormat("yyyy-MM-dd E요일");
                getTime = mFormat[0].format(date);
                intent.putExtra("date", getTime);
                intent.putExtra("situation", recordList.get(position).situation);
                intent.putExtra("thought", recordList.get(position).thought);
                intent.putExtra("emotion", recordList.get(position).emotionWord);
                intent.putExtra("emotionText", recordList.get(position).emotionText);
                intent.putExtra("reflection", recordList.get(position).reflection);

                // 오리지날 리스트의 몇 번째 인덱스에 새로운 리스트의 요소가 있나
                // 이미 새로운 리스트를 만들 때, 오리지날 리스트의 몇 번째 인덱스의 것인지를 저장해 두었다.
                // 새로운 리스트의 요소의 인덱스 = 저장된 인덱스의 순서
                // 그러므로 indexList.get(position)을 하면 해당 오리지날 리스트의 인덱스가 나온다.
                if (indexListEmotion.size() != 0) {
                    intent.putExtra("index", indexListEmotion.get(position));
                } else if (indexListTrauma.size() != 0) {
                    intent.putExtra("index", indexListTrauma.get(position));
                } else if (indexListSingleEmotion.size() != 0) {
                    intent.putExtra("index", indexListSingleEmotion.get(position));
                } else {
                    intent.putExtra("index", position);
                }

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
