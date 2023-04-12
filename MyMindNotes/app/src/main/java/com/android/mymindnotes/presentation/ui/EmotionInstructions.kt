package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import android.os.Build
import android.graphics.text.LineBreaker
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.android.mymindnotes.R
import com.android.mymindnotes.databinding.ActivityEmotionInstructionsBinding
import com.android.mymindnotes.databinding.EmotionitemBinding
import com.android.mymindnotes.presentation.viewmodels.EmotionInstructionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmotionInstructions : AppCompatActivity() {
    private lateinit var binding: ActivityEmotionInstructionsBinding

    // 뷰모델 객체 주입
    private val viewModel: EmotionInstructionsViewModel by viewModels()

    private lateinit var adaptor: EmotionInstructionAdaptor
    private var emotionList = listOf<Emotion>()
    private var singleEmotion = mutableListOf<Emotion>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmotionInstructionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground5).into(binding.background)

        // 리스트 만들기
        emotionList = listOf(
            Emotion("기쁨", R.drawable.orange_happiness, R.string.happiness),
            Emotion("기대", R.drawable.green_anticipation, R.string.anticipation),
            Emotion("신뢰", R.drawable.darkblue_trust, R.string.trust),
            Emotion("놀람", R.drawable.yellow_surprise, R.string.surprise),
            Emotion("슬픔", R.drawable.grey_sadness, R.string.sadness),
            Emotion("혐오", R.drawable.brown_disgust, R.string.disgust),
            Emotion("공포", R.drawable.black_fear, R.string.fear),
            Emotion("분노", R.drawable.red_anger, R.string.anger)
        )

        // RecycerView 세팅
        adaptor = EmotionInstructionAdaptor(emotionList)
        binding.emotionInstructionView.adapter = adaptor
        binding.emotionInstructionView.layoutManager = LinearLayoutManager(this)
        binding.emotionInstructionView.addItemDecoration(EmotionRecyclerViewDecoration())


        // 감정 정렬 버튼 클릭 이벤트
        binding.sortButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickSortButton()
            }
        }

        // 감정 정렬 버튼 클릭 감지
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sortButton.collect {
                    val intent =
                        Intent(this@EmotionInstructions, EmotionInstructionSortingPopup::class.java)
                    startActivityForResult(intent, 1)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val emotion = data!!.getStringExtra("emotion")
            binding.sortButton.text = emotion

            singleEmotion.clear()

            if (emotion == "모든 감정") {
                singleEmotion.addAll(emotionList)
            } else {
                singleEmotion.addAll(emotionList.filter { it.emotion == emotion })
            }

            adaptor.updateItemList(singleEmotion)
        }
    }
}

class ViewHolder(var binding: EmotionitemBinding) : RecyclerView.ViewHolder(binding.root)

class EmotionInstructionAdaptor(  // 항목 구성 데이터
    private var emotionList: List<Emotion>?
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(viewGorup: ViewGroup, i: Int): ViewHolder {
        val binding =
            EmotionitemBinding.inflate(LayoutInflater.from(viewGorup.context), viewGorup, false)
        return ViewHolder(binding)
    }

    // 항목 구성하기 위해서 자동 콜된다: 항목이 x개면 x번 호출된다
    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // 감정 이름 세팅
        viewHolder.binding.emotion.text = emotionList!![position].emotion
        // 감정 아이콘 세팅
        viewHolder.binding.emotionIcon.setImageResource(emotionList!![position].emotionIcon)

        // 감정 설명 세팅
        viewHolder.binding.emotionInstruction.setText(emotionList!![position].instruction)
        viewHolder.binding.emotionInstruction.justificationMode =
            LineBreaker.JUSTIFICATION_MODE_INTER_WORD
    }

    override fun getItemCount(): Int {
        return emotionList!!.size
    }

    // 데이터셋을 업데이트하기 위한 메서드
    fun updateItemList(arrayList: List<Emotion>?) {
        emotionList = arrayList
        notifyDataSetChanged()
    }
}

internal class EmotionRecyclerViewDecoration : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[0, 0, 0] = 60
    }
}