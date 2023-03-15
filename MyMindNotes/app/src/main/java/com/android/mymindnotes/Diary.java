package com.android.mymindnotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityDiaryBinding;
import com.android.mymindnotes.databinding.DiaryitemBinding;
import com.android.mymindnotes.model.UserDiary;
import com.android.mymindnotes.model.retrofit.GetDiaryListApi;
import com.android.mymindnotes.data.retrofit.RetrofitService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Diary extends AppCompatActivity {
    ActivityDiaryBinding binding;

    // 오리지날 리스트와 리사이클러뷰, 어뎁터
    ArrayList<UserDiary> recordList;
    static RecyclerView diaryView;
    DiaryAdaptor adaptor;

    SharedPreferences userindex;

    // 날짜를 위한 변수
    String date;

    // 감정 동그라미
    int emotionColorNumber;

    // 정렬을 위한 리스트
    ArrayList<UserDiary> emotionRecordList;
    ArrayList<UserDiary> traumaRecordList;
    ArrayList<UserDiary> singleEmotionList;

    // 위의 리스트의 요소들이 오리지날 리스트의 어떤 인덱스에 위치했었는지를 저장해주기 위한 리스트(다음 화면으로 올바른 인덱스를 넘겨주어서 오리지날 리스트를 수정할 수 있도록)
    ArrayList<Integer> indexListEmotion;
    ArrayList<Integer> indexListTrauma;
    ArrayList<Integer> indexListSingleEmotion;

    // onResume()으로 화면으로 돌아왔을 때 어떤 화면에서 돌아왔으며, 어떤 화면을 표시해줄 것인지 구분하기 위한 리스트
    Boolean isEmotionRecordListChecked = false;
    Boolean isTraumaRecordListChecked = false;
    Boolean isSingleEmotionListChecked = false;

    // 감정별 정렬을 위한 변수들
    String[] emotionArray;
    String singleEmotion;


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


    // 다시 화면으로 돌아왔을 때 데이터 업데이트 시켜주기
    @Override
    public void onResume() {
        super.onResume();

        // 일기 수정 화면이 아닌 다른 화면에서 돌아오면 일어나는 null exception 대비
        if (recordList != null) {
            refreshDiary();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String emotion = data.getStringExtra("emotion");
                sortbyemotion(emotion);
                binding.sortEmotionButton.setText(emotion);
                binding.sortEmotionButton.setTextSize((float) (standardSize_X / 30));

            }
        }
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        indexListEmotion = new ArrayList<>();
        indexListTrauma = new ArrayList<>();
        indexListSingleEmotion = new ArrayList<>();

        emotionArray = getResources().getStringArray(R.array.emotions_array);

        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground4).into(binding.background);

        // 글짜 크기 조절
        getStandardSize();
        binding.sortDateButton.setTextSize((float) (standardSize_X / 32));
        binding.sortEmotionButton.setTextSize((float) (standardSize_X / 32));
        binding.sortEmotionDiaryButton.setTextSize((float) (standardSize_X / 32));
        binding.sortTraumaButton.setTextSize((float) (standardSize_X / 32));

        // 일기 목록 가져오기
        getDiaryList();


        // RecyclerView
        diaryView = binding.diaryView;
        // RecyclerView에 LayoutManager 적용 (기본을 최근순)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        diaryView.setLayoutManager(linearLayoutManager);


        // 날짜별 최신순/오래된순 정렬
            binding.sortDateButton.setOnClickListener(view -> {
                binding.sortEmotionButton.setText("감정별");
                isEmotionRecordListChecked = false;
                isTraumaRecordListChecked = false;
                isSingleEmotionListChecked = false;
                indexListSingleEmotion.clear();
                indexListEmotion.clear();
                indexListTrauma.clear();

                if (binding.sortDateButton.getText().toString().equals("오래된순")) {
                    binding.sortDateButton.setText("최신순");
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    diaryView.setLayoutManager(linearLayoutManager);
                } else {
                    binding.sortDateButton.setText("오래된순");
                    linearLayoutManager.setReverseLayout(false);
                    linearLayoutManager.setStackFromEnd(false);
                    diaryView.setLayoutManager(linearLayoutManager);
                }
                adaptor.updateItemList(recordList);
            });


        // 마음일기 모음
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

                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
                // 옆에 최신순/오래된순 버튼의 텍스트에 따라서 All 클릭 시에 오리지널 리스트 일기 정렬되기 - 화면이 중간지점부터가 아닌 가장 윗쪽으로 스크롤 된 상태로 뜨게 하기 위한 조치
                if (binding.sortDateButton.getText().equals("오래된순")) {
                    linearLayoutManager1.setReverseLayout(false);
                    linearLayoutManager1.setStackFromEnd(false);
                } else if (binding.sortDateButton.getText().equals("최신순")) {
                    linearLayoutManager1.setReverseLayout(true);
                    linearLayoutManager1.setStackFromEnd(true);
                }
                diaryView.setLayoutManager(linearLayoutManager1);

            });



        // 트라우마 일기 모음
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
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
                // 옆에 최신순/오래된순 버튼의 텍스트에 따라서 All 클릭 시에 오리지널 리스트 일기 정렬되기 - 화면이 중간지점부터가 아닌 가장 윗쪽으로 스크롤 된 상태로 뜨게 하기 위한 조치
                if (binding.sortDateButton.getText().equals("오래된순")) {
                    linearLayoutManager1.setReverseLayout(false);
                    linearLayoutManager1.setStackFromEnd(false);
                } else if (binding.sortDateButton.getText().equals("최신순")) {
                    linearLayoutManager1.setReverseLayout(true);
                    linearLayoutManager1.setStackFromEnd(true);
                }
                diaryView.setLayoutManager(linearLayoutManager1);

            });



        // 감정별 정렬 (Activity 팝업창 띄우기)
        binding.sortEmotionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, EmotionSortingPopup.class);
            startActivityForResult(intent, 1);
        });


    }

    // 감정별 정렬을 위한 함수
    private void sortbyemotion(String emotion) {
        isEmotionRecordListChecked = false;
        isTraumaRecordListChecked = false;
        isSingleEmotionListChecked = true;

        singleEmotionList = new ArrayList<>();

        indexListTrauma.clear();
        indexListEmotion.clear();
        indexListSingleEmotion.clear();

        singleEmotion = emotion;

        for (int i = 0; i < recordList.size(); i++) {
            if (recordList.get(i).getEmotion().equals(emotion)) {
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
        private ArrayList<UserDiary> recordList;

        public DiaryAdaptor(ArrayList<UserDiary> arrayList) {
            this.recordList = arrayList;
        }

        // 데이터셋을 업데이트하기 위한 메서드
        public void updateItemList(ArrayList<UserDiary> arrayList) {
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
            // 글짜 크기 조절
            getStandardSize();
            // 감정 아이콘 세팅
            String emotion = recordList.get(position).getEmotion();
            switch (emotion) {
                case "기쁨":
                    emotionColorNumber = R.drawable.orange_happiness;
                    break;
                case "기대":
                    emotionColorNumber = R.drawable.green_anticipation;
                    break;
                case "신뢰":
                    emotionColorNumber = R.drawable.darkblue_trust;
                    break;
                case "놀람":
                    emotionColorNumber = R.drawable.yellow_surprise;
                    break;
                case "슬픔":
                    emotionColorNumber = R.drawable.grey_sadness;
                    break;
                case "혐오":
                    emotionColorNumber = R.drawable.brown_disgust;
                    break;
                case "공포":
                    emotionColorNumber = R.drawable.black_fear;
                    break;
                case "분노":
                    emotionColorNumber = R.drawable.red_anger;
                    break;
                default:
                    emotionColorNumber = R.drawable.purple_etc;
                    break;
            }
            viewHolder.binding.emotionCircle.setImageResource(emotionColorNumber);

            // 날짜 세팅
            date = recordList.get(position).getDate() + " " + recordList.get(position).getDay();
            viewHolder.binding.date.setText(date);
            viewHolder.binding.date.setTextSize((float) (standardSize_X / 25));

            // 감정 이름 세팅
            viewHolder.binding.emotionWord.setText(emotion);
            viewHolder.binding.emotionWord.setTextSize((float) (standardSize_X / 24.5));

            // 상황 세팅
            String situation = recordList.get(position).getSituation();
            viewHolder.binding.situation.setText(situation);
            viewHolder.binding.situation.setTextSize((float) (standardSize_X / 25));

            // 타입 세팅
            String type = recordList.get(position).getType();
            viewHolder.binding.type.setText(type);
            viewHolder.binding.type.setTextSize((float) (standardSize_X / 25));

            viewHolder.binding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), Diary_Result.class);
                intent.putExtra("type", recordList.get(position).getType());
                intent.putExtra("date", recordList.get(position).getDate() + " " + recordList.get(position).getDay());
                intent.putExtra("situation", recordList.get(position).getSituation());
                intent.putExtra("thought", recordList.get(position).getThought());
                intent.putExtra("emotion", recordList.get(position).getEmotion());
                intent.putExtra("emotionText", recordList.get(position).getEmotionDescription());
                intent.putExtra("reflection", recordList.get(position).getReflection());
                intent.putExtra("diaryNumber", recordList.get(position).getDiary_number());

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

    // 네트워크 통신: 일기 목록 가져와서 어뎁터에 넘겨주기
    public void getDiaryList() {
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
                        recordList = gson.fromJson(jsonResult, type);
                        // 어레이리스트를 매개변수로 넘겨주어서 어뎁터 객체를 생성
                        adaptor = new DiaryAdaptor(recordList);
                        // 생성한 어뎁터 객체를 RecyclerView에 적용
                        diaryView.setAdapter(adaptor);
                        // 생성한 itemDecoration 객체를 RecyclerView에 적용
                        diaryView.addItemDecoration(new DiaryRecyclerViewDecoration());
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
    }


    // 네트워크 통신: 일기 목록 가져와서 업데이트된 일기 목록 화면에 보이게 하기
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
                        recordList = gson.fromJson(jsonResult, type);

                        // 최신순/오래된순에서 수정 후 돌아왔을 때 최신순/오래된순 정렬 유지
                        if (binding.sortDateButton.getText().toString().equals("최신순")) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            linearLayoutManager.setReverseLayout(true);
                            linearLayoutManager.setStackFromEnd(true);
                            diaryView.setLayoutManager(linearLayoutManager);
                        } else {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            linearLayoutManager.setReverseLayout(false);
                            linearLayoutManager.setStackFromEnd(false);
                            diaryView.setLayoutManager(linearLayoutManager);
                        }
                        adaptor.updateItemList(recordList);


                        // 만약 마음일기 모음에서 클릭이나 수정 후 돌아온 거라면
                        if (isEmotionRecordListChecked) {
                            emotionRecordList = new ArrayList<>();
                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).type.equals("오늘의 마음 일기")) {
                                    emotionRecordList.add(recordList.get(i));
                                }
                            }
                            adaptor.updateItemList(emotionRecordList);
                        }


                        // 만약 트라우마일기 모음에서 클릭이나 수정 후 돌아온 거라면
                        if (isTraumaRecordListChecked) {
                            traumaRecordList = new ArrayList<>();
                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).type.equals("트라우마 일기")) {
                                    traumaRecordList.add(recordList.get(i));
                                }
                            }
                            adaptor.updateItemList(traumaRecordList);
                        }


                        // 감정별 정렬에서 클릭이나 수정 후 돌아온 거라면
                        if (isSingleEmotionListChecked) {
                            singleEmotionList = new ArrayList<>();
                            for (int i = 0; i < recordList.size(); i++) {
                                if (recordList.get(i).getEmotion().equals(singleEmotion)) {
                                    singleEmotionList.add(recordList.get(i));
                                }
                            }
                            adaptor.updateItemList(singleEmotionList);
                        }

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
