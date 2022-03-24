package com.example.socialmedia.Model;

public class FollowModel {

    String followedBy;
    private long followedArt;

    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }

    public long getFollowedArt() {
        return followedArt;
    }

    public void setFollowedArt(long followedArt) {
        this.followedArt = followedArt;
    }
}
