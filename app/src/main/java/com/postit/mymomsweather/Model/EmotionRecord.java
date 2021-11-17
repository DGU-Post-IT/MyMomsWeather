package com.postit.mymomsweather.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class EmotionRecord {
    int emotion;
    @ServerTimestamp
    Date time;

    public EmotionRecord(int emotion) {
        this.emotion = emotion;
    }

    public EmotionRecord() {
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

