package com.android.mymindnotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mymindnotes.databinding.ActivityEmotionInstructionsBinding;
import com.android.mymindnotes.databinding.EmotionitemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmotionInstructions extends AppCompatActivity {
    ActivityEmotionInstructionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmotionInstructionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<String> emotionList = new ArrayList<>(Arrays.asList("기쁨", "기대", "신뢰", "놀람", "슬픔", "혐오", "공포", "분노"));
        List<Integer> emotionIconList = new ArrayList<>(Arrays.asList(R.drawable.orange_happiness, R.drawable.green_anticipation, R.drawable.darkblue_trust, R.drawable.yellow_surprise, R.drawable.grey_sadness, R.drawable.brown_disgust, R.drawable.black_fear, R.drawable.red_anger));
        List<Integer> InstructionList = new ArrayList<>(Arrays.asList(R.string.happiness, R.string.anticipation, R.string.trust, R.string.surprise, R.string.sadness, R.string.disgust, R.string.fear, R.string.anger));

        binding.emotionInstructionView.setLayoutManager(new LinearLayoutManager(this));
        binding.emotionInstructionView.setAdapter(new EmotionInstructionAdaptor(emotionList, emotionIconList, InstructionList));
        binding.emotionInstructionView.addItemDecoration(new EmotionRecyclerViewDecoration());
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
    private List<String> emotionList;
    private List<Integer> emotionIconList;
    private List<Integer> InstructionList;

    public EmotionInstructionAdaptor(List<String> emotionList, List<Integer> emotionIconList, List<Integer> InstructionList) {
        this.emotionList = emotionList;
        this.emotionIconList = emotionIconList;
        this.InstructionList = InstructionList;
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
        String emotion = emotionList.get(position);
        viewHolder.binding.emotion.setText("〔 " + emotion + " 〕");

        // 감정 아이콘 세팅
        Integer emotionIcon = emotionIconList.get(position);
        viewHolder.binding.emotionIcon.setImageResource(emotionIcon);

        // 감정 설명 세팅
        Integer instruction = InstructionList.get(position);
        viewHolder.binding.emotionInstruction.setText(instruction);
        viewHolder.binding.emotionInstruction.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
    }

    @Override
    public int getItemCount() {
        return emotionList.size();
    }
}

class EmotionRecyclerViewDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(00, 0, 0, 60);

    }
}
