package com.example.askchat.fragment.chatfunc;

import java.time.LocalDateTime;
import java.util.Date;

public class MessageModel {

    String message, date;
    boolean isSender;

    public MessageModel() {
    }

    public MessageModel(String message, String date, boolean isSender) {
        this.message = message;
        this.date = date;
        this.isSender = isSender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setLocalDateTime(String date) {
        this.date = date;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }
}
