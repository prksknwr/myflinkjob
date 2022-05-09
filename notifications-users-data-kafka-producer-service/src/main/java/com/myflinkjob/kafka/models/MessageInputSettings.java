package com.myflinkjob.kafka.models;

public class MessageInputSettings {

    private String email;
    private String sms;

    public MessageInputSettings(String email, String sms) {
        this.email = email;
        this.sms = sms;
    }

    public String getEmail() {
        return email;
    }

    public String getSms() {
        return sms;
    }
}
