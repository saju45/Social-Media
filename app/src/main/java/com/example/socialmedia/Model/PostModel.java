package com.example.socialmedia.Model;

public class PostModel {

    public static String POST_TYPE_IMAGE="image";
    public static String POST_TYPE_TEXT="text";
    public static String POST_TYPE_IMAGEANDTEXT="imageandtext";

    String postId;
    String postImg;
    String postedBy;
    String postDescription;
    String postAt;

    int postLike;
    int commentCount;

    public PostModel() {
    }

    public PostModel(String postId, String postImg, String postedBy, String postDescription, String postAt) {
        this.postId = postId;
        this.postImg = postImg;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postAt = postAt;
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

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
