package com.myflinkjob.kafka.deserializers;


import com.myflinkjob.commons.models.models.SmsMessage;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class SmsMessageDeserializer implements Deserializer<SmsMessage> {
    @Override
    public void close() {
    }

    @Override
    public SmsMessage deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        SmsMessage smsMessage = null;
        try {
            smsMessage = mapper.readValue(bytes, SmsMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return smsMessage;
    }
}
