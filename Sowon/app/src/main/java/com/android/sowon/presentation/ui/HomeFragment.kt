package com.android.sowon.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.android.sowon.R
import com.android.sowon.databinding.FragmentHomeBinding
import com.android.sowon.presentation.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        val eventBannerList = arrayListOf(R.drawable.example1, R.drawable.example2, R.drawable.example3)

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
            var currentState = 0 // 현재 상태
            var currentPos = 0 // 위치 값

            // 페이지가 스크롤할 때 호출
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPos == position) { // 현재 상태가 ViewPager2.SCROLL_STATE_DRAGGING(fake drag: 시작과 끝 페이지에서 이동 불가능한 방향으로 drag하였음)이며 현재 위치와 Position 값이 일치할 경우
                    if(currentPos == 0) viewpager.currentItem = 2 // currentPos 현재 위치가 0이면 첫 번째 페이지의 위치에서 마지막 페이지로 이동하고자 하는 것이기 때문에, 마지막 페이지의 Position값인 2로 이동하게 함
                    else if(currentPos == 2) viewpager.currentItem = 0 // 반대로 현재 위치가 2일 경우 마지막 페이지에서 첫번째 페이지(position=0)으로 이동하게 함. 이로서 순환하는 ViewPager가 만들어진다.
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            // 페이지가 변하였을 경우 현재 페이지의 Position 값과 State를 변수에 저장
            override fun onPageSelected(position: Int) {
                currentPos = position
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                currentState = state
                super.onPageScrollStateChanged(state)
            }
        })

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //  Fragment에서 View Binding을 사용할 경우 Fragment는 View보다 오래 지속되어, 메모리 누수가 발생할 수 있기 때문.
    }


}
