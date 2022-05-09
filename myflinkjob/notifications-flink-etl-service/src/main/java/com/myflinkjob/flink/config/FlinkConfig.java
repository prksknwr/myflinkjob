package com.myflinkjob.flink.config;

public class FlinkConfig {

    public final static int PARALLELISM = 1;

    public static class Labels {
        public static final String USER_DATA_KAFKA_SOURCE_STREAM_LABEL = "Kafka Source: Users Message Events";
        public static final String EMAIL_NOTIFICATION_MESSAGE_STREAM_LABEL = "Email Notificatiion Message Stream";

        public static final String SMS_NOTIFICATION_MESSAGE_STREAM_LABEL = "Sms Notificatiion Message Stream";
        public static final String EMAIL_NOTIFICATION_MESSAGE_KAFKA_SINK_LABEL = "Email Notification Message Kafka Sink";
        public static final String SMS_NOTIFICATION_MESSAGE_KAFKA_SINK_LABEL = "Sms Notification Message Kafka Sink";
        public static final String NOTIFICATION_SERVICE_FLINK_TOPOLOGY_LABEL = "Notification ETL Service";
    }

}
