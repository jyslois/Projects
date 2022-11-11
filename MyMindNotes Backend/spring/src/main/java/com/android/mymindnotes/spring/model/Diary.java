package com.android.mymindnotes.spring.model;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Diary {
    @GeneratedValue
    private int diary_number;
    @NotNull(message = "회원 번호를 입력해 주세요.") // int 타입은 NotNull, String 타입은 NotBlank
    private int user_index;
    @NotBlank(message = "일기 타입을 입력해 주세요.")
    private String type;
    @NotBlank(message = "날짜를 입력해 주세요.")
    private String date;
    @NotBlank(message = "요일을 입력해 주세요.")
    private String day;
    @NotBlank(message = "상황을 입력해 주세요.")
    private String situation;
    @NotBlank(message = "생각을 입력해 주세요.")
    private String thought;
    @NotBlank(message = "감정을 입력해 주세요.")
    private String emotion;
    private String emotionDescription;
    private String reflection;

    // 오류 방지를 위해 빈 생성자 추가
    public Diary() {

    }

    public Diary(int diary_number, int user_index, String type, String date, String day, String situation, String thought, String emotion, String emotionDescription, String reflection) {
        this.diary_number = diary_number;
        this.user_index = user_index;
        this.type = type;
        this.date = date;
        this.day = day;
        this.situation = situation;
        this.thought = thought;
        this.emotion = emotion;
        this.emotionDescription = emotionDescription;
        this.reflection = reflection;
    }

    public Diary(int user_index, String type, String date, String day, String situation, String thought, String emotion, String emotionDescription, String reflection) {
        this.user_index = user_index;
        this.type = type;
        this.date = date;
        this.day = day;
        this.situation = situation;
        this.thought = thought;
        this.emotion = emotion;
        this.emotionDescription = emotionDescription;
        this.reflection = reflection;
    }

    public int getDiary_number() {
        return diary_number;
    }

    public void setDiary_number(int diary_number) {
        this.diary_number = diary_number;
    }

    public int getUser_index() {
        return user_index;
    }

    public void setUser_index(int user_index) {
        this.user_index = user_index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getEmotionDescription() {
        return emotionDescription;
    }

    public void setEmotionDescription(String emotionDescription) {
        this.emotionDescription = emotionDescription;
    }

    public String getReflection() {
        return reflection;
    }

    public void setReflection(String reflection) {
        this.reflection = reflection;
    }
}
