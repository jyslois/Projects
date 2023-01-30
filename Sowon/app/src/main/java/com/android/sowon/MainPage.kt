package com.android.sowon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.sowon.databinding.ActivityMainBinding
import com.android.sowon.databinding.ActivityMainPageBinding

class MainPage : AppCompatActivity() {

    private lateinit var binding : ActivityMainPageBinding
    val fragmentManager: FragmentManager = supportFragmentManager
    val homeFragment = HomeFragment()
    val classFragment = ClassFragment()
    val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        // 홈 프레그먼트를 기본 화면으로 띄우기 (우선적으로 프레임레이아웃에 추가하기)
        transaction.add(R.id.mainFrameLayout, homeFragment).commit()

        // 하단 탭이 눌렸을 때, 화면을 전환하기 위해서 이벤트 처리 위해 BottomNavigationView 객체 생성
        var bottomNavigation = binding.bottomNavigation

        // 탭 아이템 선택 시 이벤트 처리
        // bottom_menu.xml에서 설정했던 각 아이템들의 id를 통해 알맞을 프래그먼트로 변경하게 된다.
        // setOnNavigationItemSelectedListener은 deprecated 되었다 => setOnItemSelectedListener
        bottomNavigation.setOnItemSelectedListener { item ->
            // 아이템이 선택될 때 마다 실행
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            // 위 코드가 만약 onNavigationItemSelectedListener 밖에 있어서 화면이 사용자에게 보여진 후 1회만 실행 된다면
            // 이미 화면을 그리면서 Commit()한 transaction에 다른 Fragment를 할당하고 Commit()하려 했기 때문에 이미 commit되었다고 에러가 발생
            // 따라서 onNavigationItemSelectedListener 안에 작성하여 Item을 선택할 때 마다 transaction을 따로 만들어 주어 관리

            when (item.itemId) {
                R.id.home -> {
                    transaction.replace(R.id.mainFrameLayout, homeFragment).commit()
                }

                R.id.myClass -> {
                    transaction.replace(R.id.mainFrameLayout, classFragment).commit()
                }

                R.id.myAccount -> {
                    transaction.replace(R.id.mainFrameLayout, accountFragment).commit()
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        // 현재 fragment가 HomeFragment라면 뒤로 가기 버튼 눌렀을 때 종료
        for (fragment in supportFragmentManager.fragments) {
            // 현재 보이는 fragment가
            if (fragment.isVisible) {
                // Home Fragment라면, 완전히 종료
                if (fragment is HomeFragment) {
                    ActivityCompat.finishAffinity(this);
                }
            }
        }

        // 그게 아니라면(다른 탭이면) 뒤로 가기 버튼을 누르면, homeFragment가 보이게 하기
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.mainFrameLayout, homeFragment).commit()
    }

}