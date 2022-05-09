package com.myflinkjob.kafka.consumers;

import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.commons.models.models.EmailMessage;
import com.myflinkjob.kafka.services.NotificationService;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailNotificationConsumerTest {

    @Mock
    private PollingStrategy strategy;

    @Mock
    private KafkaConsumer<String, EmailMessage> emailMessageKafkaConsumer;

    @Mock
    private NotificationService notificationService;

    private EmailNotificationConsumer classUnderTest;

    @BeforeEach
    public void setUp() throws IOException {
        when(strategy.pollForever()).thenReturn(false);
        classUnderTest = new EmailNotificationConsumer(emailMessageKafkaConsumer, notificationService);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(strategy);
        Mockito.reset(emailMessageKafkaConsumer);
        Mockito.reset(notificationService);
    }


    @Test
    public void shouldSendEmailNotifications() {

        EmailMessage messsage1 =  new EmailMessage.Builder()
                .userId(1)
                .emailAddress("test4@mail.com")
                .message("Hello user test4, this is a test message")
                .build();

        EmailMessage messsage2 =  new EmailMessage.Builder()
                .userId(1)
                .emailAddress("test5@mail.com")
                .message("Hello user test5, this is a test message")
                .build();

        ConsumerRecord<String, EmailMessage> record1  =
                new ConsumerRecord<String, EmailMessage>(KafkaConfig.Topics.EMAIL_NOTIFICATIONS, 1, 2, "tes4", messsage1);
        ConsumerRecord<String, EmailMessage> record2  =
                new ConsumerRecord<String, EmailMessage>(KafkaConfig.Topics.EMAIL_NOTIFICATIONS, 1, 2, "test5", messsage2);
        Map<TopicPartition, List<ConsumerRecord<String, EmailMessage>>>  map = new HashMap<>();
        map.put(new TopicPartition(KafkaConfig.Topics.EMAIL_NOTIFICATIONS, 0), Arrays.asList(record1, record2));



        ConsumerRecords<String, EmailMessage> records = new ConsumerRecords<>(map);
        when(emailMessageKafkaConsumer.poll(100)).thenReturn(records);
        classUnderTest.subscribe(strategy);

        verify(notificationService, times(2)).send(anyInt(), anyString(), anyString());
        verify(notificationService, times(1)).send(eq(messsage1.getUserId()), eq(messsage1.getEmailAddress()), eq(messsage1.getMessage()));
        verify(notificationService, times(1)).send(eq(messsage2.getUserId()), eq(messsage2.getEmailAddress()), eq(messsage2.getMessage()));

    }

    @Test
    public void shouldNotTriggerEmailsWhenEmailIdIsNotValid() {

        EmailMessage messsage1 =  new EmailMessage.Builder()
                .userId(1)
                .emailAddress("test4@mailcom")
                .message("Hello user test4, this is a test message")
                .build();

        EmailMessage messsage2 =  new EmailMessage.Builder()
                .userId(1)
                .emailAddress("test5mail.com")
                .message("Hello user test5, this is a test message")
                .build();

        ConsumerRecord<String, EmailMessage> record1  =
                new ConsumerRecord<String, EmailMessage>(KafkaConfig.Topics.EMAIL_NOTIFICATIONS, 1, 2, "tes4", messsage1);
        ConsumerRecord<String, EmailMessage> record2  =
                new ConsumerRecord<String, EmailMessage>(KafkaConfig.Topics.EMAIL_NOTIFICATIONS, 1, 2, "test5", messsage2);
        Map<TopicPartition, List<ConsumerRecord<String, EmailMessage>>>  map = new HashMap<>();
        map.put(new TopicPartition(KafkaConfig.Topics.EMAIL_NOTIFICATIONS, 0), Arrays.asList(record1, record2));

        ConsumerRecords<String, EmailMessage> records = new ConsumerRecords<>(map);
        when(emailMessageKafkaConsumer.poll(100)).thenReturn(records);
        classUnderTest.subscribe(strategy);

        verify(notificationService, never()).send(anyInt(), anyString(), anyString());
    }

    @Test
    public void shouldNotTriggerEmailsWhenListIsEmpty() {

        ConsumerRecords<String, EmailMessage> records = new ConsumerRecords<>(Collections.emptyMap());
        when(emailMessageKafkaConsumer.poll(100)).thenReturn(records);
        classUnderTest.subscribe(strategy);

        verify(notificationService, never()).send(anyInt(), anyString(), anyString());
    }

}