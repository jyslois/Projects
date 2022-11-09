package com.android.mymindnotes.spring.mapper;

import com.android.mymindnotes.spring.model.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoMapper {
    @Mapper
    // API 작성 & 해당 API가 호출됐을 떄 매핑되어 실행될 SQL문 작성.

    // 회원정보 조회 (아이디를 가지고 데이터베이스를 조회해서 UserInfo 객체를 리턴하는 Api
    // 이 Api를 Mapping할 SQL문: parameter로 전달된 email이 {}안의 email로 전달(매핑)
    // 만약에 이메일이 e@hotmail.com이라면,
    // WHERE email = e@hotmail.com인 조건을 갖는
    // row정보를 MySQL의 테이블UserInfo로부터 가지고 와서, 각 콜럼 이름 (email, nickname, password, birthyear)과 동일한
    // UserInfo의 멤버변수에 해당 정보를 집어넣게 된다. (해당 조건을 갖는 UserInfo의 정보가 자바의 객체로 반환이 되고, 이것을 json형태로 전달되는 것이다. )
    @Select("SELECT * FROM UserInfo WHERE email=#{email}")
    UserInfo getUserInfo(@Param("email") String email);

    // Insert, Update, Delete를 사용하는 API의 반환 타입은 int로,
    // 해당 SQL문으로 인해서 영향을 받은 SQL record의 갯수가 반환된다. (정상적이라면 모두 1이 반환)
    // 회원가입
    @Insert("INSERT INTO UserInfo(`email`, `nickname`, `password`, `birthyear`) VALUES(#{email}, #{nickname}, #{password}, #{birthyear})")
    int insertUser(@Param("email") String email, @Param("nickname") String nickname, @Param("password") String password, @Param("birthyear") int birthyear);

    // 회원정보 수정
    @Update("UPDATE UserInfo SET email=#{email}, nickname=#{nickname}, password=#{password}, birthyear=#{birthyear} WHERE email=#{email}")
    int updateUserInfo(@Param("email") String email, @Param("nickname") String nickname, @Param("password") String password, @Param("birthyear") int birthyear);

    // 회원탈퇴
    @Delete("DELETE FROM UserInfo WHERE email=#{email}")
    int deleteUser(@Param("email") String email);
}
