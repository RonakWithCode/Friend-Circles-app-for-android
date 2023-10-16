package com.crazyostudio.friendcircle.model;

import java.util.ArrayList;

public class SubjectModel {
    private String SubName,Uri;
    private long time;
    private ArrayList<String> notes;

    public SubjectModel() {
    }

    public SubjectModel(String subName, String uri, long time ,ArrayList<String> Notes) {
        SubName = subName;
        Uri = uri;
        this.time = time;
        notes = Notes;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
