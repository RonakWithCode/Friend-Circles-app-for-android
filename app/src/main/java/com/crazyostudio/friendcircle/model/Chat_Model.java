package com.crazyostudio.friendcircle.model;

public class Chat_Model {
    private String ID,Message;
    long time;

    public Chat_Model(String ID, String message, long time) {
        this.ID = ID;
        Message = message;
        this.time = time;
    }
    public Chat_Model(){}
    public Chat_Model(String ID, String message) {
        this.ID = ID;
        Message = message;
    }
    public Chat_Model(String message, long time) {
        Message = message;
        this.time = time;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
