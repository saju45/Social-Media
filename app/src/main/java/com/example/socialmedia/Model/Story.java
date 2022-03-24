package com.example.socialmedia.Model;

import java.util.ArrayList;

public class Story {

    String storyBy;
    long storyAt;
    ArrayList<UserStories> arrayList;

    public Story() {
    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UserStories> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<UserStories> arrayList) {
        this.arrayList = arrayList;
    }
}
