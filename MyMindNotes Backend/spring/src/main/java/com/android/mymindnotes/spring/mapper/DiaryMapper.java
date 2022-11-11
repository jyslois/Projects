package com.android.mymindnotes.spring.mapper;

import com.android.mymindnotes.spring.model.Diary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DiaryMapper {
    // 일기 쓰기(저장)
    @Insert("INSERT INTO Diary(`user_index`, `type`, `date`, `day`, `situation`, `thought`, `emotion`, `emotionDescription`, `reflection`) VALUES(#{user_index}, #{type}, #{date}, #{day}, #{situation}, #{thought}, #{emotion}, #{emotionDescription}, #{reflection})")
    int insertDiary(@Param("user_index") int user_index, @Param("type") String type, @Param("date") String date, @Param("day") String day, @Param("situation") String situation, @Param("thought") String thought, @Param("emotion") String emotion, @Param("emotionDescription") String emotionDescription, @Param("reflection") String reflection);

    // 일기 모두 가져오기(읽기) - index로 일기 조회
    // 조회 결과가 하나 이상이기에 List<Diary>로 반환
    @Select("SELECT * FROM Diary WHERE user_index=#{user_index}")
    public List<Diary> getAllDiary(@Param("user_index") int user_index);

    // 일기 수정
    @Update("UPDATE Diary SET situation=#{situation}, thought=#{thought}, emotion=#{emotion}, emotionDescription=#{emotionDescription}, reflection=#{reflection} WHERE diary_number=#{diary_number}")
    int updateDiary(@Param("diary_number") int diary_number, @Param("situation") String situation, @Param("thought") String thought, @Param("emotion") String emotion, @Param("emotionDescription") String emotionDescription, @Param("reflection") String reflection);

    // 일기 삭제
    @Delete("DELETE FROM Diary WHERE diary_number=#{diary_number}")
    int deleteDiary(@Param("diary_number") int diary_number);


}
