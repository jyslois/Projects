package com.android.sowon.presentation.ui

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.android.sowon.R
import com.android.sowon.databinding.FragmentHomeBinding
import com.android.sowon.presentation.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.ViewModelLifecycle
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import javax.inject.Inject

@AndroidEntryPoint // enable field injection of Android-specific dependencies
class HomeFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // 뷰모델 객체 삽입
    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 스크롤 배너 이벤트 광고
        // 배너 이미지 리스트
        val eventBannerList = arrayListOf(R.drawable.example3, R.drawable.example1, R.drawable.example2)

        // 배너 viewpager 세팅
        val viewpager = binding.bannerViewPager
        viewpager.adapter = BannerViewPagerAdaptor(eventBannerList) // 어뎁터 생성, 뷰페이저에 붙이기
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 뷰페이저 방향을 가로로
        viewpager.offscreenPageLimit = 2 // 뷰페이저에서 미리 로드할 페이지 수 지정

        // viewPager2 Indciator
        val indicator = binding.viewpager2Indicator
        indicator.attachTo(viewpager)

        // viewPager2 Circular Scrolling
        viewpager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            var currentState = 0 // 현재 상태 - ViewPager2.SCROLL_STATE_IDLE, indicating that the ViewPager2 is not currently being scrolled
            var currentPos = 0 // 위치 값, which corresponds to the first page in the ViewPager2.

            // 페이지가 스크롤될 때 호출 - This method is called when the ViewPager2 is scrolled, either by the user swiping or by programmatic scrolling.
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // 현재 상태가 ViewPager2.SCROLL_STATE_DRAGGING(사용자 스크롤 동작을 감지: 사용자가 ViewPager2를 드래그하거나 fake drag을 사용하여 ViewPager2를 수평으로 이동시키는 경우-사용자가 스크롤하고 있는 것처럼 보이게끔 하지만 실제로는 스크롤이 일어나지 않음)인지,
                // 현재 위치와 현재 페이지의 위치가 일치하는지 (ViewPager2가 프로그래밍 방식으로 스크롤되면 해당 메서드가 현재 페이지와 일치하지 않는 위치 값을 사용하여 호출될 수 있기 때문)를 체크
                if(currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPos == position) {
                    // checks whether the current position is 0 or the last page. If the current position is 0, it sets the current item to the last page (eventBannerList.size - 1) to create the circular scrolling effect. If the current position is the last page, it sets the current item to 0 to loop back to the first page.
                    if(currentPos == 0) viewpager.currentItem = eventBannerList.size - 1 // currentPos 현재 위치가 0이면 첫 번째 페이지의 위치에서 마지막 페이지로 이동하고자 하는 것이기 때문에, 마지막 페이지의 Position값인 (eventBannerList.size - 1)로 이동하게 함
                    else if(currentPos == eventBannerList.size - 1) viewpager.currentItem = 0 // 반대로 현재 위치가 마지막 페이지(eventBannerList.size - 1)일 경우 마지막 페이지에서 첫번째 페이지(position=0)으로 이동하게 함. 이로서 순환하는 ViewPager가 만들어진다.
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

        // 과정별 정렬 버튼
        val allSortingButton = binding.sortingButtonAll
        val basicsSortingButton = binding.sortingButtonBasics
        val kakaoTalkSortingButton = binding.sortingButtonKakaoTalk
        val baeminSortingButton = binding.sortingButtonBaemin

        // 굵기 설정
        fun unBold() {
            allSortingButton.setTypeface(null, Typeface.NORMAL)
            basicsSortingButton.setTypeface(null, Typeface.NORMAL)
            kakaoTalkSortingButton.setTypeface(null, Typeface.NORMAL)
            baeminSortingButton.setTypeface(null, Typeface.NORMAL)
        }

        fun makeBold(textView: TextView) {
            textView.setTypeface(null, Typeface.BOLD)
        }

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
                // 강의 정렬 버튼 클릭 결과 구독
                // 전체 강의 버튼
                launch {
                    viewModel.allSortingButton.collect {
                        unBold()
                        makeBold(it)
                    }
                }

                // 기초 강의 버튼
                launch {
                    viewModel.basicsSortingButton.collect {
                        unBold()
                        makeBold(it)
                    }
                }

                // 카카오톡 강의 버튼
                launch {
                    viewModel.kakaoTalkSortingButton.collect {
                        unBold()
                        makeBold(it)
                    }
                }

                // 배민 강의 버튼
                launch {
                    viewModel.baeminSortingButton.collect {
                        unBold()
                        makeBold(it)
                    }
                }


            }
        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //  Fragment에서 View Binding을 사용할 경우 Fragment는 View보다 오래 지속되어, 메모리 누수가 발생할 수 있기 때문.
    }


}
