package com.android.mymindnotes.presentation.ui

import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.android.mymindnotes.presentation.viewmodels.MainViewModel
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // viewModel 객체 생성
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("순서", "(Activity) onCreate() 시작")
        super.onCreate(savedInstanceState)

        viewModel.checkAndUpdateAutoLoginState()

        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.e("순서", "(Activity) UI 랜더링")
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground2).into(binding.background)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiState.collect { uiState ->
                        when (uiState) {
                            is MainViewModel.MainUiState.Loading -> {
                            }

                            is MainViewModel.MainUiState.AutoLogin -> {

                                val intent = Intent(applicationContext, MainPageActivity::class.java)
                                startActivity(intent)
                                Log.e("순서", "(Activity) 화면 전환")

                            }
                        }
                    }
                }
            }
        }


        // 로그인 클릭
        binding.loginButton.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 클릭
        binding.joinButton.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }
    }

    // 뒤로가기 누를 시 앱 종료
    override fun onBackPressed() {
        finishAffinity()
    }
}