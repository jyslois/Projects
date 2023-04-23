package com.android.sowon.presentation.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.android.sowon.R
import com.android.sowon.databinding.FragmentHomeBinding
import com.android.sowon.presentation.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint // enable field injection of Android-specific dependencies
class HomeFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // 뷰모델 객체 삽입
    private val viewModel: HomeFragmentViewModel by viewModels()

    // 배너 리스트
    private lateinit var eventBannerList: ArrayList<Int>

    // 리사이클러뷰 어뎁터
    private lateinit var adaptor: LectureListAdaptor

    // 정렬 버튼
    private lateinit var allSortingButton: TextView
    private lateinit var basicsSortingButton: TextView
    private lateinit var kakaoTalkSortingButton: TextView
    private lateinit var baeminSortingButton: TextView

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getLectureList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 스크롤 배너 이벤트 광고
        setupBanner()

        // viewPager2 Indicator 붙이기
        setupViewPagerIndicator()

        // viewPager2 Circular Scrolling으로 만들기
        setupCircularScrolling()

        // 수업 종류별 정렬 버튼 세팅 - 버튼 크기와 텍스트 사이즈, 패딩 크기를 디바이스 화면 길이에 비례하여 설정
        setUpSortingButtons()

        // 수업 리스트 RecyclerView 세팅
        initLectureRecyclerView()

        // 버튼 클릭 이벤트
        // 전체 강의 보기 정렬 버튼 클릭
        allSortingButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickAllSortingButton(allSortingButton)
            }
        }

        // 기초 강의 보기 정렬 버튼 클릭
        basicsSortingButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickBasicsSortingButton(basicsSortingButton)
            }
        }

        // 카카오톡 강의 보기 정렬 버튼 클릭
        kakaoTalkSortingButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickKakaoTalkSortingButton(kakaoTalkSortingButton)
            }
        }

        // 배민 강의 보기 정렬 버튼 클릭
        baeminSortingButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickBaeminSortingButton(baeminSortingButton)
            }
        }


        // Get the lifecycle of the Fragment
        val lifecycle = viewLifecycleOwner.lifecycle

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 수업 목록 가져오기
                launch {
                    viewModel.lectureList.collect {
                        adaptor.submitList(it) // adaptor에 수업 목록 업데이트
                    }
                }

                // 강의 정렬 버튼 클릭 결과 구독
                // 전체 강의 버튼
                launch {
                    viewModel.allSortingButton.collect {
                        changeBold(it)
                    }
                }

                // 기초 강의 버튼
                launch {
                    viewModel.basicsSortingButton.collect {
                        changeBold(it)
                    }
                }

                // 카카오톡 강의 버튼
                launch {
                    viewModel.kakaoTalkSortingButton.collect {
                        changeBold(it)
                    }
                }

                // 배민 강의 버튼
                launch {
                    viewModel.baeminSortingButton.collect {
                        changeBold(it)
                    }
                }
            }
        }

        return binding.root
    }


    // 스크롤 배너 이벤트 광고
    private fun setupBanner() {
        // 배너 이미지 리스트
        eventBannerList = arrayListOf(
            R.drawable.example3,
            R.drawable.example1,
            R.drawable.example2
        )

        // 배너 viewpager 세팅
        binding.bannerViewPager.apply {
            adapter = BannerViewPagerAdaptor(eventBannerList) // 어뎁터 생성, 뷰페이저에 붙이기
            orientation = ViewPager2.ORIENTATION_HORIZONTAL // 뷰페이저 방향을 가로로
        }
    }

    // viewPager2 Indicator
    private fun setupViewPagerIndicator() {
        binding.viewpager2Indicator.attachTo(binding.bannerViewPager)
    }

    // viewPager2 Circular Scrolling
    private fun setupCircularScrolling() {
        binding.bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var currentState = 0 // 현재 상태 - ViewPager2.SCROLL_STATE_IDLE, indicating that the ViewPager2 is not currently being scrolled
            var currentPos = 0 // 위치 값, which corresponds to the first page in the ViewPager2.

            // 페이지가 스크롤될 때 호출 - This method is called when the ViewPager2 is scrolled, either by the user swiping or by programmatic scrolling.
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // 현재 상태가 ViewPager2.SCROLL_STATE_DRAGGING(사용자 스크롤 동작을 감지: 사용자가 ViewPager2를 드래그하거나 fake drag을 사용하여 ViewPager2를 수평으로 이동시키는 경우-사용자가 스크롤하고 있는 것처럼 보이게끔 하지만 실제로는 스크롤이 일어나지 않음)인지,
                // 현재 위치와 현재 페이지의 위치가 일치하는지 (ViewPager2가 프로그래밍 방식으로 스크롤되면 해당 메서드가 현재 페이지와 일치하지 않는 위치 값을 사용하여 호출될 수 있기 때문)를 체크
                if (currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPos == position) {
                    // checks whether the current position is 0 or the last page. If the current position is 0, it sets the current item to the last page (eventBannerList.size - 1) to create the circular scrolling effect. If the current position is the last page, it sets the current item to 0 to loop back to the first page.
                    if (currentPos == 0) binding.bannerViewPager.currentItem = eventBannerList.size - 1 // currentPos 현재 위치가 0이면 첫 번째 페이지의 위치에서 마지막 페이지로 이동하고자 하는 것이기 때문에, 마지막 페이지의 Position값인 (eventBannerList.size - 1)로 이동하게 함
                    else if (currentPos == eventBannerList.size - 1) binding.bannerViewPager.currentItem = 0 // 반대로 현재 위치가 마지막 페이지(eventBannerList.size - 1)일 경우 마지막 페이지에서 첫번째 페이지(position=0)으로 이동하게 함. 이로서 순환하는 ViewPager가 만들어진다.
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            // called when the current page is changed. 페이지가 변하였을 경우 현재 페이지의 Position 값과 State를 변수에 저장
            override fun onPageSelected(position: Int) {
                currentPos = position
                super.onPageSelected(position)
            }

            // called when the scroll state of the ViewPager2 changes.
            override fun onPageScrollStateChanged(state: Int) {
                currentState = state
                super.onPageScrollStateChanged(state)
            }
        })
    }

    // setup Sorting Buttons - 디바이스 화면 가로 길이에 비례해서 버튼의 넓이 길이, 텍스트 크기와 패딩 조정
    private fun setUpSortingButtons() {
        allSortingButton = binding.sortingButtonAll
        basicsSortingButton = binding.sortingButtonBasics
        kakaoTalkSortingButton = binding.sortingButtonKakaoTalk
        baeminSortingButton = binding.sortingButtonBaemin

        // 디바이스 화면 가로 길이에 비례해서 텍스트 크기와 패딩 조정
        // 화면의 가로 길이 구하기
        val displayMetrics = resources.displayMetrics // 디바이스의 화면 픽셀 밀도를 포함한 여러 정보를 담아온다
        val screenWidth = displayMetrics.widthPixels // 화면의 가로 길이를 구한다

        // 각 버튼의 가로 길이 비례하여 텍스트 크기와 패딩 조정
        val buttonWidth = screenWidth / 4 // 전체 화면을 4로 나누어 각각의 버튼에 할당할 가로 길이를 구한다
        val textSize = buttonWidth / 5.6 // 텍스트 크기를 비례적으로 조정
        val padding = buttonWidth / 7 // 버튼의 패딩을 비례적으로 조정

        allSortingButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat()) // TypedValue.COMPLEX_UNIT_PX - textSize의 값을 픽셀로 변환
        allSortingButton.setPadding(padding, padding, padding, padding)

        basicsSortingButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        basicsSortingButton.setPadding(padding, padding, padding, padding)

        kakaoTalkSortingButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        kakaoTalkSortingButton.setPadding(padding, padding, padding, padding)

        baeminSortingButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        baeminSortingButton.setPadding(padding, padding, padding, padding)
    }

    // sorting button 굵기 설정
    private fun changeBold(textView: TextView) {
        allSortingButton.setTypeface(null, Typeface.NORMAL)
        basicsSortingButton.setTypeface(null, Typeface.NORMAL)
        kakaoTalkSortingButton.setTypeface(null, Typeface.NORMAL)
        baeminSortingButton.setTypeface(null, Typeface.NORMAL)
        textView.setTypeface(null, Typeface.BOLD)
    }

    // 수업 리스트 RecyclerView 세팅
    private fun initLectureRecyclerView() {
        adaptor = LectureListAdaptor() // 어뎁터 객체 생성
        binding.lectureRecyclerView.adapter = adaptor // RecyclerView에 어뎁터 연결
        binding.lectureRecyclerView.layoutManager = LinearLayoutManager(context) // 항목을 1차원 목록으로 정렬
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //  Fragment에서 View Binding을 사용할 경우 Fragment는 View보다 오래 지속되어, 메모리 누수가 발생할 수 있기 때문.
    }

}
