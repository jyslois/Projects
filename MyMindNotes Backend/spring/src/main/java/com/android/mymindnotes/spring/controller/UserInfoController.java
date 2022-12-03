package com.android.mymindnotes.spring.controller;

import com.android.mymindnotes.spring.mapper.UserInfoMapper;
import com.android.mymindnotes.spring.model.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

// 회원 관련 API
@RestController
public class UserInfoController {
    // Mapper 사용하기: API 호출
    private UserInfoMapper mapper;
    private JavaMailSender javaMailSender;

    // 생성자를 통해서 전달받은 mapper를 내부 mapper에 저장
    // 이렇게 하면 스프링부트가 알아서 mapper 클래스를 만들고,
    // 그 객체를 UserInfoController를 생성하면서 생성자로 전달해 준다.
    // 그러면은 우리는 이렇게 전달받은 mapper을 사용해서 api를 호출할 수 있다.

    public UserInfoController(UserInfoMapper mapper, JavaMailSender javaMailSender) {
        this.mapper = mapper;
        this.javaMailSender = javaMailSender;
    }

    // 이메일 중복 체크
    @GetMapping("/api/member/checkEmail/{email}")
    public Map<String, Object> checkEmail(@PathVariable("email") String email) {
        Map<String, Object> result = new HashMap<>();
        // 회원 정보 조회
        UserInfo userinfo = mapper.getUserInfoFromEmail(email);
        // 존재하는 회원이라면
        if (userinfo != null) {
            result.put("code", 1001);
            result.put("msg", "이미 가입되어 있는 이메일입니다");
            // 존재하지 않는 회원이라면
        } else {
            result.put("code", 1000);
            result.put("msg", "사용 가능한 이메일입니다");
        }
        return result;
    }


    // 닉네임 중복 체크
    @GetMapping("/api/member/checkNickname/{nickname}")
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
    public Map<String, Object> addUser(@RequestBody @Valid UserInfo userinfo, Errors errors) {
        Map<String, Object> result = new HashMap<>();

        // 유효성 통과 못한 필드와 메시지를 핸들링
        if (errors.hasErrors()) {
            // 유효성 검사에 실패한 필드 목록을 가져온다: errors.getFieldErrors()
            for (FieldError error : errors.getFieldErrors()) {
                // 유효성 검사에 실패한 필드명을 가져온다. error.getField()
                // 유효성 검사에 실패한 필드에 정의된 메시지를 가져온다. error.getDefaultMessage()
                result.put("code", 2001);
                result.put("msg", error.getDefaultMessage());
            }
        } else {
            mapper.insertUser(userinfo.getEmail(), userinfo.getNickname(), userinfo.getPassword(), userinfo.getBirthyear());
            result.put("code", 2000);
            // user index를 얻기 위한 조회
            UserInfo user = mapper.getUserInfoFromEmail(userinfo.getEmail());
            result.put("user_index", user.getUser_index());
            result.put("msg", "회원 가입 성공! 환영합니다");
        }

        return result;
    }

    // 닉네임 수정
    @PutMapping("/api/member/update/nickname")
    public Map<String, Object> updateUserNickname(@RequestBody @Valid ChangeUserNickname changeUserNickname, Errors errors) {
        Map<String, Object> result = new HashMap<>();
        // 유효성 통과 못한 필드와 메시지를 핸들링
        if (errors.hasErrors()) {
            // 유효성 검사에 실패한 필드 목록을 가져온다: errors.getFieldErrors()
            for (FieldError error : errors.getFieldErrors()) {
                // 유효성 검사에 실패한 필드명을 가져온다. error.getField()
                // 유효성 검사에 실패한 필드에 정의된 메시지를 가져온다. error.getDefaultMessage()
                result.put("code", 3001);
                result.put("msg", error.getDefaultMessage());
            }
        } else {
            mapper.updateUserNickname(changeUserNickname.getUser_index(), changeUserNickname.getNickname());
            result.put("code", 3000);
            result.put("msg", "닉네임이 변경되었습니다");
        }
        return result;
    }

