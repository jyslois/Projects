package com.android.mymindnotes.data.retrofit.model.diary;

public class UserDiary {
    public int diary_number;
    public int user_index;
    public String type;
    public String date;
    public String day;
    public String situation;
    public String thought;
    public String emotion;
    public String emotionDescription;
    public String reflection;

    public UserDiary() {

    }

    public UserDiary(int user_index, String type, String date, String day, String situation, String thought, String emotion, String emotionDescription, String reflection) {
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

    public UserDiary(int diary_number, int user_index, String type, String date, String day, String situation, String thought, String emotion, String emotionDescription, String reflection) {
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
