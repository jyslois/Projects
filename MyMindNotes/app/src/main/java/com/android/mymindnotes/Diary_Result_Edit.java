package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.mymindnotes.databinding.ActivityDiaryResultEditBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Diary_Result_Edit extends AppCompatActivity {
    ActivityDiaryResultEditBinding binding;
    SharedPreferences arrayList;
    SharedPreferences.Editor arrayListEdit;
    ArrayList<Record> recordList;
    String type;
    String situation;
    String thought;
    String emotion;
    String emotionText;
    String reflection;
    String date;
    int index;
    int emotionColor;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryResultEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // emotion instruction button
        binding.emotioInstructionButton.setOnClickListener(view -> {
            Intent emotion = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(emotion);
        });

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground).into(binding.background);


        // 데이터 세팅
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        situation = intent.getStringExtra("situation");
        thought = intent.getStringExtra("thought");
        emotion = intent.getStringExtra("emotion");
        emotionText = intent.getStringExtra("emotionText");
        reflection = intent.getStringExtra("reflection");
        index = intent.getIntExtra("index", index);
        Log.d("index", String.valueOf(index));
        date = intent.getStringExtra("date");

        // 화면에 뿌리기
        binding.date.setText(date);
        binding.type.setText(type);
        binding.editSituation.setText(situation);
        binding.editThought.setText(thought);
        binding.editEmotion.setText(emotion);
        binding.editEmotionText.setText(emotionText);
        binding.editReflection.setText(reflection);

        // 타입에 따라 회고 입력란 힌트 다르게 설정
        if (type.equals("오늘의 마음 일기")) {
            binding.editReflection.setHint("(선택) 나는 왜 그런 마음이 들었을까요?");
        } else if (type.equals("트라우마 일기")) {
            binding.editReflection.setHint("지금의 내게 어떤 영향을 미치고 있나요?");
        }

        // 수정 (완료) 버튼
        binding.editButton.setOnClickListener(view -> {
            situation = binding.editSituation.getText().toString();
            thought = binding.editThought.getText().toString();
            emotion = binding.editEmotion.getText().toString();
            emotionText = binding.editEmotionText.getText().toString();
            reflection = binding.editReflection.getText().toString();

            // 저장된 arrayList 가져오기
            arrayList = getSharedPreferences("recordList", MODE_PRIVATE);
            arrayListEdit = arrayList.edit();

            Gson gson = new Gson();
            String json = arrayList.getString("arrayList", "");
            Type type = new TypeToken<ArrayList<Record>>() {
            }.getType();
            recordList = gson.fromJson(json, type);

            // 수정된 내용 저장
            recordList.get(index).situation = situation;
            recordList.get(index).thought = thought;
            recordList.get(index).emotionWord = emotion;
            recordList.get(index).emotionText = emotionText;
            recordList.get(index).reflection = reflection;
            // emotionColor 수정
            switch (emotion) {
                case "기쁨":
                    emotionColor = R.drawable.orange_happiness;
                    break;
                case "기대":
                    emotionColor = R.drawable.green_anticipation;
                    break;
                case "신뢰":
                    emotionColor = R.drawable.darkblue_trust;
                    break;
                case "놀람":
                    emotionColor = R.drawable.yellow_surprise;
                    break;
                case "슬픔":
                    emotionColor = R.drawable.grey_sadness;
                    break;
                case "혐오":
                    emotionColor = R.drawable.brown_disgust;
                    break;
                case "공포":
                    emotionColor = R.drawable.black_fear;
                    break;
                case "분노":
                    emotionColor = R.drawable.red_anger;
                    break;
                default:
                    emotionColor = R.drawable.purple_etc;
                    break;
            }
            recordList.get(index).emotionCircle = emotionColor;

            // 업데이트 된 recordList를 SharedPreferences에 저장
            json = gson.toJson(recordList);
            arrayListEdit.putString("arrayList", json);
            arrayListEdit.commit();

            finish();

        });
    }

    DialogInterface.OnClickListener dialogListener = (dialog, which) -> {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            finish();
        }
    };

    // 뒤로 가기 버튼 누를 시, 알람창 띄우기
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("수정한 기록이 사라져요. 정말 돌아가시겠어요?");
        builder.setNegativeButton("종료", dialogListener);
        builder.setPositiveButton("계속 수정", null);
        alertDialog = builder.create();
        alertDialog.show();
    }
}