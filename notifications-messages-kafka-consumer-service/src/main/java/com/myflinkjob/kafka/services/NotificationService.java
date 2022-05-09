package com.myflinkjob.kafka.services;

public class NotificationService {

    public void send(int userId, String address, String message) {
        System.out.printf("Hello %s (#%d)!%nYou've received a new message:%n%s%n", address, userId, message);
    }

}
