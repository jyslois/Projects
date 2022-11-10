package com.android.mymindnotes.spring.controller;

import com.android.mymindnotes.spring.mapper.UserInfoMapper;
import com.android.mymindnotes.spring.model.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

// 회원 관련 API
@RestController
public class UserInfoController {
    // Mapper 사용하기: API 호출
    private UserInfoMapper mapper;

    // 생성자를 통해서 전달받은 mapper를 내부 mapper에 저장
    // 이렇게 하면 스프링부트가 알아서 mapper 클래스를 만들고,
    // 그 객체를 UserInfoController를 생성하면서 생성자로 전달해 준다.
    // 그러면은 우리는 이렇게 전달받은 mapper을 사용해서 api를 호출할 수 있다.

    public UserInfoController(UserInfoMapper mapper) {
        this.mapper = mapper;
    }

    // 이메일 중복 체크
    @GetMapping("/api/member/checkEmail/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> checkEmail(@PathVariable("email") String email) {
        Map<String, Object> result = new HashMap<>();
        // 회원 정보 조회
        UserInfo userinfo = mapper.getUserInfoFromEmail(email);
        // 존재하는 회원이라면
        if (userinfo != null) {
            result.put("code", 1001);
            result.put("msg", "이미 가입된 이메일입니다");
            // 존재하지 않는 회원이라면
        } else {
            result.put("code", 1000);
            result.put("msg", "사용 가능한 이메일입니다");
        }
        return result;
    }

    // 닉네임 중복 체크
    @GetMapping("/api/member/checkNickname/{nickname}")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> checkNickname(@PathVariable("nickname") String nickname) {
        Map<String, Object> result = new HashMap<>();
        // 회원 정보 조회
        UserInfo userinfo = mapper.getUserInfoFromNickname(nickname);
        // 존재하는 회원이라면
        if (userinfo != null) {
            result.put("code", 1003);
            result.put("msg", "이미 사용 중인 닉네임입니다");
            // 존재하지 않는 회원이라면
        } else {
            result.put("code", 1002);
            result.put("msg", "사용 가능한 닉네임입니다");
        }
        return result;
    }

    // 회원가입
    @PostMapping("/api/member/add")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> addUser(@RequestBody @Valid UserInfo userinfo, Errors errors) {
        Map<String, Object> result = new HashMap<>();

        // 유효성 통과 못한 필드와 메시지를 핸들링
        if (errors.hasErrors()) {
            // 유효성 검사에 실패한 필드 목록을 가져온다: errors.getFieldErrors()
            for (FieldError error : errors.getFieldErrors()) {
                // 유효성 검사에 실패한 필드명을 가져온다. error.getField()
                // 유효성 검사에 실패한 필드에 정의된 메시지를 가져온다. error.getDefaultMessage()
                result.put("code", 2001);
                result.put(error.getField(), error.getDefaultMessage());
            }
        } else {
            mapper.insertUser(userinfo.getEmail(), userinfo.getNickname(), userinfo.getPassword(), userinfo.getBirthyear());
            result.put("code", 2000);
            result.put("msg", "회원 가입 성공! 환영합니다");
        }

        return result;
    }

    // 회원정보 수정
    @PutMapping("/api/member/update/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> updateUserInfo(@PathVariable("email") String email, @RequestBody @Valid UserInfo userinfo, Errors errors) {
        Map<String, Object> result = new HashMap<>();
        // 유효성 통과 못한 필드와 메시지를 핸들링
        if (errors.hasErrors()) {
            // 유효성 검사에 실패한 필드 목록을 가져온다: errors.getFieldErrors()
            for (FieldError error : errors.getFieldErrors()) {
                // 유효성 검사에 실패한 필드명을 가져온다. error.getField()
                // 유효성 검사에 실패한 필드에 정의된 메시지를 가져온다. error.getDefaultMessage()
                result.put("code", 3001);
                result.put(error.getField(), error.getDefaultMessage());
            }
        } else {
            mapper.updateUserInfo(email, userinfo.getNickname(), userinfo.getPassword());
            result.put("code", 3000);
            result.put("msg", "회원 정보가 수정되었습니다");
        }

        return result;
    }

    // 회원탈퇴
    @DeleteMapping("/api/member/delete/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteUser(@PathVariable("email") String email) {
        Map<String, Object> result = new HashMap<>();
        mapper.deleteUser(email);
        result.put("code", 4000);
        result.put("msg", "회원 탈퇴 완료");
    }

    // 로그인

}
