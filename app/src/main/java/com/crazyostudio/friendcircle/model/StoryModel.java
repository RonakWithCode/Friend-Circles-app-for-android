package com.crazyostudio.friendcircle.model;

public class StoryModel {
    private String StoryUri,StoryType,StoryShortMsg;
    private long StoryStartTime;

    public StoryModel(){}
    public StoryModel(String storyUri, String storyType, String storyShortMsg, long storyStartTime) {
        StoryUri = storyUri;
        StoryType = storyType;
        StoryShortMsg = storyShortMsg;
        StoryStartTime = storyStartTime;
    }
    public String getStoryUri() {
        return StoryUri;
    }

    public void setStoryUri(String storyUri) {
        StoryUri = storyUri;
    }

    public String getStoryType() {
        return StoryType;
    }

    public void setStoryType(String storyType) {
        StoryType = storyType;
    }

    public String getStoryShortMsg() {
        return StoryShortMsg;
    }

    public void setStoryShortMsg(String storyShortMsg) {
        StoryShortMsg = storyShortMsg;
    }

    public long getStoryStartTime() {
        return StoryStartTime;
    }

    public void setStoryStartTime(long storyStartTime) {
        StoryStartTime = storyStartTime;
    }
}
