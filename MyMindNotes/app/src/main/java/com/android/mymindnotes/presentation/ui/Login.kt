package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.EditText
import android.content.SharedPreferences
import android.os.Bundle
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.MainPage
import com.android.mymindnotes.FindPassword
import com.android.mymindnotes.databinding.ActivityLoginBinding
import com.android.mymindnotes.model.retrofit.RetrofitService
import com.android.mymindnotes.model.retrofit.LoginApi
import com.android.mymindnotes.model.UserInfoLogin
import com.android.mymindnotes.presentation.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var alertDialog: AlertDialog
    lateinit var autoSave: CheckBox
    lateinit var autoLogin: CheckBox
    lateinit var auto: SharedPreferences
    lateinit var autoSaveEdit: Editor
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var userindex: SharedPreferences
    lateinit var userindexEdit: Editor

    // 뷰모델 객체 주입
    private val viewModel: LoginViewModel by viewModels()

    // 알림 dialoguee
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        // 백그라운드 이미지
        Glide.with(this@Login).load(R.drawable.mainbackground).into(binding!!.background)

        // 로그인 정보 저장 & 자동 로그인 구현 관련
        var loginButton = binding!!.loginButton
        autoSave = binding!!.autoSaveButton
        autoLogin = binding!!.autoLoginButton
        email = binding!!.email
        auto = getSharedPreferences("autoSave", MODE_PRIVATE)
        autoSaveEdit = auto.edit()
        password = binding!!.password
        userindex = getSharedPreferences("userindex", MODE_PRIVATE)
        userindexEdit = userindex.edit()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.autoSaveCheck.collect {
                        // 만약 autoSaveCheck 값이 true로 저장되어 있다면,
                        if (it) {
                            // 체크박스 상태도 체크로 표시하기
                            autoSave!!.isChecked = true
                            // 저장된 아이디와 암호를 가져오기
                            viewModel.getIdAndPassword()
                        } else {
                            autoSave!!.isChecked = false
                        }
                    }
                }

                launch {
                    viewModel.id.collect {
                        // 아이디 뷰에 뿌리기
                        email!!.setText(it)
                    }
                }

                launch {
                    viewModel.password.collect {
                        // 패스워드 뷰에 뿌리기
                        password!!.setText(it)
                    }
                }


            }
        }


        // 아이디/비밀번호 저장 체크 박스 클릭 시
        autoSave!!.setOnClickListener { view: View? ->
            // 만약 체크 박스가 체크되어 있거나, 로그인 상태 유지 체크 박스가 체크되어 있다면,
            if (autoSave!!.isChecked || autoLogin!!.isChecked) {
                // 상태 저장
                autoSaveEdit.putBoolean("autoSaveCheck", true)
                autoSaveEdit.apply()
                // 아이디와 비밀번호 저장
                autoSaveEdit.putString("id", email!!.text.toString())
                autoSaveEdit.putString("password", password!!.text.toString())
            } else {
                // 체크되어 있지 않다면,
                // 상태 저장, 아이디와 비밀번호 값을 ""로 저장
                autoSaveEdit.putBoolean("autoSaveCheck", false)
                autoSaveEdit.apply()
                autoSaveEdit.putString("id", "")
                autoSaveEdit.putString("password", "")
            }
            autoSaveEdit.commit()
        }


        // 만약 로그인 상태 유지 값이 true로 저장되어 있다면
        if (auto.getBoolean("autoLoginCheck", false)) {
            // 체크박스 상태도 체크로 표시 (아이디 비밀번호 저장, 로그인 상태 유지 둘 다 체크)
            autoLogin!!.isChecked = true
            autoSave!!.isChecked = true
            // 만약 이메일과 패스워드가 비어 있지 않다면, 자동 로그인 하기
            if (email!!.text.toString() != "" && password!!.text.toString() != "") {
                val intent = Intent(applicationContext, MainPage::class.java)
                startActivity(intent)
            }
        } else {
            autoLogin!!.isChecked = false
        }


        // 자동 로그인 체크 버튼 클릭 이벤트
        autoLogin!!.setOnClickListener { view: View? ->
            // 만약 체크 박스가 체크되어 있다면
            if (autoLogin!!.isChecked) {
                autoSave!!.isChecked = true
                autoSaveEdit.putBoolean("autoLoginCheck", true)
                autoSaveEdit.apply()
            } else {
                // 체크되어 있지 않다면
                autoSaveEdit.putBoolean("autoLoginCheck", false)
                autoSaveEdit.apply()
            }
            autoSaveEdit.commit()
        }


        // 로그인 버튼 클릭 시
        loginButton.setOnClickListener(View.OnClickListener { view: View? ->
            val emailInput = email!!.text.toString()
            val passwordInput = password!!.text.toString()

            // 만약 이메일/페스워드를 적지 않았다면
            if (emailInput == "") {
                dialog("아이디를 입력하세요")
            } else if (passwordInput == "") {
                dialog("비밀번호를 입력하세요")
                // 바른 이메일 형식을 입력하지 않았다면ㄴ
            } else if (!emailInput.contains("@")) {
                dialog("올바른 이메일 형식으로 입력해 주세요")
                // 비밀번호가 6자리 이하라면
            } else if (binding!!.password.text.toString().length < 6) {
                dialog("비밀번호가 틀렸습니다")
                // 올바르게 적었다면
            } else {
                // 만약 자동 로그인 버튼이 체크되어 있다면
                if (autoLogin!!.isChecked) {
                    // 상태 저장
                    autoSaveEdit.putBoolean("autoLoginCheck", true)
                    autoSaveEdit.apply()
                    autoSave!!.isChecked = true
                }

                // 아이디/비밀번호 저장 버튼이 체크되어 있다면
                if (autoSave!!.isChecked) {
                    // 체크박스 상태 저장
                    autoSaveEdit.putBoolean("autoSaveCheck", true)
                    autoSaveEdit.apply()
                    // 아이디와 비밀번호 저장
                    autoSaveEdit.putString("id", emailInput)
                    autoSaveEdit.putString("password", passwordInput)
                } else {
                    autoSaveEdit.putBoolean("autoCheck", false)
                    autoSaveEdit.apply()
                    autoSaveEdit.putString("id", null)
                    autoSaveEdit.putString("password", null)
                }
                autoSaveEdit.commit()
//                login()
            }
        })

        // 비밀번호 찾기 버튼 클릭시
        binding!!.findPasswordButton.setOnClickListener { view: View? ->
            val intent = Intent(this, FindPassword::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()

        // 만약 체크 박스가 체크되어 있다면
        if (autoSave!!.isChecked) {
            autoSaveEdit!!.putBoolean("autoSaveCheck", true)
            autoSaveEdit!!.apply()
            // 아이디와 비밀번호 저장
            autoSaveEdit!!.putString("id", email!!.text.toString())
            autoSaveEdit!!.putString("password", password!!.text.toString())
        } else {
            // 체크되어 있지 않다면 아이디와 비밀번호 값을 null 저장
            autoSaveEdit!!.putBoolean("autoSaveCheck", false)
            autoSaveEdit!!.apply()
            autoSaveEdit!!.putString("id", "")
            autoSaveEdit!!.putString("password", "")
        }
        autoSaveEdit!!.apply()
    }

//    // 네트워크 통신: 로그인
//    fun login() {
//        // Retrofit 객체 생성
//        val retrofitService = RetrofitService()
//        // Retrofit 객체에 Service 인터페이스 등록
//        val loginApi = retrofitService.retrofit.create(LoginApi::class.java)
//        // Call 객체 획득
//        val userInfoLogin = UserInfoLogin(email!!.text.toString(), password!!.text.toString())
//        val call = loginApi.login(userInfoLogin)
//        // 네트워킹 시도
//        call.enqueue(object : Callback<Map<String?, Any>> {
//            override fun onResponse(
//                call: Call<Map<String?, Any>>,
//                response: Response<Map<String?, Any>>
//            ) {
//                // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
//                // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
//                if (response.body()!!["code"].toString()
//                        .toDouble() == 5001 || response.body()!!["code"].toString()
//                        .toDouble() == 5003 || response.body()!!["code"].toString()
//                        .toDouble() == 5005
//                ) {
//                    dialog(response.body()!!["msg"] as String?)
//                } else if (response.body()!!["code"].toString().toDouble() == 5000) {
//                    // 회원 번호 저장
//                    userindexEdit!!.putInt(
//                        "userindex", response.body()!!["user_index"].toString().toDouble()
//                            .toInt()
//                    )
//                    userindexEdit!!.commit()
//                    // 화면 전환
//                    val intent = Intent(applicationContext, MainPage::class.java)
//                    startActivity(intent)
//                }
//            }
//
//            override fun onFailure(call: Call<Map<String?, Any>>, t: Throwable) {
//                dialog("네트워크 연결에 실패했습니다. 다시 시도해 주세요.")
//            }
//        })
//    }
}