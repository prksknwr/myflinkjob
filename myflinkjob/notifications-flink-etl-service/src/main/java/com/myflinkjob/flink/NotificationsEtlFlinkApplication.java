package com.myflinkjob.flink;

import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.commons.models.NotificationMessage;
import com.myflinkjob.flink.etl.NotificationsFlinkTopology;
import com.myflinkjob.flink.etl.serializers.EmailMessageSerializer;
import com.myflinkjob.flink.etl.serializers.SmsMessageSerializer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

public class NotificationsEtlFlinkApplication {

    public static void main(String[] args) {

        Properties props = KafkaConfig.getProperties();

        FlinkKafkaConsumer<String> userDataEventKafkaSource = new FlinkKafkaConsumer<>(KafkaConfig.Topics.USER_DATA, new SimpleStringSchema(), props);
        FlinkKafkaProducer<NotificationMessage> emailNotificationKafkaSink = new FlinkKafkaProducer(KafkaConfig.Topics.EMAIL_NOTIFICATIONS,  new EmailMessageSerializer(), props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
        FlinkKafkaProducer<NotificationMessage> smsNotificationKafkaSink = new FlinkKafkaProducer(KafkaConfig.Topics.SMS_NOTIFICATIONS, new SmsMessageSerializer(), props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);

        new NotificationsFlinkTopology(userDataEventKafkaSource, emailNotificationKafkaSink, smsNotificationKafkaSink)
                .execute();
    }
}
