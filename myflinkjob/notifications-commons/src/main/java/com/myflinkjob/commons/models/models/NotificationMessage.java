package com.myflinkjob.commons.models.models;

public class NotificationMessage {

    private int userId;
    private String message;

    public NotificationMessage() {
    }

    public NotificationMessage(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
