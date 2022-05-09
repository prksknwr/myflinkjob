package com.myflinkjob.flink.etl.serializers;


import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.commons.models.models.SmsMessage;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class SmsMessageSerializer implements KafkaSerializationSchema<SmsMessage> {

    private static Logger LOG = LoggerFactory.getLogger(SmsMessageSerializer.class);

    @Override
    public ProducerRecord<byte[], byte[]> serialize(SmsMessage smsMessage, @Nullable Long aLong) {
        try {
            byte[] message = new ObjectMapper().writeValueAsBytes(smsMessage);
            return new ProducerRecord<>(KafkaConfig.Topics.SMS_NOTIFICATIONS, message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            LOG.info("An exception occurred while serializing the producer record. {}", e.getMessage());
        }
        return null;
    }
}
