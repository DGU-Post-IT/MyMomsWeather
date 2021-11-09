package com.postit.mymomsweather.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class AudioRecord {
    private String speakerID;
    @ServerTimestamp
    private Date time;
    private String audioLocation;
    private ArrayList<String> evalutaion;

    AudioRecord(){}

    public AudioRecord(String speakerID, String audioLocation, ArrayList<String> evalutaion) {
        this.speakerID = speakerID;
        this.audioLocation = audioLocation;
        this.evalutaion = evalutaion;
    }

    public String getSpeakerID() {
        return speakerID;
    }

    public void setSpeakerID(String speakerID) {
        this.speakerID = speakerID;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAudioLocation() {
        return audioLocation;
    }

    public void setAudioLocation(String audioLocation) {
        this.audioLocation = audioLocation;
    }

    public ArrayList<String> getEvalutaion() {
        return evalutaion;
    }

    public void setEvalutaion(ArrayList<String> evalutaion) {
        this.evalutaion = evalutaion;
    }
}
