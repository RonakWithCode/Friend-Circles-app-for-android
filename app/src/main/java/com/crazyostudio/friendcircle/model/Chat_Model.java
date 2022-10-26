package com.crazyostudio.friendcircle.model;

public class Chat_Model {
    private String ID,Message;
    private boolean IsImage;


    public Chat_Model(){}
    public Chat_Model(String ID, String message) {
        this.ID = ID;
        Message = message;
    }


    public boolean isImage() {
        return IsImage;
    }

    public Chat_Model(String ID, String message, boolean isImage) {
        this.ID = ID;
        Message = message;
        IsImage = isImage;
    }

    public void setImage(boolean image) {
        IsImage = image;
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

}
