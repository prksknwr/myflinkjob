package com.myflinkjob.kafka;


import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.kafka.consumers.EmailNotificationConsumer;
import com.myflinkjob.kafka.consumers.SmsNotificationConsumer;
import com.myflinkjob.kafka.deserializers.EmailMessageDeserializer;
import com.myflinkjob.kafka.deserializers.SmsMessageDeserializer;
import com.myflinkjob.kafka.services.NotificationService;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class NotificationMessagesConsumerApplication {

    public static void main(String[] args) {

        NotificationService notificationService = new NotificationService();

        new Thread(
                new EmailNotificationConsumer(
                        new KafkaConsumer<>(KafkaConfig.getProperties(), new StringDeserializer(), new EmailMessageDeserializer())
                        , notificationService
                )
        ).start();

        new Thread(
                new SmsNotificationConsumer(
                        new KafkaConsumer<>(KafkaConfig.getProperties(), new StringDeserializer(), new SmsMessageDeserializer()
                        ), notificationService
                )
        ).start();
    }

}
