package com.myflinkjob.flink.etl.operators.mappers;

import com.myflinkjob.commons.models.models.ContactBy;
import com.myflinkjob.commons.models.models.User;
import com.myflinkjob.flink.etl.serializers.EmailMessageSerializer;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMapper implements MapFunction<String, User> {

    private static Logger LOG = LoggerFactory.getLogger(EmailMessageSerializer.class);

    @Override
    public User map(String userInfoString) {

        try {
            if (StringUtils.isNullOrWhitespaceOnly(userInfoString)) return null;
            String[] userInfo = userInfoString.split(",");
            User user = new User.Builder()
                    .id(Integer.parseInt(userInfo[0]))
                    .active(Boolean.parseBoolean(userInfo[1]))
                    .contactBy(ContactBy.fromString(userInfo[2]))
                    .email(userInfo[3])
                    .phone(userInfo[4])
                    .emailText(userInfo[5])
                    .smsText(userInfo[6])
                    .build();
            LOG.info(user.getEmailText() + " " + user.getSmsText());
            return user;

        } catch (Exception e) {
            e.printStackTrace();
            LOG.info("An exception occurrred while mapping user record. {}", e.getMessage());
            return null;
        }

    }
}
