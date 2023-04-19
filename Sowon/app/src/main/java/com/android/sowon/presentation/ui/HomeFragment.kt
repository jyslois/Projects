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
        val eventBannerList = arrayListOf(R.drawable.example3, R.drawable.example2, R.drawable.example1)

        // 배너 viewpager 세팅
        val viewpager = binding.bannerViewPager
        viewpager.adapter = BannerViewPagerAdaptor(eventBannerList) // 어뎁터 생성, 뷰페이저에 붙이기
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 뷰페이저 방향을 가로로

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //  Fragment에서 View Binding을 사용할 경우 Fragment는 View보다 오래 지속되어, 메모리 누수가 발생할 수 있기 때문.
    }


}
