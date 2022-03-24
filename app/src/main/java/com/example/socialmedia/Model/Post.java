package com.example.socialmedia.Model;

public class Post {

    String postId;
    String postImg;
    String postedBy;
    String postDescription;
    String postAt;
    int like;


    public Post() {
    }

    public Post(String postId, String postImg, String postedBy, String postDescription, String postAt) {
        this.postId = postId;
        this.postImg = postImg;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postAt = postAt;
    }
    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getPostId() {
        return postId;
    }


    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }


    public String getPostAt() {
        return postAt;
    }

    public void setPostAt(String postAt) {
        this.postAt = postAt;
    }
}
