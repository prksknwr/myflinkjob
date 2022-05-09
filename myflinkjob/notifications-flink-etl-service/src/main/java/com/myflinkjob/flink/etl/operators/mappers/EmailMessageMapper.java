package com.myflinkjob.flink.etl.operators.mappers;


import com.myflinkjob.commons.models.models.EmailMessage;
import com.myflinkjob.commons.models.models.NotificationMessage;
import com.myflinkjob.commons.models.models.User;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Objects;

public class EmailMessageMapper implements MapFunction<User, NotificationMessage> {
    private static Logger LOG = LoggerFactory.getLogger(EmailMessageMapper.class);

    @Override
    public NotificationMessage map(User user) {
        try {

            if (Objects.isNull(user)) return null;
            return new EmailMessage.Builder()
                    .userId(user.getId())
                    .message(user.getEmailText())
                    .emailAddress(user.getEmail())
                    .build();

        } catch(Exception e) {
            e.printStackTrace();
            LOG.debug("An exception occurrred while mapping email notification message. {}", e.getMessage());
            return null;
        }

    }

}