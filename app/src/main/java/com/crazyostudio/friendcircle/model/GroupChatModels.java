package com.crazyostudio.friendcircle.model;

public class GroupChatModels {
    private String SanderId,Message,SanderName,SanderBio,SanderImage,SanderStory;
    private long SandTime,Saw;
    private boolean isSand,isSaw,isGroup;

    public GroupChatModels(String sanderId, String message, String sanderName, String sanderBio, String sanderImage, String sanderStory, long sandTime, long saw, boolean isSand, boolean isSaw) {
        SanderId = sanderId;
        Message = message;
        SanderName = sanderName;
        SanderBio = sanderBio;
        SanderImage = sanderImage;
        SanderStory = sanderStory;
        SandTime = sandTime;
        Saw = saw;
        this.isSand = isSand;
        this.isSaw = isSaw;
    }

    public GroupChatModels(String sanderId, String message, String sanderName, String sanderBio, String sanderImage, String sanderStory) {
        SanderId = sanderId;
        Message = message;
        SanderName = sanderName;
        SanderBio = sanderBio;
        SanderImage = sanderImage;
        SanderStory = sanderStory;
    }
    public GroupChatModels(){}

    public String getSanderId() {
        return SanderId;
    }

    public void setSanderId(String sanderId) {
        SanderId = sanderId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
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

    public long getSaw() {
        return Saw;
    }

    public void setSaw(long saw) {
        Saw = saw;
    }

    public boolean isSand() {
        return isSand;
    }

    public void setSand(boolean sand) {
        isSand = sand;
    }

    public boolean isSaw() {
        return isSaw;
    }

    public void setSaw(boolean saw) {
        isSaw = saw;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
