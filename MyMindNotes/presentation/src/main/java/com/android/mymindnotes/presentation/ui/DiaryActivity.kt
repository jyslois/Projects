package com.android.mymindnotes.presentation.ui

import android.annotation.SuppressLint
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
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityDiaryBinding
import com.android.mymindnotes.presentation.databinding.DiaryitemBinding
import com.android.mymindnotes.core.model.UserDiary
import com.android.mymindnotes.presentation.viewmodels.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.ArrayList

@AndroidEntryPoint
class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding

    // 오리지날 리스트와 리사이클러뷰, 어뎁터
    var recordList: ArrayList<UserDiary>? = null
    private var adaptor: DiaryAdaptor? = null

    // 날짜를 위한 변수
    var date: String? = null

    // 감정 동그라미
    var emotionColorNumber = 0

    // 정렬을 위한 리스트
    var emotionRecordList = ArrayList<UserDiary>()
    var traumaRecordList = ArrayList<UserDiary>()
    var singleEmotionList = ArrayList<UserDiary>()

    // 위의 리스트의 요소들이 오리지날 리스트의 어떤 인덱스에 위치했었는지를 저장해주기 위한 리스트(다음 화면으로 올바른 인덱스를 넘겨주어서 오리지날 리스트를 수정할 수 있도록)
    var indexListEmotion = ArrayList<Int>()
    var indexListTrauma = ArrayList<Int>()
    var indexListSingleEmotion = ArrayList<Int>()

    // onResume()으로 화면으로 돌아왔을 때 어떤 화면에서 돌아왔으며, 어떤 화면을 표시해줄 것인지 구분하기 위한 리스트
    var isEmotionRecordListChecked = false
    var isTraumaRecordListChecked = false
    var isSingleEmotionListChecked = false

    // 감정별 정렬을 위한 변수들
    private var emotionArray: Array<String>? = null
    var singleEmotion: String? = null

    // 아이템 간격 설정 여부를 위한 변수
    var diaryRecyclerViewDecoration: DiaryRecyclerViewDecoration? = null

    // 뷰모델 객체 주입
    private val viewModel: DiaryViewModel by viewModels()

    // 다시 화면으로 돌아왔을 때 데이터 업데이트 시켜주기
    public override fun onResume() {
        super.onResume()

        // 일기 수정 화면이 아닌 다른 화면에서 돌아오면 일어나는 null exception 대비
        if (recordList != null) {
            lifecycleScope.launch {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is DiaryViewModel.DiaryUiState.Success -> {
                            uiState.getDiaryListResult?.let {
                                if (it["code"].toString().toDouble() == 7000.0) {
                                    // 서버로부터 리스트 받아와서 저장하기
                                    val gson = Gson()
                                    val type = object : TypeToken<List<UserDiary?>?>() {}.type
                                    val jsonResult = gson.toJson(it["diaryList"])

                                    recordList = gson.fromJson(jsonResult, type)

                                    // 최신순/오래된순에서 수정 후 돌아왔을 때 최신순/오래된순 정렬 유지
                                    if (binding.sortDateButton.text.toString() == "최신순") {
                                        val linearLayoutManager =
                                            LinearLayoutManager(applicationContext)
                                        linearLayoutManager.reverseLayout = true
                                        linearLayoutManager.stackFromEnd = true
                                        diaryView!!.layoutManager = linearLayoutManager
                                    } else {
                                        val linearLayoutManager =
                                            LinearLayoutManager(applicationContext)
                                        linearLayoutManager.reverseLayout = false
                                        linearLayoutManager.stackFromEnd = false
                                        diaryView!!.layoutManager = linearLayoutManager
                                    }

                                    adaptor!!.updateItemList(recordList)

                                    // 만약 마음일기 모음에서 클릭이나 수정 후 돌아온 거라면
                                    if (isEmotionRecordListChecked) {
                                        emotionRecordList = ArrayList()
                                        for (i in recordList?.indices!!) {
                                            if (recordList!![i].type == "오늘의 마음 일기") {
                                                emotionRecordList.add(recordList!![i])
                                            }
                                        }
                                        adaptor!!.updateItemList(emotionRecordList)
                                    }


                                    // 만약 트라우마일기 모음에서 클릭이나 수정 후 돌아온 거라면
                                    if (isTraumaRecordListChecked) {
                                        traumaRecordList = ArrayList()
                                        for (i in recordList?.indices!!) {
                                            if (recordList!![i].type == "트라우마 일기") {
                                                traumaRecordList.add(recordList!![i])
                                            }
                                        }
                                        adaptor!!.updateItemList(traumaRecordList)
                                    }


                                    // 감정별 정렬에서 클릭이나 수정 후 돌아온 거라면
                                    if (isSingleEmotionListChecked) {
                                        singleEmotionList = ArrayList()
                                        for (i in recordList?.indices!!) {
                                            if (recordList!![i].getEmotion() == singleEmotion) {
                                                singleEmotionList.add(recordList!![i])
                                            }
                                        }
                                        adaptor!!.updateItemList(singleEmotionList)
                                    }
                                }
                            }
                        }

                        is DiaryViewModel.DiaryUiState.Error -> {
                            val toast = Toast.makeText(
                                applicationContext,
                                "서버와의 통신에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }
                }
            }

            lifecycleScope.launch {
                // 일기 리스트 새로고침
                viewModel.getDiaryList()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emotionArray = resources.getStringArray(R.array.emotions_array)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground4).into(binding.background)

        // RecyclerView 세팅
        diaryView = binding.diaryView
        // RecyclerView에 LayoutManager 적용 (기본을 최근순)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        diaryView!!.layoutManager = linearLayoutManager


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is DiaryViewModel.DiaryUiState.Success -> {
                            uiState.getDiaryListResult?.let {
                                if (it["code"].toString().toDouble() == 7000.0) {
                                    // 서버로부터 리스트 받아와서 저장하기 , https://ppizil.tistory.com/4
                                    val gson = Gson()
                                    val type = object : TypeToken<List<UserDiary?>?>() {}.type
                                    val jsonResult = gson.toJson(it["diaryList"])

                                    recordList = gson.fromJson(jsonResult, type)
                                    // 어레이리스트를 매개변수로 넘겨주어서 어뎁터 객체를 생성
                                    adaptor = DiaryAdaptor(recordList)
                                    // 생성한 어뎁터 객체를 RecyclerView에 적용
                                    diaryView!!.adapter = adaptor

                                    // 생성한 itemDecoration 객체를 RecyclerView에 적용
                                    // 한 번도 생성이 안 됐을 경우(null)일 경우만 간격 추가해주기. (아니면 화면이 재생성될 때마다 간격이 추가돼 버린다).
                                    if (diaryRecyclerViewDecoration == null) {
                                        diaryRecyclerViewDecoration = DiaryRecyclerViewDecoration()
                                        diaryView!!.addItemDecoration(diaryRecyclerViewDecoration!!)
                                    }
                                }
                            }
                        }

                        is DiaryViewModel.DiaryUiState.Error -> {
                            if (uiState.error) {
                                val toast = Toast.makeText(
                                    applicationContext,
                                    "서버와의 통신에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }
                        }
                    }
                }

            }
        }


        // 정렬 버튼 감지
        // 날짜별 최신순/오래된순 정렬 버튼 클릭
        binding.sortDateButton.setOnClickListener {
            binding.sortEmotionButton.text = "감정별"
            isEmotionRecordListChecked = false
            isTraumaRecordListChecked = false
            isSingleEmotionListChecked = false
            indexListSingleEmotion.clear()
            indexListEmotion.clear()
            indexListTrauma.clear()
            if (binding.sortDateButton.text.toString() == "오래된순") {
                binding.sortDateButton.text = "최신순"
                linearLayoutManager.reverseLayout = true
                linearLayoutManager.stackFromEnd = true
                diaryView!!.layoutManager = linearLayoutManager
            } else {
                binding.sortDateButton.text = "오래된순"
                linearLayoutManager.reverseLayout = false
                linearLayoutManager.stackFromEnd = false
                diaryView!!.layoutManager = linearLayoutManager
            }
            adaptor!!.updateItemList(recordList)
        }

        // 마음 일기 모음 버튼 클릭
        binding.sortEmotionDiaryButton.setOnClickListener {
            binding.sortEmotionButton.text = "감정별"
            isEmotionRecordListChecked = true
            isTraumaRecordListChecked = false
            isSingleEmotionListChecked = false
            indexListSingleEmotion.clear()
            indexListTrauma.clear()
            emotionRecordList = ArrayList()
            for (i in recordList!!.indices) {
                if (recordList!![i].type == "오늘의 마음 일기") {
                    emotionRecordList.add(recordList!![i])
                    indexListEmotion.add(i)
                }
            }
            adaptor!!.updateItemList(emotionRecordList)
            val linearLayoutManager1 = LinearLayoutManager(this@DiaryActivity)
            // 옆에 최신순/오래된순 버튼의 텍스트에 따라서 All 클릭 시에 오리지널 리스트 일기 정렬되기 - 화면이 중간지점부터가 아닌 가장 윗쪽으로 스크롤 된 상태로 뜨게 하기 위한 조치
            if (binding.sortDateButton.text == "오래된순") {
                linearLayoutManager1.reverseLayout = false
                linearLayoutManager1.stackFromEnd = false
            } else if (binding.sortDateButton.text == "최신순") {
                linearLayoutManager1.reverseLayout = true
                linearLayoutManager1.stackFromEnd = true
            }
            diaryView!!.layoutManager = linearLayoutManager1
        }

        // 트라우마 일기 모음 버튼 클릭
        binding.sortTraumaButton.setOnClickListener {
            binding.sortEmotionButton.text = "감정별"
            isTraumaRecordListChecked = true
            isEmotionRecordListChecked = false
            isSingleEmotionListChecked = false
            indexListSingleEmotion.clear()
            indexListEmotion.clear()
            traumaRecordList = ArrayList()
            for (i in recordList!!.indices) {
                if (recordList!![i].type == "트라우마 일기") {
                    traumaRecordList.add(recordList!![i])
                    indexListTrauma.add(i)
                }
            }
            adaptor!!.updateItemList(traumaRecordList)
            val linearLayoutManager1 = LinearLayoutManager(this@DiaryActivity)
            // 옆에 최신순/오래된순 버튼의 텍스트에 따라서 All 클릭 시에 오리지널 리스트 일기 정렬되기 - 화면이 중간지점부터가 아닌 가장 윗쪽으로 스크롤 된 상태로 뜨게 하기 위한 조치
            if (binding.sortDateButton.text == "오래된순") {
                linearLayoutManager1.reverseLayout = false
                linearLayoutManager1.stackFromEnd = false
            } else if (binding.sortDateButton.text == "최신순") {
                linearLayoutManager1.reverseLayout = true
                linearLayoutManager1.stackFromEnd = true
            }
            diaryView!!.layoutManager = linearLayoutManager1
        }

        // 감정벌 정렬 버튼 클릭
        binding.sortEmotionButton.setOnClickListener {
            val intent = Intent(this@DiaryActivity, EmotionSortingPopup::class.java)
            startActivityForResult(intent, 1)
        }

    }

    // 감정정렬팝업으로부터 데이터 가져오기 (EmotionSortingPopup)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // 데이터 받기
                val emotion = data!!.getStringExtra("emotion")
                sortByEmotion(emotion)
                binding.sortEmotionButton.text = emotion
            }
        }
    }

    // 감정별 정렬을 위한 함수
    private fun sortByEmotion(emotion: String?) {
        isEmotionRecordListChecked = false
        isTraumaRecordListChecked = false
        isSingleEmotionListChecked = true
        singleEmotionList = ArrayList()
        indexListTrauma.clear()
        indexListEmotion.clear()
        indexListSingleEmotion.clear()
        singleEmotion = emotion
        for (i in recordList!!.indices) {
            if (recordList!![i].getEmotion() == emotion) {
                singleEmotionList.add(recordList!![i])
                indexListSingleEmotion.add(i)
            }
        }
        adaptor!!.updateItemList(singleEmotionList)
    }

    // RecyclerView
    // 목록의 개별 항목을 구성하기 위한 뷰들을 viewBinding을 통해 hold,들고 있는 역할 - 뷰를 재활용할 수 있게 해 준다
    internal inner class ViewHolder(var binding: DiaryitemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // viewHolder을 생성하며, 배열에 저장되어 있는 데이터와 recyclerView를 연결시켜 주어서, 데이터를 RecyclerView에서 목록으로 볼 수 있게 한다.
    private inner class DiaryAdaptor(  // 항목 구성 데이터
        private var recordList: ArrayList<UserDiary>?
    ) : RecyclerView.Adapter<ViewHolder>() {

        // 데이터셋을 업데이트하기 위한 메서드
        @SuppressLint("NotifyDataSetChanged")
        fun updateItemList(arrayList: ArrayList<UserDiary>?) {
            this.recordList = arrayList
            notifyDataSetChanged()
        }

        // 뷰 홀더 객체를 생성한다. 항목을 구성하기 위한 레이아웃 xml파일을 inflate한 binding객체를 ViewHolder 생성자로 넘겨주어 만들어진 viewHolder 객체를 반환하면, 이를 메모리에 유지했다가 onBindViewHolder() 호출 시에 매개변수로 전달하는 구조이다.
        override fun onCreateViewHolder(viewGorup: ViewGroup, i: Int): ViewHolder {
            val binding =
                DiaryitemBinding.inflate(LayoutInflater.from(viewGorup.context), viewGorup, false)
            // 뷰 홀더 생성
            return ViewHolder(binding)
        }

        // 배열에 저장되어 있는 데이터와 뷰홀더의 뷰들을 연결시켜준다.
        // 각 항목 구성하기 위해서 자동 콜된다: 항목이 x개면 x번 호출된다
        @RequiresApi(api = Build.VERSION_CODES.O)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            // 감정 아이콘 세팅
            val emotion = recordList!![position].getEmotion()
            emotionColorNumber = when (emotion) {
                "기쁨" -> R.drawable.orange_happiness
                "기대" -> R.drawable.green_anticipation
                "신뢰" -> R.drawable.darkblue_trust
                "놀람" -> R.drawable.yellow_surprise
                "슬픔" -> R.drawable.grey_sadness
                "혐오" -> R.drawable.brown_disgust
                "공포" -> R.drawable.black_fear
                "분노" -> R.drawable.red_anger
                else -> R.drawable.purple_etc
            }
            viewHolder.binding.emotionCircle.setImageResource(emotionColorNumber)

            // 날짜 세팅
            date = recordList!![position].getDate() + " " + recordList!![position].getDay()

            viewHolder.binding.date.text = date

            // 감정 이름 세팅
            viewHolder.binding.emotionWord.text = emotion

            // 상황 세팅
            val situation = recordList!![position].getSituation()

            viewHolder.binding.situation.text = situation

            // 타입 세팅
            val type = recordList!![position].getType()

            viewHolder.binding.type.text = type

            // 아이템 클릭(일기 상세 보기)
            viewHolder.binding.root.setOnClickListener {

                val intent = Intent(applicationContext, DiaryResultActivity::class.java)
                intent.putExtra("type", recordList!![position].getType())
                intent.putExtra(
                    "date",
                    recordList!![position].getDate() + " " + recordList!![position].getDay()
                )
                intent.putExtra("situation", recordList!![position].getSituation())
                intent.putExtra("thought", recordList!![position].getThought())
                intent.putExtra("emotion", recordList!![position].getEmotion())
                intent.putExtra("emotionDescription", recordList!![position].getEmotionDescription())
                intent.putExtra("reflection", recordList!![position].getReflection())
                intent.putExtra("diaryNumber", recordList!![position].getDiary_number())

                // 오리지날 리스트의 몇 번째 인덱스에 새로운 리스트의 요소가 있나
                // 이미 새로운 리스트를 만들 때, 오리지날 리스트의 몇 번째 인덱스의 것인지를 저장해 두었다.
                // 새로운 리스트의 요소의 인덱스 = 저장된 인덱스의 순서
                // 그러므로 indexList.get(position)을 하면 해당 오리지날 리스트의 인덱스가 나온다.
                if (indexListEmotion.size != 0) {
                    intent.putExtra("index", indexListEmotion[position])
                } else if (indexListTrauma.size != 0) {
                    intent.putExtra("index", indexListTrauma[position])
                } else if (indexListSingleEmotion.size != 0) {
                    intent.putExtra("index", indexListSingleEmotion[position])
                } else {
                    intent.putExtra("index", position)
                }
                startActivity(intent)

            }
        }

        override fun getItemCount(): Int {
            return if (recordList == null) {
                0
            } else recordList!!.size
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }
    }


    class DiaryRecyclerViewDecoration : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(0, 0, 0, 30)
        }
    }


    companion object {
        var diaryView: RecyclerView? = null
    }
}