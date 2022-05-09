package com.myflinkjob.commons.models.config;

import java.util.Properties;

public class KafkaConfig {

    public static class Topics {
        public final static String USER_DATA = "users";
        public final static String EMAIL_NOTIFICATIONS = "email-notifications";
        public final static String SMS_NOTIFICATIONS = "sms-notifications";

    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect", "localhost:2181"); // Zookeeper default host:port
        properties.setProperty("group.id", "myGroup");                 // Consumer group ID
        properties.setProperty("auto.offset.reset", "earliest");

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);

        return properties;
    }

    public static Properties getProducerConsumerConfig() {
        Properties properties = getProperties();
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }
}
