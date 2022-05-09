package com.myflinkjob.flink.etl.serializers;


import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.commons.models.models.EmailMessage;
import com.sun.istack.internal.Nullable;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;


public class EmailMessageSerializer implements KafkaSerializationSchema<EmailMessage> {

    private static Logger LOG = LoggerFactory.getLogger(EmailMessageSerializer.class);

    @Override
    public ProducerRecord<byte[], byte[]> serialize(EmailMessage notificationMessage, @Nullable Long aLong) {
        try {
            byte[] message = new ObjectMapper().writeValueAsBytes(notificationMessage);
            return new ProducerRecord<>(KafkaConfig.Topics.EMAIL_NOTIFICATIONS, message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            LOG.debug("An exception occurred while serializing the producer record. {}", e.getMessage());
        }
        return null;
    }
}
