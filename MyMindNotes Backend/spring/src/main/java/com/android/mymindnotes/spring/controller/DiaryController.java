package com.android.mymindnotes.spring.controller;

import com.android.mymindnotes.spring.mapper.DiaryMapper;
import com.android.mymindnotes.spring.model.UserDiary;
import com.android.mymindnotes.spring.model.DiaryEdit;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DiaryController {
    private DiaryMapper mapper;

    public DiaryController(DiaryMapper mapper) {
        this.mapper = mapper;
    }

    // 일기 관련 API: 일기 쓰기(저장하기), 읽기(가져오기), 수정, 삭제
    // 일기 쓰기(일기 저장)
    @PostMapping("/api/diary/add")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> addDiary(@RequestBody @Valid UserDiary userDiary, Errors errors) {
        Map<String, Object> result = new HashMap<>();

        if (errors.hasErrors()) {
            for (FieldError error : errors.getFieldErrors()) {
                result.put("code", 6001);
                result.put(error.getField(), error.getDefaultMessage());
            }
        } else {
            mapper.insertDiary(userDiary.getUser_index(), userDiary.getType(), userDiary.getDate(), userDiary.getDay(), userDiary.getSituation(), userDiary.getThought(), userDiary.getEmotion(), userDiary.getEmotionDescription(), userDiary.getReflection());
            result.put("code", 6000);
        }

        return result;
    }

    // 일기 모두 가져오기
    @GetMapping("/api/diary/getAll/{user_index}")
    public Map<String, Object> getAllDiary(@PathVariable("user_index") int user_index) {
        Map<String, Object> result = new HashMap<>();
        // 회원 정보 조회
        // 조회 결과가 하나 이상이기에 List<Diary>로 반환
        ArrayList<UserDiary> userDiary = mapper.getAllDiary(user_index);
        result.put("code", 7000);
        result.put("일기목록", userDiary);
        return result;
    }

    // 특정 일기 가져오기(일기 읽기)
    @GetMapping("/api/diary/get/{diary_number}")
    public Map<String, Object> getDiary(@PathVariable("diary_number") int diary_number) {
        Map<String, Object> result = new HashMap<>();
        // 회원 정보 조회
        // 조회 결과가 하나 이상이기에 List<Diary>로 반환
        UserDiary userDiary = mapper.getDiary(diary_number);
        if (userDiary != null) {
            result.put("code", 7002);
            result.put("일기", userDiary);
        } else {
            result.put("code", 7003);
            result.put("msg", "존재하지 않는 일기입니다.");
        }
        return result;
    }

    // 일기 수정
    @PutMapping("/api/diary/update/{diary_number}")
    public Map<String, Object> updateDiary(@PathVariable("diary_number") int diary_number, @RequestBody @Valid DiaryEdit diary, Errors errors) {
        Map<String, Object> result = new HashMap<>();
        // 유효성 통과 못한 필드와 메시지를 핸들링
        if (errors.hasErrors()) {
            // 유효성 검사에 실패한 필드 목록을 가져온다: errors.getFieldErrors()
            for (FieldError error : errors.getFieldErrors()) {
                // 유효성 검사에 실패한 필드명을 가져온다. error.getField()
                // 유효성 검사에 실패한 필드에 정의된 메시지를 가져온다. error.getDefaultMessage()
                result.put("code", 8001);
                result.put(error.getField(), error.getDefaultMessage());
            }
        } else {
            mapper.updateDiary(diary_number, diary.getSituation(), diary.getThought(), diary.getEmotion(), diary.getEmotionDescription(), diary.getReflection());
            result.put("code", 8000);
        }

        return result;
    }

    // 일기 삭제
    @DeleteMapping("/api/diary/delete/{diary_number}")
    public Map<String, Object> deleteDiary(@PathVariable("diary_number") int diary_number) {
        Map<String, Object> result = new HashMap<>();
        mapper.deleteDiary(diary_number);
        result.put("code", 9000);
        return result;
    }

}



