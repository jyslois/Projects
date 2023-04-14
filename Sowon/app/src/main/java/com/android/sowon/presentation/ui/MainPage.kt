package com.android.sowon.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.sowon.R
import com.android.sowon.databinding.ActivityMainPageBinding
import com.android.sowon.presentation.viewmodel.MainPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainPage : AppCompatActivity() {
    private lateinit var binding : ActivityMainPageBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var transaction: FragmentTransaction

    @Inject
    lateinit var homeFragment: HomeFragment
    @Inject
    lateinit var classFragment: ClassFragment
    @Inject
    lateinit var accountFragment: AccountFragment

    // 뷰모델 객체 주입
    private val viewModel: MainPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 진입 화면 세팅
        // fragmentManager 세팅
        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()
        // 홈 프레그먼트를 기본 화면으로 띄우기 (우선적으로 프레임레이아웃에 추가하기)
        transaction.add(R.id.mainFrameLayout, homeFragment).commit()


        val bottomNavigation = binding.bottomNavigation

        // 클릭 이벤트
        // 바톰네비게이션 아이템 클릭
        bottomNavigation.setOnItemSelectedListener { item ->  // setOnNavigationItemSelectedListener은 deprecated 되었다 => setOnItemSelectedListener
            lifecycleScope.launch {
                viewModel.clickBottomNavigationItem(item)
            }
            true
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 클릭 이벤트 감지
                // 바톰네비게이션 아이템 클릭 감지
                launch {
                    viewModel.bottomNavigation.collect {
                        transaction = fragmentManager.beginTransaction() // 아이템이 선택될 때 마다 transaction을 따로 만들어주기: 위 코드가 만약 setOnItemSelectedListener 밖에 있어서 화면이 사용자에게 보여진 후 1회만 실행 된다면, 이미 화면을 그리면서 Commit()한 transaction에 다른 Fragment를 할당하고 Commit()하려 했기 때문에 이미 commit되었다고 에러가 발생. 따라서 onNavigationItemSelectedListener 안에 작성하여 Item을 선택할 때 마다 transaction을 따로 만들어 주어 관리
                        transaction.apply {
                            when (it.itemId) { // bottom_menu.xml에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 된다.
                                R.id.home -> replace(R.id.mainFrameLayout, homeFragment)
                                R.id.myClass -> replace(R.id.mainFrameLayout, classFragment)
                                R.id.myAccount -> replace(R.id.mainFrameLayout, accountFragment)
                            }
                            // Commit the transaction
                            commit()
                        }
                    }
                }
            }
        }

    }

    private var backButtonPressedTime = 0L
    override fun onBackPressed() {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment.isVisible && fragment is HomeFragment) { // 현재 fragment가 HomeFragment라면
                if (System.currentTimeMillis() - backButtonPressedTime > 3000) { // 3초가 지나서 눌렀다면
                    Toast.makeText(applicationContext, "종료하려면 한 번 더 누르세요", Toast.LENGTH_SHORT).show()
                    backButtonPressedTime = System.currentTimeMillis() // 현재 시간을 backButtonPreseedTime에 저장
                } else {
                    ActivityCompat.finishAffinity(this) // 3초 이내에 두 번 누른 경우 앱 종료
                }
                return
            }
        }

        // 그게 아니라면(다른 탭이면) 뒤로 가기 버튼을 누르면, homeFragment가 보이게 하기
        transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.mainFrameLayout, homeFragment).commit()
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.menu.getItem(0).isChecked = true // homeFragment item도 다시 checked로 표시

    }

}