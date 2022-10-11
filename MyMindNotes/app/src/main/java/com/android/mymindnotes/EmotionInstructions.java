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

        ArrayList<Emotion> emotionList = new ArrayList<>();
        emotionList.add(new Emotion("기쁨", R.drawable.orange_happiness, R.string.happiness));
        emotionList.add(new Emotion("기대", R.drawable.green_anticipation, R.string.anticipation));
        emotionList.add(new Emotion("신뢰", R.drawable.darkblue_trust, R.string.trust));
        emotionList.add(new Emotion("놀람", R.drawable.yellow_surprise, R.string.surprise));
        emotionList.add(new Emotion("슬픔", R.drawable.grey_sadness, R.string.sadness));
        emotionList.add(new Emotion("혐오", R.drawable.brown_disgust, R.string.disgust));
        emotionList.add(new Emotion("공포", R.drawable.black_fear, R.string.fear));
        emotionList.add(new Emotion("분노", R.drawable.red_anger, R.string.anger));

        binding.emotionInstructionView.setLayoutManager(new LinearLayoutManager(this));
        binding.emotionInstructionView.setAdapter(new EmotionInstructionAdaptor(emotionList));
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
}

class EmotionRecyclerViewDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(00, 0, 0, 60);

    }
}
