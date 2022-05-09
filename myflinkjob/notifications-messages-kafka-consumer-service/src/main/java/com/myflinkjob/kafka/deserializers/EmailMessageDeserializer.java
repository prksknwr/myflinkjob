package com.myflinkjob.kafka.deserializers;


import com.myflinkjob.commons.models.models.EmailMessage;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class EmailMessageDeserializer implements Deserializer<EmailMessage> {

    @Override
    public EmailMessage deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        EmailMessage emailMessage = null;
        try {
            emailMessage = mapper.readValue(bytes, EmailMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailMessage;
    }

    @Override
    public void close() {
    }


}
