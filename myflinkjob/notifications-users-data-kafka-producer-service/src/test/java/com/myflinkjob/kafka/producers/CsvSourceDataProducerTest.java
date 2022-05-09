package com.myflinkjob.kafka.producers;

import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.kafka.models.MessageInputSettings;
import com.myflinkjob.kafka.repo.UserRepository;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvSourceDataProducerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaProducer<String, String> producer;

    private CsvSourceDataProducer classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new CsvSourceDataProducer(userRepository, producer, new MessageInputSettings("test email", "test sms"));
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(userRepository);
    }

    @Test
    public void shouldSendAllUserInfosToKafkaQueue() throws IOException {
        when(userRepository.getAllUsersFromCsv()).thenReturn(
                Arrays.asList(
                        "1,true,email,test1@mail.com,+999999999999",
                        "2,true,none,test2@mail.com,+999999999998",
                        "3,false,phone,test3mail.com,+999999999997",
                        "4,true,email,test4@mail.com,+999999999996",
                        "5,true,all,test4@mail.com,+999999999996"
                )
        );

        classUnderTest.send();

        verify(producer, times (5)).send(any(ProducerRecord.class));

        verify(producer, times(1)).send(eq(new ProducerRecord<>(KafkaConfig.Topics.USER_DATA, "1,true,email,test1@mail.com,+999999999999,test email,test sms")));
        verify(producer, times(1)).send(eq(new ProducerRecord<>(KafkaConfig.Topics.USER_DATA, "2,true,none,test2@mail.com,+999999999998,test email,test sms")));
        verify(producer, times(1)).send(eq(new ProducerRecord<>(KafkaConfig.Topics.USER_DATA, "3,false,phone,test3mail.com,+999999999997,test email,test sms")));
        verify(producer, times(1)).send(eq(new ProducerRecord<>(KafkaConfig.Topics.USER_DATA, "4,true,email,test4@mail.com,+999999999996,test email,test sms")));
        verify(producer, times(1)).send(eq(new ProducerRecord<>(KafkaConfig.Topics.USER_DATA, "5,true,all,test4@mail.com,+999999999996,test email,test sms")));

    }

    @Test
    void shouldBehaveFailSafeAndSkipSendingMessageToKafkaQueueWhenIOExceptionOccurs() throws IOException {
        when(userRepository.getAllUsersFromCsv()).thenThrow(IOException.class);
        verify(producer, never()).send(any(ProducerRecord.class));
    }

    @Test
    void shouldSkipSendingMessageToKafkaQueueWhenUsersListIsEmpty() throws IOException {
        when(userRepository.getAllUsersFromCsv()).thenReturn(new ArrayList<>());
        verify(producer, never()).send(any(ProducerRecord.class));
    }
}