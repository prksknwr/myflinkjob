package com.myflinkjob.flink.etl.operators.mappers;

import com.myflinkjob.commons.models.models.NotificationMessage;
import com.myflinkjob.commons.models.models.SmsMessage;
import com.myflinkjob.commons.models.models.User;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Objects;


public class SmsMessageMapper implements MapFunction<User, NotificationMessage> {
    private static Logger LOG = LoggerFactory.getLogger(SmsMessageMapper.class);

    @Override
    public NotificationMessage map(User user) {
        try {
            if (Objects.isNull(user)) return null;
            return new SmsMessage.Builder()
                    .userId(user.getId())
                    .message(user.getSmsText())
                    .phone(user.getPhone())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("An exception occurrred while mapping sms notification message record. {}", e.getMessage());
            return null;
        }
    }
}
