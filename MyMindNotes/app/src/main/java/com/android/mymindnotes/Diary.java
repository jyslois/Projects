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

    // 오리지날 리스트와 리사이클러뷰, 어뎁터
    ArrayList<Record> recordList;
    static RecyclerView diaryView;
    DiaryAdaptor adaptor;

    // 날짜를 위한 변수
    Date date;
    String getTime;

    // 정렬을 위한 리스트
    ArrayList<Record> emotionRecordList;
    ArrayList<Record> traumaRecordList;
    ArrayList<Record> singleEmotionList;

    // 위의 리스트의 요소들이 오리지날 리스트의 어떤 인덱스에 위치했었는지를 저장해주기 위한 리스트(다음 화면으로 올바른 인덱스를 넘겨주어서 오리지날 리스트를 수정할 수 있도록)
    ArrayList<Integer> indexListEmotion;
    ArrayList<Integer> indexListTrauma;
    ArrayList<Integer> indexListSingleEmotion;

    // onResume()으로 화면으로 돌아왔을 때 어떤 화면에서 돌아왔으며, 어떤 화면을 표시해줄 것인지 구분하기 위한 리스트
    Boolean isEmotionRecordListChecked = false;
    Boolean isTraumaRecordListChecked = false;
    Boolean isSingleEmotionListChecked = false;

    // 스피너(드롭 다운 메뉴)를 위한 변수들
    Spinner spinner;
    String[] emotionArray;
    String singleEmotion;


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
        Glide.with(this).load(R.drawable.diarybackground4).into(binding.background);

        // RecyclerView
        diaryView = binding.diaryView;
        // RecyclerView에 LayoutManager 적용
        diaryView.setLayoutManager(new LinearLayoutManager(this));

        // 어레이리스트를 매개변수로 넘겨주어서 어뎁터 객체를 생성
        adaptor = new DiaryAdaptor(recordList);
        // 생성한 어뎁터 객체를 RecyclerView에 적용
        diaryView.setAdapter(adaptor);
        // 생성한 itemDecoration 객체를 RecyclerView에 적용
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

                // 옆에 최신순/오래된순 버튼의 텍스트에 따라서 All 클릭 시에 오리지널 리스트 일기 정렬되기 - 화면이 중간지점부터가 아닌 가장 윗쪽으로 스크롤 된 상태로 뜨게 하기 위한 조치
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
                    if (recordList != null) {
                        switch (emotionArray[position]) {
                            case "All": {
                                isEmotionRecordListChecked = false;
                                isTraumaRecordListChecked = false;
                                isSingleEmotionListChecked = true;

                                indexListTrauma.clear();
                                indexListEmotion.clear();
                                indexListSingleEmotion.clear();

                                adaptor.updateItemList(recordList);

                                // 옆에 최신순/오래된순 버튼의 텍스트에 따라서 All 클릭 시에 오리지널 리스트 일기 정렬되기 - 화면이 중간지점부터가 아닌 가장 윗쪽으로 스크롤 된 상태로 뜨게 하기 위한 조치
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
                            case "기쁨": {
                                sortbyemotion("기쁨", position);
                            }
                            break;
                            case "기대": {
                                sortbyemotion("기대", position);
                            }
                            break;
                            case "신뢰": {
                                sortbyemotion("신뢰", position);
                            }
                            break;
                            case "놀람": {
                                sortbyemotion("놀람", position);
                            }
                            break;
                            case "슬픔": {
                                sortbyemotion("슬픔", position);
                            }
                            break;
                            case "혐오": {
                                sortbyemotion("혐오", position);
                            }
                            break;
                            case "공포": {
                                sortbyemotion("공포", position);
                            }
                            break;
                            case "분노": {
                                sortbyemotion("분노", position);
                            }
                            break;

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

    }

    // 감정별 정렬을 위한 함수
    private void sortbyemotion(String emotion, int position) {
        isEmotionRecordListChecked = false;
        isTraumaRecordListChecked = false;
        isSingleEmotionListChecked = true;

        singleEmotionList = new ArrayList<>();

        indexListTrauma.clear();
        indexListEmotion.clear();
        indexListSingleEmotion.clear();

        singleEmotion = emotionArray[position];

        for (int i = 0; i < recordList.size(); i++) {
            if (recordList.get(i).emotionWord.equals(emotion)) {
                singleEmotionList.add(recordList.get(i));
                indexListSingleEmotion.add(i);
            }
        }

        adaptor.updateItemList(singleEmotionList);
    }

    // 목록의 개별 항목을 구성하기 위한 뷰들을 viewBinding을 통해 hold,들고 있는 역할 - 뷰를 재활용할 수 있게 해 준다
    class ViewHolder extends RecyclerView.ViewHolder {
        DiaryitemBinding binding;

        ViewHolder(DiaryitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    // viewHolder을 생성하며, 배열에 저장되어 있는 데이터와 recyclerView를 연결시켜 주어서, 데이터를 RecyclerView에서 목록으로 볼 수 있게 한다.
    class DiaryAdaptor extends RecyclerView.Adapter<ViewHolder> {
        // 항목 구성 데이터
        private ArrayList<Record> recordList;

        public DiaryAdaptor(ArrayList<Record> arrayList) {
            this.recordList = arrayList;
        }

        // 데이터셋을 업데이트하기 위한 메서드
        public void updateItemList(ArrayList<Record> arrayList) {
            this.recordList = arrayList;
            notifyDataSetChanged();
        }

        // 뷰 홀더 객체를 생성한다. 항목을 구성하기 위한 레이아웃 xml파일을 inflate한 binding객체를 ViewHolder 생성자로 넘겨주어 만들어진 viewHOlder 객체를 반환하면, 이를 메모리에 유지했다가 onBindViewHolder() 호출 시에 매개변수로 전달하는 구조이다.
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGorup, int i) {
            DiaryitemBinding binding = DiaryitemBinding.inflate(LayoutInflater.from(viewGorup.getContext()), viewGorup, false);
            // 뷰 홀더 생성
            return new ViewHolder(binding);
        }


        // 배열에 저장되어 있는 데이터와 뷰홀더의 뷰들을 연결시켜준다.
        // 각 항목 구성하기 위해서 자동 콜된다: 항목이 x개면 x번 호출된다
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

}