    // 비밀번호 수정
    @PutMapping("/api/member/update/password")
    public Map<String, Object> updateUserPassword(@RequestBody @Valid ChangeUserPassword changeUserPassword, Errors errors) {
        Map<String, Object> result = new HashMap<>();
        // 인덱스로 회원 정보 조회
        UserInfo userinfo = mapper.getUserInfoFromUserIndex(changeUserPassword.getUser_index());
        // 만약 기존 비밀번호가 틀리다면
        if (!userinfo.getPassword().equals(changeUserPassword.getOriginalpassword())) {
            result.put("code", 3005);
            result.put("msg", "기존 비밀번호를 확인해 주세요");
        } else {
            // 유효성 통과 못한 필드와 메시지를 핸들링
            if (errors.hasErrors()) {
                // 유효성 검사에 실패한 필드 목록을 가져온다: errors.getFieldErrors()
                for (FieldError error : errors.getFieldErrors()) {
                    // 유효성 검사에 실패한 필드명을 가져온다. error.getField()
                    // 유효성 검사에 실패한 필드에 정의된 메시지를 가져온다. error.getDefaultMessage()
                    result.put("code", 3003);
                    result.put("msg", error.getDefaultMessage());
                }
            } else {
                mapper.updateUserPassword(changeUserPassword.getUser_index(), changeUserPassword.getPassword());
                result.put("code", 3002);
                result.put("msg", "비밀번호가 변경되었습니다");
            }
        }
        return result;
    }


    // 임시 비밀번호로 비밀번호 수정
    @PutMapping("/api/member/update/password/temp")
    public Map<String, Object> toTemPassword(@RequestBody ChangeToTemporaryPassword changeToTemporaryPassword) throws MessagingException {
        Map<String, Object> result = new HashMap<>();
        // 이메일로 회원 정보 조회
        UserInfo userinfo = mapper.getUserInfoFromEmail(changeToTemporaryPassword.getEmail());
        // 존재하는 회원이라면
        if (userinfo != null) {
            // 비밀번호 변경
            mapper.toTemPassword(changeToTemporaryPassword.getEmail(), changeToTemporaryPassword.getPassword());
            result.put("code", 3006);
            result.put("msg", "전송 완료");

            // 이메일 보내기
            // SimpleMailMessage (단순 텍스트 구성 메일 메시지 생성할 때 이용)
            SimpleMailMessage simpleMessage = new SimpleMailMessage();

            // 수신자 설정
            simpleMessage.setTo(changeToTemporaryPassword.getEmail());

            // 메일 제목
            simpleMessage.setSubject("나의 마음 일지 임시 비밀번호입니다.");

            // 메일 내용
            simpleMessage.setText("귀하의 임시 비밀번호는 \"" + changeToTemporaryPassword.getPassword() + "\"입니다. \n\n로그인 후 계정 설정에서 비밀번호를 꼭 변경해 주세요.");

            // 메일 발송
            javaMailSender.send(simpleMessage);

        // 존재하지 않는 회원이라면
        } else {
            result.put("code", 3007);
            result.put("msg", "가입되어 있지 않은 이메일입니다");
        }
        return result;
    }


    // 회원탈퇴
    @DeleteMapping("/api/member/delete/{user_index}")
    public Map<String, Object> deleteUser(@PathVariable("user_index") int user_index) {
        Map<String, Object> result = new HashMap<>();
        mapper.deleteAllDiary(user_index);
        mapper.deleteUser(user_index);
        result.put("code", 4000);
        result.put("msg", "회원 탈퇴 완료");
        return result;
    }

    // 로그인
    @PostMapping("/api/member/login")
    public Map<String, Object> login(@RequestBody @Valid UserInfoLogin userinfologin) {
        Map<String, Object> result = new HashMap<>();
        // 회원 정보 조회
        UserInfo user = mapper.getUserInfoFromEmail(userinfologin.getEmail());
        // 이메일로 조회해서 존재하는 회원이라면,
        if (user != null) {
            // 비밀번호 체크
            if (user.getPassword().equals(userinfologin.getPassword())) {
                result.put("code", 5000);
                // user index와 닉네임을 얻기 위한 조회
                UserInfo userinfo = mapper.getUserInfoFromEmail(userinfologin.getEmail());
                result.put("user_index", userinfo.getUser_index());
                result.put("nickname", userinfo.getNickname());
                result.put("msg", "환영합니다");
            } else {
                result.put("code", 5003);
                result.put("msg", "비밀번호를 확인해 주세요");
            }
        } else {
            // 존재하지 않는 회원이라면,
            result.put("code", 5001);
            result.put("msg", "가입되지 않은 이메일입니다");
        }
        return result;
    }

    // 회원번호(userindex)로 회원정보 얻기
    @GetMapping("/api/member/getUserInfo/{user_index}")
    public Map<String, Object> getUserInfo(@PathVariable("user_index") int user_index) {
        Map<String, Object> result = new HashMap<>();
        UserInfo userInfo = mapper.getUserInfoFromUserIndex(user_index);
        result.put("email", userInfo.getEmail());
        result.put("nickname", userInfo.getNickname());
        result.put("birthyear", userInfo.getBirthyear());
        return result;
    }


}




