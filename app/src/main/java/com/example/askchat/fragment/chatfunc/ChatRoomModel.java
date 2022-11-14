package com.example.askchat.fragment.chatfunc;

public class ChatRoomModel {
    String message, time, userID;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String message, String time, String userID) {
        this.message = message;
        this.time = time;
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
