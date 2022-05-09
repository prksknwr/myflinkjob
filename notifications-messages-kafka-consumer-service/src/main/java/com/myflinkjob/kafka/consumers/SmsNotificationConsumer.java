package com.myflinkjob.kafka.consumers;

import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.commons.models.models.SmsMessage;
import com.myflinkjob.kafka.services.NotificationService;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import java.util.Collections;

public class SmsNotificationConsumer implements NotificationConsumer {

    private static Logger LOG = LoggerFactory.getLogger(EmailNotificationConsumer.class);

    private final KafkaConsumer<String, SmsMessage> smsMessageConsumer;
    private NotificationService notificationService;

    public SmsNotificationConsumer(KafkaConsumer<String, SmsMessage> smsMessageConsumer, NotificationService notificationService) {
        this.smsMessageConsumer = smsMessageConsumer;
        this.notificationService = notificationService;
    }
    @Override
    public void subscribe(PollingStrategy strategy) {
        synchronized (this.smsMessageConsumer) {
            this.smsMessageConsumer.subscribe(Collections.singletonList(KafkaConfig.Topics.SMS_NOTIFICATIONS));

            do {
                ConsumerRecords<String, SmsMessage> records = this.smsMessageConsumer.poll(100);
                for (ConsumerRecord<String, SmsMessage> record : records) {
                    try {
                        SmsMessage smsMessage = record.value();
                        notificationService.send(smsMessage.getUserId(), smsMessage.getPhoneNumber(), smsMessage.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.debug("An error occurred while processing sms message. {}", e.getMessage());
                    }
                }

            } while (strategy.pollForever());
        }
    }

    @Override
    public void run() {
        this.subscribe(new PollingStrategy());
    }
}
