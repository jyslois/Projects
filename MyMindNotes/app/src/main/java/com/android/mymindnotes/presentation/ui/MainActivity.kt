package com.android.mymindnotes.presentation.ui

import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
import com.android.mymindnotes.presentation.viewmodels.MainViewModel
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.Join
import com.android.mymindnotes.Login
import com.android.mymindnotes.MainPage
import com.android.mymindnotes.databinding.ActivityMainBinding
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // viewModel 객체 생성
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground2).into(binding.background)


        // 로그인 상태 유지 값 SharedFlow 관찰하고 있다가 값에 따라서 동작하기
        // activity의 lifecycle과 결합하기
        // UI update가 이루어져야 하기 때문에 Dispatchers.Main의 쓰레드(메인 쓰레드)에서 진행
        // 사실 Dispatchers.Main은 생략 가능(어차피 Main thread이기 때문에)
        lifecycleScope.launch {
            // lifecycle의 상태state가 Started에서 벗어나면 코루틴을 cancel 시키기
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // 자식 코루틴은 부모 코루틴의 context를 물려받음 - Dispatchers.Main. Dispatchers.IO을 사용하는 코루틴에서 SharedFlow에 값을 emit하고, Dispatchers.Main을 사용하는 또다른 코루튼에서 SharedFlow를 collect()한다면, 값의 방출은 계속해서 IO thread에서 이루어지지만, 수집된 값은 Main Thread에서 처리된다. Main thread로 결과가 보내진다.
                    viewModel.autoLoginCheck.collect {
                        // if you want to trigger the intent every time the value of autoLoginCheck is true, you should use collect() instead of collectLatest()
                        // viewModel의 SharedFlow 값을 관찰하고 있다가 값이 emit되고, 그 값이 true라면 아래를 수행한다.
                        if (it) {
                            val intent = Intent(baseContext, MainPage::class.java)
                            startActivity(intent);
                        }
                    }
                }

                launch {
                    // 뷰모델의 SharedFlow에 값이 emit되면 collect해서 화면 전환
                    viewModel.login.collect {
                        if (it) {
                            val intent = Intent(applicationContext, Login::class.java)
                            startActivity(intent)
                        }
                    }
                }

                launch {
                    viewModel.join.collect {
                        if (it) {
                            val intent = Intent(applicationContext, Join::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }


        // 로그인 클릭 시 뷰모델의 메서드 호출
        binding.loginButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickLoginButton()
            }
        }

        // 회원가입 클릭 시 뷰모델의 메서드 호출
        binding.joinButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickJoinButton()
            }
        }
    }

    // 뒤로가기 누를 시 앱 종료
    override fun onBackPressed() {
        finishAffinity()
    }
}