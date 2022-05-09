package com.myflinkjob.kafka.consumers;

import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.commons.models.models.SmsMessage;
import com.myflinkjob.kafka.services.NotificationService;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SmsNotificationConsumerTest {
    @Mock
    private PollingStrategy strategy;

    @Mock
    private KafkaConsumer<String, SmsMessage> smsMessageKafkaConsumer;

    @Mock
    private NotificationService notificationService;

    private SmsNotificationConsumer classUnderTest;

    @BeforeEach
    public void setUp() throws IOException {
        when(strategy.pollForever()).thenReturn(false);
        classUnderTest = new SmsNotificationConsumer(smsMessageKafkaConsumer, notificationService);
    }


    @Test
    public void subscribeShouldSendEmailNotifications() {

        SmsMessage messsage1 =  new SmsMessage.Builder()
                .userId(1)
                .phone("+999999999")
                .message("Hello user +998988889, this is a test message")
                .build();

        SmsMessage messsage2 =  new SmsMessage.Builder()
                .userId(2)
                .phone("+888888888")
                .message("Hello user +888888888, this is a test message")
                .build();

        ConsumerRecord<String, SmsMessage> record1  =
                new ConsumerRecord<>(KafkaConfig.Topics.SMS_NOTIFICATIONS, 1, 2, "tes4", messsage1);
        ConsumerRecord<String, SmsMessage> record2  =
                new ConsumerRecord<>(KafkaConfig.Topics.SMS_NOTIFICATIONS, 1, 2, "test5", messsage2);
        Map<TopicPartition, List<ConsumerRecord<String, SmsMessage>>> map = new HashMap<>();
        map.put(new TopicPartition(KafkaConfig.Topics.SMS_NOTIFICATIONS, 0), Arrays.asList(record1, record2));



        ConsumerRecords<String, SmsMessage> records = new ConsumerRecords<>(map);
        when(smsMessageKafkaConsumer.poll(100)).thenReturn(records);
        classUnderTest.subscribe(strategy);

        verify(notificationService, times(2)).send(anyInt(), anyString(), anyString());
        verify(notificationService, times(1)).send(eq(messsage1.getUserId()), eq(messsage1.getPhoneNumber()), eq(messsage1.getMessage()));
        verify(notificationService, times(1)).send(eq(messsage2.getUserId()), eq(messsage2.getPhoneNumber()), eq(messsage2.getMessage()));

    }

    @Test
    public void subscribeShouldNotTriggerSmsMessagesWhenListIsEmpty() {

        ConsumerRecords<String, SmsMessage> records = new ConsumerRecords<>(Collections.emptyMap());
        when(smsMessageKafkaConsumer.poll(100)).thenReturn(records);
        classUnderTest.subscribe(strategy);

        verify(notificationService, never()).send(anyInt(), anyString(), anyString());
    }
}