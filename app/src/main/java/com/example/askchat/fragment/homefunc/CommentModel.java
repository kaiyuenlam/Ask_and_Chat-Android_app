package com.example.askchat.fragment.homefunc;

public class CommentModel {
    String userID, comment, time;

    public CommentModel() {
    }

    public CommentModel(String userID, String comment, String time) {
        this.userID = userID;
        this.comment = comment;
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
