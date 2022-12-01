package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityJoinBinding;
import com.android.mymindnotes.model.UserInfo;
import com.android.mymindnotes.retrofit.CheckEmailApi;
import com.android.mymindnotes.retrofit.CheckNicknameApi;
import com.android.mymindnotes.retrofit.JoinApi;
import com.android.mymindnotes.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.util.Map;
import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Join extends AppCompatActivity {
    ActivityJoinBinding binding;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;

    // 회원가입 성공 시 userindex 저장
    SharedPreferences userindex;
    SharedPreferences.Editor userindexEdit;

    // 중복 확인
    boolean emailCheck;
    boolean nicknameCheck;
    AlertDialog alertDialog;

    String nickNameInput;
    String emailInput;
    String passwordInput;
    String birthyearInput;

    // 이메일 중복 체크 확인완료 dialogue
    void confirmEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("사용 가능한 이메일입니다.");
        builder.setPositiveButton("확인", null);
        alertDialog = builder.create();
        alertDialog.show();
        emailCheck = true;
        binding.emailCheckButton.setText("확인완료");
        binding.emailCheckButton.setBackgroundColor(Color.parseColor("#FFDDD5")); // String으로된 Color값을 Int로 바꾸기
    }

    // 닉네임 중복 체크 확인완료 dialogue
    void confirmNicknameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("사용 가능한 닉네임입니다.");
        builder.setPositiveButton("확인", null);
        alertDialog = builder.create();
        alertDialog.show();
        nicknameCheck = true;
        binding.nickNameCheckButton.setText("확인완료");
        binding.nickNameCheckButton.setBackgroundColor(Color.parseColor("#FFDDD5")); // String으로된 Color값을 Int로 바꾸기
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground).into(binding.background);

        // 중복확인
        emailCheck = false;
        nicknameCheck = false;

        // 형식 체크
        String emailPattern = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        String passwordPattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}";
        String nicknamePattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$";

        // 이메일 중복 체크
        binding.emailCheckButton.setOnClickListener(view -> {
            if (binding.emailInput.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "이메일을 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
            // 이메일 형식 체크
            } else if(!Pattern.matches(emailPattern, binding.emailInput.getText())) {
                Toast toast = Toast.makeText(getApplicationContext(), "올바른 이메일 형식으로 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (emailCheck == false) {
                // 네트워크 통신(이메일 중복됐는지 체크)
                checkEmail();
                }
            }
        });


        // 닉네임 중복 체크
        binding.nickNameCheckButton.setOnClickListener(view -> {
            if (binding.nickNameInput.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "닉네임을 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
                // 이메일 형식 체크
            } else if(!Pattern.matches(nicknamePattern, binding.nickNameInput.getText())) {
                Toast toast = Toast.makeText(getApplicationContext(), "닉네임은 특수문자를 제외한 2~10자여야 합니다", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (nicknameCheck == false) {
                    // 네트워크 통신(닉네임이 중복됐는지 체크)
                    checkNickname();
                }
            }
        });

        // 중복확인 후에 이메일이나 닉네임 input이 일어나면 다시 중복체크 해야 하므로
        binding.emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailCheck = false;
                binding.emailCheckButton.setText("중복확인");
                binding.emailCheckButton.setBackgroundColor(Color.parseColor("#C3BE9F98")); // String으로된 Color값을 Int로 바꾸기
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.nickNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nicknameCheck = false;
                binding.nickNameCheckButton.setText("중복확인");
                binding.nickNameCheckButton.setBackgroundColor(Color.parseColor("#C3BE9F98")); // String으로된 Color값을 Int로 바꾸기
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();

        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);
        userindexEdit = userindex.edit();

        // 회원 가입 버튼 클릭 이벤트
        binding.joinButton.setOnClickListener(view -> {
            nickNameInput = binding.nickNameInput.getText().toString();
            emailInput = binding.emailInput.getText().toString();
            passwordInput = binding.passwordInput.getText().toString();
            String passwordReTypeInput = binding.passwordRetypeInput.getText().toString();
            birthyearInput = binding.birthyearInput.getText().toString();

            // 형식 체크

            // 만약 이메일이나 페스워드를 적지 않았다면
            if (emailInput.equals("") || passwordInput.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
                // 비밀 번호 형식이 잘못되었다면
            } else if (!Pattern.matches(passwordPattern, passwordInput)) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호는 영문+숫자 조합 6자~20자여야 합니다", Toast.LENGTH_SHORT);
                toast.show();
                // 비밀번호와 비밀번호 확인란이 일치하지 않으면
            } else if (!passwordInput.equals(passwordReTypeInput)) {
                Toast toast = Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT);
                toast.show();
                // 닉네임을 적지 않았다면
            } else if (nickNameInput.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "닉네임을 입력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
                // 생년을 입력하지 않았다면
            } else if (birthyearInput.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "태어난 년도를 력해 주세요", Toast.LENGTH_SHORT);
                toast.show();
            // 생년의 형식이 잘못되었다면
            } else if (Integer.parseInt(birthyearInput) < 1901 || Integer.parseInt(birthyearInput) > 2155) {
                Toast toast = Toast.makeText(getApplicationContext(), "생년은 1901~2155 사이여야 합니다", Toast.LENGTH_SHORT);
                toast.show();
            // 이메일 중복확인을 하지 않았다면
            } else if (emailCheck == false) {
                    Toast toast = Toast.makeText(getApplicationContext(), "이메일 중복확인을 해주세요", Toast.LENGTH_SHORT);
                    toast.show();
                    // 닉네임 중복확인을 하지 않았다면
                } else if (nicknameCheck == false) {
                    Toast toast = Toast.makeText(getApplicationContext(), "닉네임 중복확인을 해주세요", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                join();
            }
        });
    }

    // 네트워크 통신: 이메일 중복 체크
    public void checkEmail() {
                // Retrofit 객체 생성
                RetrofitService retrofitService = new RetrofitService();
                // Retrofit 객체에 Service 인터페이스 등록
                CheckEmailApi checkEmailApi = retrofitService.getRetrofit().create(CheckEmailApi.class);
                // Call 객체 획득
                Call<Map<String, Object>> call = checkEmailApi.checkEmail(binding.emailInput.getText().toString());
                // 네트워킹 시도
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 1001) {
                            Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 1000) {
                            confirmEmailDialog();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

    }


    // 네트워크 통신: 닉네임 중복 체크
    public void checkNickname() {
                // Retrofit 객체 생성
                RetrofitService retrofitService = new RetrofitService();
                // Retrofit 객체에 Service 인터페이스 등록
                CheckNicknameApi checkNicknameApi = retrofitService.getRetrofit().create(CheckNicknameApi.class);
                // Call 객체 획득
                Call<Map<String, Object>> call = checkNicknameApi.checkNickname(binding.nickNameInput.getText().toString());
                // 네트워킹 시도
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 1003) {
                            Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 1002) {
                            confirmNicknameDialog();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

    }

    // // 네트워크 통신: 회원 가입
    public void join() {
                // Retrofit 객체 생성
                RetrofitService retrofitService = new RetrofitService();
                // Retrofit 객체에 Service 인터페이스 등록
                JoinApi joinApi = retrofitService.getRetrofit().create(JoinApi.class);
                // Call 객체 획득
                UserInfo userInfo = new UserInfo(emailInput, nickNameInput, passwordInput, Integer.parseInt(birthyearInput));
                Call<Map<String, Object>> call = joinApi.addUser(userInfo);
                // 네트워킹 시도
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 2001) {
                            Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 2000) {
                            // 회원 번호 저장
                            userindexEdit.putInt("userindex", (int) Double.parseDouble(String.valueOf((response.body().get("user_index")))));
                            userindexEdit.commit();
                            // 아이디와 비밀번호 저장
                            autoSaveEdit.putString("id", emailInput);
                            autoSaveEdit.putString("password", passwordInput);
                            autoSaveEdit.commit();
                            // 아이디/비밀번호 저장 체크 박스 상태를 true로 저장
                            autoSaveEdit.putBoolean("autoSaveCheck", true);
                            autoSaveEdit.commit();

                            // 환영 메시지 띄우기
                            Toast toast = Toast.makeText(getApplicationContext(), (CharSequence) response.body().get("msg"), Toast.LENGTH_SHORT);
                            toast.show();
                            // 메인 화면 전환
                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }

}

