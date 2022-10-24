package com.android.mymindnotes;

import java.util.Comparator;
import java.util.Date;

public class Record {
    String emotionWord;
    int emotionCircle;
    Date date;
    String situation;
    String emotionText;
    String thought;
    String reflection;
    String type;

    Record(int emotionColor, Date date, String type, String emotion, String situation, String thought, String emotionText, String reflection) {
        this.emotionCircle = emotionColor;
        this.date = date;
        this.type = type;
        this.emotionWord = emotion;
        this.situation = situation;
        this.thought = thought;
        this.emotionText = emotionText;
        this.reflection = reflection;
    }

    public Date getDate() {
        return this.date;
    }

}
