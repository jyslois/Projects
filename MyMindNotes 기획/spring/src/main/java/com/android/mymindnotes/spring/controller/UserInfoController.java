package com.android.mymindnotes.spring.controller;

import com.android.mymindnotes.spring.mapper.UserInfoMapper;
import com.android.mymindnotes.spring.model.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    // 회원 관련 API
    // 회원가입
    @PostMapping("/api/member/add")
    @ResponseStatus(value = HttpStatus.OK)
    public void addUser(@RequestBody UserInfo userinfo) {
        // mapper의 api 호출 - api에 매핑된 SQL문 실행
        mapper.insertUser(userinfo.getEmail(), userinfo.getNickname(), userinfo.getPassword(), userinfo.getBirthyear());
    }

    // 회원 정보 가져오기(조회)
    // 이 mapper에 정의된 getUserInfo라는 API를 호출하게 될 것이고,
    // Path로 전달된 email이 파라미터로 전달될 것이다.
    // api가 호출되면, UserInfoMapper에서 해당 api와 매핑된 sql문이 수행되면서
    // 해당 조건을 갖는 UserInfo의 정보가 자바의 객체로 반환이 되고, 이것을 json형태로 전달되는 것이다.
    @GetMapping("/api/member/get/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> getUserInfo(@PathVariable("email") String email) {
        Map<String, Object> result = new HashMap<>();

        UserInfo userinfo = mapper.getUserInfo(email);

        // 존재하는 회원이라면
        if (userinfo != null) {
            result.put("code", 1000);
            result.put("회원정보", userinfo);
        // 존재하지 않는 회원이라면
        } else {
            result.put("code", 1001);
            result.put("msg", "존재하지 않는 회원입니다.");
        }

        return result;
    }

    // 회원정보 수정
    @PutMapping("/api/member/update/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateUserInfo(@PathVariable("email") String email, @RequestParam("nickname") String nickname, @RequestParam("password") String password, @RequestParam("birthyear") int birthyear) {
         mapper.updateUserInfo(email, nickname, password, birthyear);
    }

    // 회원탈퇴
    @DeleteMapping("/api/member/delete/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteUser(@PathVariable("email") String email) {
        mapper.deleteUser(email);
    }

}
