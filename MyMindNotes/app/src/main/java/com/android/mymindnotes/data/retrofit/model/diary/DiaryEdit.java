package com.android.mymindnotes.data.retrofit.model.diary;

public class DiaryEdit {
    public String situation;
    public String thought;
    public String emotion;
    public String emotionDescription;
    public String reflection;

    public DiaryEdit() {

    }

    public DiaryEdit(String situation, String thought, String emotion, String emotionDescription, String reflection) {
        this.situation = situation;
        this.thought = thought;
        this.emotion = emotion;
        this.emotionDescription = emotionDescription;
        this.reflection = reflection;
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
