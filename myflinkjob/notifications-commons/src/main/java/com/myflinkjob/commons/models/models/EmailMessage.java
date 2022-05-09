package com.myflinkjob.commons.models.models;

public class EmailMessage extends NotificationMessage {

    private final String emailAddress;

    public EmailMessage() {
        super();
        this.emailAddress = "";
    }

    public EmailMessage(int userId, String message, String emailAddress) {
        super(userId, message);
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public static class Builder {
        private int userId;
        private String message;
        private String emailAddress;

        public Builder() {}

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public EmailMessage build() {
            return new EmailMessage(userId, message, emailAddress);
        }
    }
}
