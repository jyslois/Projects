package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
import android.text.TextWatcher
import android.text.Editable
import android.graphics.Color
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.data.retrofit.CheckEmailApi
import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.databinding.ActivityJoinBinding
import com.android.mymindnotes.presentation.viewmodels.JoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class Join : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
//    var auto: SharedPreferences
//    var autoSaveEdit: SharedPreferences.Editor
//
//    // 회원가입 성공 시 userindex 저장
//    var userindex: SharedPreferences
//    var userindexEdit: SharedPreferences.Editor
//
//    // 회원가입 후 최초 로그인시 알람 설정 다이얼로그를 띄우기 위한 sharedPreferences
//    var firsTime: SharedPreferences
//    var firstTimeEdit: SharedPreferences.Editor

    // 중복 확인을 했는지에 대한 변수
    var emailCheck: Boolean = false
    var nicknameCheck: Boolean = false
    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog
    // 뷰모델 객체 주입
    private val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground).into(binding.background)

        // 형식 체크
        val emailPattern = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
        val passwordPattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}"
        val nicknamePattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$"

        // 버튼 클릭 이벤트
        // 이메일 중복 확인 버튼
        // 이메일 중복 체크

        binding.emailCheckButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickEmailCheckButton()
            }
        }

        // 닉네임 중복 확인 버튼
        binding.nickNameCheckButton.setOnClickListener {
            lifecycleScope.launch {
//                viewModel.clickNickNameCheckButton()
            }
        }

        // 회원가입 버튼
        binding.joinButton.setOnClickListener {

        }

        // Text 변화 감지 - 중복확인 후에 이메일이나 닉네임 input이 일어나면 다시 중복체크 해야 하므로
        // 이메일
        binding.emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailCheck = false
                binding.emailCheckButton.text = "중복확인"
                binding.emailCheckButton.setBackgroundColor(Color.parseColor("#C3BE9F98")) // String으로된 Color값을 Int로 바꾸기
            }
            override fun afterTextChanged(s: Editable) {}
        })

        // 닉네임
        binding.nickNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                nicknameCheck = false
                binding.nickNameCheckButton.text = "중복확인"
                binding.nickNameCheckButton.setBackgroundColor(Color.parseColor("#C3BE9F98")) // String으로된 Color값을 Int로 바꾸기
            }

            override fun afterTextChanged(s: Editable) {}
        })



        // ViewModel SharedFlow 구독
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 이메일 중복 확인 버튼 클릭 감지 Flow 구독
                launch {
                    viewModel.clickEmailCheck.collect {
                        if (it) {
                            val emailInput = binding.emailInput.text.toString()
                            if (emailInput == "") {
                                dialog("이메일을 입력해 주세요")
                                // 이메일 형식 체크
                            } else if (!Pattern.matches(emailPattern, emailInput)) {
                                dialog("올바른 이메일 형식으로 입력해 주세요")
                            } else {
                                if (!emailCheck) {
                                    // 네트워크 통신(이메일 중복됐는지 체크)
                                    viewModel.checkEmail(emailInput)
                                }
                            }
                        }
                    }
                }

                // 이메일 중복 체크 결과 플로우 구독
                launch {
                    viewModel.emailCheckResult.collect {
                        if (it["code"].toString().toDouble() == 1001.0) {
                            dialog(it["msg"] as String)
                        } else if (it["code"].toString().toDouble() == 1000.0) {
                            confirmEmailDialog()
                        }
                    }
                }
            }
        }


//        // 회원가입 후 최초 로그인시 알람 설정 다이얼로그를 띄우기 위한 sharedPreferences
//        firsTime = getSharedPreferences("firstTime", MODE_PRIVATE)
//        firstTimeEdit = firsTime.edit()
//
//
//

//

//
//
//        // 닉네임 중복 체크
//        binding!!.nickNameCheckButton.setOnClickListener { view: View? ->
//            if (binding!!.nickNameInput.text.toString() == "") {
//                dialog("닉네임을 입력해 주세요")
//                // 이메일 형식 체크
//            } else if (!Pattern.matches(nicknamePattern, binding!!.nickNameInput.text)) {
//                dialog("닉네임은 특수문자를 제외한 2~10자여야 합니다")
//            } else {
//                if (nicknameCheck == false) {
//                    // 네트워크 통신(닉네임이 중복됐는지 체크)
//                    checkNickname()
//                }
//            }
//        }
//
//
//        auto = getSharedPreferences("autoSave", MODE_PRIVATE)
//        autoSaveEdit = auto.edit()
//        userindex = getSharedPreferences("userindex", MODE_PRIVATE)
//        userindexEdit = userindex.edit()
//
//        // 회원 가입 버튼 클릭 이벤트
//        binding!!.joinButton.setOnClickListener { view: View? ->
//            var nickNameInput = binding!!.nickNameInput.text.toString()
//            var emailInput = binding!!.emailInput.text.toString()
//            var passwordInput = binding!!.passwordInput.text.toString()
//            var passwordReTypeInput = binding!!.passwordRetypeInput.text.toString()
//            var birthyearInput = binding!!.birthyearInput.text.toString()
//
//            // 형식 체크
//
//            // 만약 이메일이나 페스워드를 적지 않았다면
//            if (emailInput == "" || passwordInput == "") {
//                dialog("이메일과 비밀번호를 입력해 주세요")
//                // 비밀 번호 형식이 잘못되었다면
//            } else if (!Pattern.matches(passwordPattern, passwordInput)) {
//                dialog("비밀번호는 영문+숫자 조합 6자~20자여야 합니다")
//                // 비밀번호와 비밀번호 확인란이 일치하지 않으면
//            } else if (passwordInput != passwordReTypeInput) {
//                dialog("비밀번호가 일치하지 않습니다")
//                // 닉네임을 적지 않았다면
//            } else if (nickNameInput == "") {
//                dialog("닉네임을 입력해 주세요")
//                // 생년을 입력하지 않았다면
//            } else if (birthyearInput == "") {
//                dialog("태어난 년도를 력해 주세요")
//                // 생년의 형식이 잘못되었다면
//            } else if (birthyearInput!!.toInt() < 1901 || birthyearInput!!.toInt() > 2155) {
//                dialog("생년은 1901~2155 사이여야 합니다")
//                // 이메일 중복확인을 하지 않았다면
//            } else if (emailCheck == false) {
//                dialog("이메일 중복확인을 해주세요")
//                // 닉네임 중복확인을 하지 않았다면
//            } else if (nicknameCheck == false) {
//                dialog("닉네임 중복확인을 해주세요")
//            } else {
//                join()
//            }
//        }
//    }
//

