package com.android.mymindnotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.android.mymindnotes.databinding.ActivityEmotionInstructionsBinding;
import com.android.mymindnotes.databinding.EmotionitemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmotionInstructions extends AppCompatActivity {
    ActivityEmotionInstructionsBinding binding;
    Spinner spinner;
    String[] emotionArray;
    EmotionInstructionAdaptor adaptor;
    ArrayList<Emotion> emotionList;
    ArrayList<Emotion> singleEmotion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmotionInstructionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground5image).into(binding.background);

        emotionArray = getResources().getStringArray(R.array.emotions_array_forinstruction);

        emotionList = new ArrayList<>();
        singleEmotion = new ArrayList<>();
        emotionList.add(new Emotion("기쁨", R.drawable.orange_happiness, R.string.happiness));
        emotionList.add(new Emotion("기대", R.drawable.green_anticipation, R.string.anticipation));
        emotionList.add(new Emotion("신뢰", R.drawable.darkblue_trust, R.string.trust));
        emotionList.add(new Emotion("놀람", R.drawable.yellow_surprise, R.string.surprise));
        emotionList.add(new Emotion("슬픔", R.drawable.grey_sadness, R.string.sadness));
        emotionList.add(new Emotion("혐오", R.drawable.brown_disgust, R.string.disgust));
        emotionList.add(new Emotion("공포", R.drawable.black_fear, R.string.fear));
        emotionList.add(new Emotion("분노", R.drawable.red_anger, R.string.anger));

        binding.emotionInstructionView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new EmotionInstructionAdaptor(emotionList);
        binding.emotionInstructionView.setAdapter(adaptor);
        binding.emotionInstructionView.addItemDecoration(new EmotionRecyclerViewDecoration());

        // 감정 정렬 버튼
        binding.sortButton.setOnClickListener(view -> {
            spinner = binding.spinner;
            // strings.xml에 있는 string array (emotions_array)를 사용해서 ArrayAdaptor 만들기
            // 이 메서드의 세 번째 인수는 선택된 항목이 스피너 컨트롤에 나타나는 방식을 정의하는 레이아웃 리소스,
            // simple_spinner_item 레이아웃은 플랫폼에서 제공. 스피너의 모양에 관해 자체적인 레이아웃을 직접 정의하고자 하지 않을 경우 사용해야 하는 기본 레이아웃.
            ArrayAdapter<CharSequence> spinnerAdaptor = ArrayAdapter.createFromResource(this, R.array.emotions_array_forinstruction, android.R.layout.simple_spinner_item);
            // 어뎁터가 스피너 선택 항목을 표시하는 데 사용해야 하는 레이아웃을 지정. simple_spinner_dropdown_item은 플랫폼에서 정의하는 표준 레이아웃.)
            spinnerAdaptor.setDropDownViewResource(R.layout.spinneritemforemotioninsturction);
            // 어뎁터를 Spinner에 적용
            spinner.setAdapter(spinnerAdaptor);
            // 버튼 길이만큼 spinner 길이 조정하기
            spinner.setDropDownWidth(binding.sortButton.getWidth());
            // 스피너가 클릭되는 것으로 처리해서 드롭다운 메뉴가 나타나게 하기
            spinner.performClick();

            // 아이템 클릭 이벤트
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    binding.sortButton.setText(emotionArray[position]);
                    switch (emotionArray[position]) {
                        case "모든 감정" : {
                            singleEmotion.clear();
                            adaptor.updateItemList(emotionList);
                        }
                        break;
                        case "기쁨" : {
                            singleEmotion.clear();
                            for (Emotion i : emotionList) {
                                if (i.emotion.equals("기쁨")) {
                                    singleEmotion.add(i);
                                }
                            }
                            adaptor.updateItemList(singleEmotion);
                        }
                        break;
                        case "기대" : {
                            singleEmotion.clear();
                            for (Emotion i : emotionList) {
                                if (i.emotion.equals("기대")) {
                                    singleEmotion.add(i);
                                }
                            }
                            adaptor.updateItemList(singleEmotion);
                        }
                        break;
                        case "신뢰" : {
                            singleEmotion.clear();
                            for (Emotion i : emotionList) {
                                if (i.emotion.equals("신뢰")) {
                                    singleEmotion.add(i);
                                }
                            }
                            adaptor.updateItemList(singleEmotion);
                        }
                        break;
                        case "놀람" : {
                            singleEmotion.clear();
                            for (Emotion i : emotionList) {
                                if (i.emotion.equals("놀람")) {
                                    singleEmotion.add(i);
                                }
                            }
                            adaptor.updateItemList(singleEmotion);
                        }
                        break;
                        case "슬픔" : {
                            singleEmotion.clear();
                            for (Emotion i : emotionList) {
                                if (i.emotion.equals("슬픔")) {
                                    singleEmotion.add(i);
                                }
                            }
                            adaptor.updateItemList(singleEmotion);
                        }
                        break;
                        case "혐오" : {
                            singleEmotion.clear();
                            for (Emotion i : emotionList) {
                                if (i.emotion.equals("혐오")) {
                                    singleEmotion.add(i);
                                }
                            }
                            adaptor.updateItemList(singleEmotion);
                        }
                        break;
                        case "공포" : {
                            singleEmotion.clear();
                            for (Emotion i : emotionList) {
                                if (i.emotion.equals("공포")) {
                                    singleEmotion.add(i);
                                }
                            }
                            adaptor.updateItemList(singleEmotion);
                        }
                        break;
                        case "분노" : {
                            singleEmotion.clear();
                            for (Emotion i : emotionList) {
                                if (i.emotion.equals("분노")) {
                                    singleEmotion.add(i);
                                }
                            }
                            adaptor.updateItemList(singleEmotion);
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

}

class ViewHolder extends RecyclerView.ViewHolder {
    EmotionitemBinding binding;

    ViewHolder(EmotionitemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

class EmotionInstructionAdaptor extends RecyclerView.Adapter<ViewHolder> {
    // 항목 구성 데이터
    private ArrayList<Emotion> emotionList;

    public EmotionInstructionAdaptor(ArrayList<Emotion> emotion) {
        this.emotionList = emotion;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGorup, int i) {
        EmotionitemBinding binding = EmotionitemBinding.inflate(LayoutInflater.from(viewGorup.getContext()), viewGorup, false);
        return new ViewHolder(binding);
    }

    // 항목 구성하기 위해서 자동 콜된다: 항목이 x개면 x번 호출된다
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // 감정 이름 세팅
        viewHolder.binding.emotion.setText("〔 " + emotionList.get(position).emotion + " 〕");

        // 감정 아이콘 세팅
        viewHolder.binding.emotionIcon.setImageResource(emotionList.get(position).emotionIcon);

        // 감정 설명 세팅
        viewHolder.binding.emotionInstruction.setText(emotionList.get(position).instruction);
        viewHolder.binding.emotionInstruction.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
    }

    @Override
    public int getItemCount() {
        return emotionList.size();
    }

    // 데이터셋을 업데이트하기 위한 메서드
    public void updateItemList(ArrayList<Emotion> arrayList) {
        this.emotionList = arrayList;
        notifyDataSetChanged();
    }

}

class EmotionRecyclerViewDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(00, 0, 0, 60);

    }
}
