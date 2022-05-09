package com.myflinkjob.kafka.services;

import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationServiceTest {

    private ByteArrayOutputStream outContent;
    private final PrintStream originalOut = System.out;

    private NotificationService notificationService;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        notificationService = new NotificationService();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldCallSendEmailNotification() {
        notificationService.send(1, "test4@mail.com", "test email");
        assertEquals("Hello test4@mail.com (#1)!\nYou've received a new message:\ntest email\n", outContent.toString());
    }

    @Test
    public void shouldCallSendSmsNotification() {
        notificationService.send(1, "+999999999999", "test sms");
        assertEquals("Hello +999999999999 (#1)!\nYou've received a new message:\ntest sms\n", outContent.toString());
    }

}