//
//    // 네트워크 통신: 닉네임 중복 체크
//    fun checkNickname() {
//        // Retrofit 객체 생성
//        val retrofitService = RetrofitService()
//        // Retrofit 객체에 Service 인터페이스 등록
//        val checkNicknameApi = retrofitService.retrofit.create(
//            CheckNicknameApi::class.java
//        )
//        // Call 객체 획득
//        val call = checkNicknameApi.checkNickname(
//            binding!!.nickNameInput.text.toString()
//        )
//        // 네트워킹 시도
//        call.enqueue(object : Callback<Map<String?, Any>> {
//            override fun onResponse(
//                call: Call<Map<String?, Any>>,
//                response: Response<Map<String?, Any>>
//            ) {
//                // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
//                // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
//                if (response.body()!!["code"].toString().toDouble() == 1003) {
//                    dialog(response.body()!!["msg"] as String?)
//                } else if (response.body()!!["code"].toString().toDouble() == 1002) {
//                    confirmNicknameDialog()
//                }
//            }
//
//            override fun onFailure(call: Call<Map<String?, Any>>, t: Throwable) {
//                dialog("네트워크 연결에 실패했습니다. 다시 시도해 주세요.")
//            }
//        })
//    }
//
//    // // 네트워크 통신: 회원 가입
//    fun join() {
//        // Retrofit 객체 생성
//        val retrofitService = RetrofitService()
//        // Retrofit 객체에 Service 인터페이스 등록
//        val joinApi = retrofitService.retrofit.create(JoinApi::class.java)
//        // Call 객체 획득
//        val userInfo = UserInfo(emailInput, nickNameInput, passwordInput, birthyearInput!!.toInt())
//        val call = joinApi.addUser(userInfo)
//        // 네트워킹 시도
//        call.enqueue(object : Callback<Map<String?, Any>> {
//            override fun onResponse(
//                call: Call<Map<String?, Any>>,
//                response: Response<Map<String?, Any>>
//            ) {
//                // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
//                // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
//                if (response.body()!!["code"].toString().toDouble() == 2001) {
//                    dialog(response.body()!!["msg"] as String?)
//                } else if (response.body()!!["code"].toString().toDouble() == 2000) {
//                    // 회원 번호 저장
//                    userindexEdit!!.putInt(
//                        "userindex", response.body()!!["user_index"].toString().toDouble()
//                            .toInt()
//                    )
//                    userindexEdit!!.commit()
//                    // 아이디와 비밀번호 저장
//                    autoSaveEdit!!.putString("id", emailInput)
//                    autoSaveEdit!!.putString("password", passwordInput)
//                    autoSaveEdit!!.commit()
//                    // 아이디/비밀번호 저장 체크 박스 상태를 true로 저장, 자동 로그인 설정
//                    autoSaveEdit!!.putBoolean("autoSaveCheck", true)
//                    autoSaveEdit!!.commit()
//                    autoSaveEdit!!.putBoolean("autoLoginCheck", true)
//                    autoSaveEdit!!.apply()
//
//                    // 회원가입 후 최초 로그인시 알람 설정 다이얼로그를 띄우기 위한 sharedPreferences
//                    firstTimeEdit!!.putString("firstTime", "firstTime")
//                    firstTimeEdit!!.commit()
//
//                    // 메인 화면 전환
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
//

}

    // 이메일 중복 체크 확인완료 dialogue
    fun confirmEmailDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("사용 가능한 이메일입니다.")
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
        emailCheck = true
        binding.emailCheckButton.text = "확인완료"
        binding.emailCheckButton.setBackgroundColor(Color.parseColor("#FFDDD5")) // String으로된 Color값을 Int로 바꾸기
    }

    // 닉네임 중복 체크 확인완료 dialogue
    fun confirmNicknameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("사용 가능한 닉네임입니다.")
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
        nicknameCheck = true
        binding.nickNameCheckButton.text = "확인완료"
        binding.nickNameCheckButton.setBackgroundColor(Color.parseColor("#FFDDD5")) // String으로된 Color값을 Int로 바꾸기
    }

    // 알림 dialoguee
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

}