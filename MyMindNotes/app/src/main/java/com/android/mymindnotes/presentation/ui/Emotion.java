package com.android.mymindnotes.presentation.ui;

public class Emotion {
    String emotion;
    int emotionIcon;
    int instruction;

    Emotion(String emotion, int emotionIcon, int instruction) {
        this.emotion = emotion;
        this.emotionIcon = emotionIcon;
        this.instruction = instruction;
    }

    public String getEmotion() {
        return this.emotion;
    }

}
