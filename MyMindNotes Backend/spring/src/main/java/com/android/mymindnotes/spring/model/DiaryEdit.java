package com.android.mymindnotes.spring.model;
import javax.validation.constraints.NotBlank;

public class DiaryEdit {
    @NotBlank(message = "상황을 입력해 주세요.")
    private String situation;
    @NotBlank(message = "생각을 입력해 주세요.")
    private String thought;
    @NotBlank(message = "감정을 입력해 주세요.")
    private String emotion;
    private String emotionDescription;
    private String reflection;

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
