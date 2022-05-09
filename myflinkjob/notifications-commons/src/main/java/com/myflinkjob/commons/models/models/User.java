package com.myflinkjob.commons.models.models;

public class User {
    private final String emailText;
    private final String smsText;
    private int id;
    private boolean active;
    private ContactBy contactBy;
    private String email;
    private String phone;

    public User(int id, boolean active, ContactBy contactBy, String email, String phone, String emailText, String smsText) {
        this.id = id;
        this.active = active;
        this.contactBy = contactBy;
        this.email = email;
        this.phone = phone;
        this.emailText = emailText;
        this.smsText = smsText;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public ContactBy getContactBy() {
        return contactBy;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmailText() {
        return emailText;
    }

    public String getSmsText() {
        return smsText;
    }

    public static class Builder {

        private int id;
        private boolean active;
        private ContactBy contactBy;
        private String email;
        private String phone;
        private String emailText;
        private String smsText;

        public Builder() {}

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder contactBy(ContactBy contactBy) {
            this.contactBy = contactBy;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder smsText(String text) {
            this.smsText = text;
            return this;
        }

        public Builder emailText(String text) {
            this.emailText = text;
            return this;
        }

        public User build() {
             return new User(id, active, contactBy, email, phone, emailText, smsText);
        }


    }
}
