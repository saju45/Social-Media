package com.example.socialmedia.Model;

public class Comments {


    String commentBody;
    long CommentedAt;
    String commentedBy;

    public Comments() {
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public long getCommentedAt() {
        return CommentedAt;
    }

    public void setCommentedAt(long commentedAt) {
        CommentedAt = commentedAt;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }
}
