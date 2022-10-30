package com.crazyostudio.friendcircle.model;

public class Chat_Model {
    private boolean IsImage;
    private String ID,Message,SanderName,SanderBio,SanderImage,SanderStory;
    private long SandTime;
    private boolean isGroup;

    public Chat_Model(){}
    public Chat_Model(String ID, String message) {
        this.ID = ID;
        Message = message;
    }


    public Chat_Model(String ID, String message, boolean isImage, String sanderName, String sanderBio, String sanderImage, boolean isGroup) {
        this.ID = ID;
        Message = message;
        IsImage = isImage;
        SanderName = sanderName;
        SanderBio = sanderBio;
        SanderImage = sanderImage;
        this.isGroup = isGroup;
    }

    public Chat_Model(String sanderBio, String sanderImage, String sanderStory, long sandTime, boolean isGroup) {
        SanderBio = sanderBio;
        SanderImage = sanderImage;
        SanderStory = sanderStory;
        SandTime = sandTime;
        this.isGroup = isGroup;
    }

    public Chat_Model(String ID, String message, boolean isImage) {
        this.ID = ID;
        Message = message;
        IsImage = isImage;
    }

    public boolean isImage() {
        return IsImage;
    }




    public String getSanderName() {
        return SanderName;
    }

    public void setSanderName(String sanderName) {
        SanderName = sanderName;
    }

    public String getSanderBio() {
        return SanderBio;
    }

    public void setSanderBio(String sanderBio) {
        SanderBio = sanderBio;
    }

    public String getSanderImage() {
        return SanderImage;
    }

    public void setSanderImage(String sanderImage) {
        SanderImage = sanderImage;
    }

    public String getSanderStory() {
        return SanderStory;
    }

    public void setSanderStory(String sanderStory) {
        SanderStory = sanderStory;
    }

    public long getSandTime() {
        return SandTime;
    }

    public void setSandTime(long sandTime) {
        SandTime = sandTime;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
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
