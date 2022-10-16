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

    // 정렬
    // 오래된순
    public static Comparator<Record> DateOldComparator = new Comparator<Record>() {
        @Override
        public int compare(Record o1, Record o2) {
            if (o1.getDate() == null || o2.getDate() == null) {
                return 0;
            }
            return o1.getDate().compareTo(o2.getDate());
        }
    };

    // 최신순
    public static Comparator<Record> DateLatestComparator = new Comparator<Record>() {
        @Override
        public int compare(Record o1, Record o2) {
            if (o1.getDate() == null || o2.getDate() == null) {
                return 0;
            }
            return o2.getDate().compareTo(o1.getDate());
//            return o1.getDate().compareTo(o2.getDate()) * -1;
        }
    };


}
