package com.myflinkjob.commons.models.models;

public class SmsMessage extends NotificationMessage{

    private final String phoneNumber;

    public SmsMessage() {
        super();
        this.phoneNumber = "";
    }

    public SmsMessage(int userId, String message, String phoneNumber) {
        super(userId, message);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static class Builder {
        private int userId;
        private String message;
        private String phone;

        public Builder() {}

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder phone(String phoneNumber) {
            this.phone = phoneNumber;
            return this;
        }

        public SmsMessage build() {
            return new SmsMessage(userId, message, phone);
        }
    }
}
