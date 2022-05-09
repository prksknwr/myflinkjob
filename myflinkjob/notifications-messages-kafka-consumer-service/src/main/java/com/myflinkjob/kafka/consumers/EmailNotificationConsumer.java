package com.myflinkjob.kafka.consumers;

import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.commons.models.models.EmailMessage;
import com.myflinkjob.kafka.services.NotificationService;
import com.myflinkjob.kafka.utils.ValidatorUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;

public class EmailNotificationConsumer implements NotificationConsumer  {

    private static Logger LOG = LoggerFactory.getLogger(EmailNotificationConsumer.class);

    private final KafkaConsumer<String, EmailMessage> emailMessageConsumer;
    private NotificationService notificationService;

    public EmailNotificationConsumer(KafkaConsumer<String, EmailMessage> emailMessageConsumer, NotificationService notificationService) {
        this.emailMessageConsumer = emailMessageConsumer;
        this.notificationService = notificationService;
    }

    @Override
    public void subscribe(PollingStrategy strategy) {
        synchronized (this.emailMessageConsumer) {
            this.emailMessageConsumer.subscribe(Collections.singletonList(KafkaConfig.Topics.EMAIL_NOTIFICATIONS));
            do {
                ConsumerRecords<String, EmailMessage> records = this.emailMessageConsumer.poll(100);
                for (ConsumerRecord<String, EmailMessage> record : records) {
                    try {
                        EmailMessage emailMessage = record.value();
                        if (ValidatorUtils.isEmailValid(emailMessage.getEmailAddress())) {
                            notificationService.send(emailMessage.getUserId(), emailMessage.getEmailAddress(), emailMessage.getMessage());
                        } else {
                            LOG.debug("An error occurred while processing email message for user {}, email {}", emailMessage.getUserId(), emailMessage.getEmailAddress());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.debug("An error occurred while processing email message. {}", e.getMessage());
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
