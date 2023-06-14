package com.android.mymindnotes.presentation.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.mymindnotes.*
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityDiaryResultBinding
import com.android.mymindnotes.core.model.UserDiary
import com.android.mymindnotes.presentation.viewmodels.DiaryResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class DiaryResult : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryResultBinding

    // 뷰모델 객체 주입
    private val viewModel: DiaryResultViewModel by viewModels()

    // 데이터를 위한 변수
    var type: String? = null
    var date: String? = null
    var situation: String? = null
    var thought: String? = null
    var emotion: String? = null
    var emotionDescription: String? = null
    var reflection: String? = null
    var index = 0
    var diaryNumber = 0
    var diarylist: ArrayList<UserDiary>? = null

    // tab layout의 tab
    var tabs = arrayOf("상황", "생각", "감정", "회고")

    // diaryResultFragmentFactoryAssistedFactory 주입 (Dagger이 interface의 구현체 만들어 바인딩 제공)
    @Inject
    lateinit var diaryResultFragmentFactoryAssistedFactory: DiaryResultFragmentFactoryAssistedFactory

    lateinit var fragmentFactory: DiaryResultFragmentFactory

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {

            launch {
                // 일기 새로고침 감지
                viewModel.diaryList.collectLatest {
                    if (it["code"].toString().toDouble() == 7000.0) {
                        val gson = Gson()
                        val type = object : TypeToken<List<UserDiary?>?>() {}.type
                        val jsonResult = gson.toJson(it["diaryList"])
                        diarylist = gson.fromJson(jsonResult, type)
                        diarylist?.let { diaryList -> sendFragmentsData(diaryList[index]) }
                    }
                }
            }

            launch {
                // 애러 구독
                viewModel.getDiaryListError.collect {
                    val toast = Toast.makeText(
                        applicationContext,
                        "서버와의 통신에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            launch {
                // 일기 리스트 새로고침
                viewModel.getDiaryList()
            }

        }
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDiaryResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground4).into(binding.background)

        // 데이터 세팅을 위해 가져오기
        val intent = intent

        // 데이터 세팅
        type = intent.getStringExtra("type")
        date = intent.getStringExtra("date")
        situation = intent.getStringExtra("situation")
        thought = intent.getStringExtra("thought")
        emotion = intent.getStringExtra("emotion")
        emotionDescription = intent.getStringExtra("emotionDescription")
        reflection = intent.getStringExtra("reflection")
        index = intent.getIntExtra("index", 0)
        diaryNumber = intent.getIntExtra("diaryNumber", 0)

        // 타입 뿌리기
        binding.type.text = "$type,  "
        // 오늘 날짜 뿌리기
        binding.date.text = "$date "

        // viewPager2와 tablayout 세팅
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewpager2
        val adapter = ViewPager2Adapter(this)
        viewPager2.adapter = adapter
        // 캐싱을 해놓을 페이지의 최대 갯수 설정, 즉, 좌우 몇개의 페이지를 그려놓고 있을지 설정(상태를 유지할 페이지의 최대 갯수). 따라서 모든 페이지를 한번 초기화하고 다시는 초기화하지 않게 하려면, viewPager.setOffscreenPageLimit(PAGE_LENGTH) 처럼 사용하면 된다. 뒤에 페이지를 모두 미리 로딩하고 싶다면 setOffscreenPageLimit(4) - 미리 뒤에 보여줄 Fragment를 담아놓고, 스크롤 할 때 보여주게 된다.
        viewPager2.offscreenPageLimit = 4

        // viewPager2와 tablayout 연동하기
        TabLayoutMediator(tabLayout, viewPager2) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabs[position]
        }.attach()

        // DiaryResultFragmentFactory 인스턴스 생성 (런타임 시 이 클래스의 intent.extras를 인자로 제공)
        setUpFragmentFactory(intent.extras)


        // 클릭 이벤트
        // 돌아가기 버튼 클릭
        binding.backtoListButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickBackToListButton()
            }
        }

        // 수정 버튼 클릭
        // 수정 버튼 클릭 시
        binding.editButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickEditButton()
            }
        }

        // 수정 화면 전환/돌아오기
        val editResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // 일기 목록 다시 가져오기 (새로고침)
                    lifecycleScope.launch {
                        viewModel.getDiaryList()
                    }
                }
            }

        // 삭제 버튼 클릭
        binding.deleteButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickDeleteButton()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 버튼 클릭 감지
                // 돌아가기 버튼 클릭 감지
                launch {
                    viewModel.backToListButton.collect {
                        finish()
                    }
                }

                // 수정 버튼 클릭 감지
                launch {
                    viewModel.editButton.collect {
                        val intento = Intent(applicationContext, DiaryResultEdit::class.java)
                        intento.putExtra("date", date)
                        intento.putExtra("type", type)
                        intento.putExtra("situation", situation)
                        intento.putExtra("thought", thought)
                        intento.putExtra("emotion", emotion)
                        intento.putExtra("emotionDescription", emotionDescription)
                        intento.putExtra("reflection", reflection)
                        intento.putExtra("diaryNumber", diaryNumber)
                        intento.putExtra("index", index)
                        editResult.launch(intento)
                    }
                }

                // 삭제 버튼 클릭 감지
                launch {
                    viewModel.deleteButton.collect {
                        viewModel.deleteDiary(diaryNumber)
                    }
                }

                // 일기 삭제 감지
                launch {
                    viewModel.deleteDiaryResult.collect {
                        if (it["code"].toString().toDouble() == 9000.0) {
                            finish()
                        }
                    }
                }

                // 일기 리스트 불러오기 감지
                launch {
                    // 일기 새로고침 감지
                    viewModel.diaryList.collectLatest {
                        if (it["code"].toString().toDouble() == 7000.0) {
                            val gson = Gson()
                            val type = object : TypeToken<List<UserDiary?>?>() {}.type
                            val jsonResult = gson.toJson(it["diaryList"])
                            diarylist = gson.fromJson(jsonResult, type)
                            diarylist?.let { diaryList -> sendFragmentsData(diaryList[index]) }
                        }
                    }
                }

                // 애러 구독
                launch {
                    viewModel.getDiaryListError.collect {
                        val toast = Toast.makeText(
                            this@DiaryResult,
                            "서버와의 통신에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }

                launch {
                    viewModel.deleteDiaryError.collect {
                        val toast = Toast.makeText(
                            this@DiaryResult,
                            "서버와의 통신에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }
        }

    }

    // 데이터 새로고침 -  refreshing the data of each fragment
    private fun sendFragmentsData(diary: UserDiary) {
        val fragments = supportFragmentManager.fragments
        for (index in fragments.indices) {
            when (val fragment = fragments[index]) {
                is SituationFragment -> {
                    fragment.refreshData(diary)
                    // 재새팅
                    situation = diary.getSituation()
                }
                is ThoughtFragment -> {
                    fragment.refreshData(diary)
                    // 재새팅
                    thought = diary.getThought()
                }
                is EmotionFragment -> {
                    fragment.refreshData(diary)
                    // 재새팅
                    emotion = diary.getEmotion()
                    emotionDescription = diary.getEmotionDescription()
                }
                is ReflectionFragment -> {
                    fragment.refreshData(diary)
                    // 재새팅
                    reflection = diary.getReflection()
                }
            }
        }
    }

    // DiaryResultFragmentFactory 인스턴스 생성 (런타임 시 intent.extras를 인자로 제공하여 생성)
    private fun setUpFragmentFactory(diaryInfo: Bundle?): DiaryResultFragmentFactory {
        fragmentFactory = diaryResultFragmentFactoryAssistedFactory.create(diaryInfo)
        return fragmentFactory
    }

    // viewPager2 어뎁터 - creating and managing the fragments in the ViewPager2
    inner class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> fragmentFactory.instantiate(
                    SituationFragment::class.java.classLoader!!,
                    SituationFragment::class.java.name
                )
                1 -> fragmentFactory.instantiate(
                    ThoughtFragment::class.java.classLoader!!,
                    ThoughtFragment::class.java.name
                )
                2 -> fragmentFactory.instantiate(
                    EmotionFragment::class.java.classLoader!!,
                    EmotionFragment::class.java.name
                )
                3 -> fragmentFactory.instantiate(
                    ReflectionFragment::class.java.classLoader!!,
                    ReflectionFragment::class.java.name
                )
                else -> fragmentFactory.instantiate(
                    SituationFragment::class.java.classLoader!!,
                    SituationFragment::class.java.name
                )
            }
        }

        override fun getItemCount(): Int {
            return tabs.size
        }
    }

}