package com.myflinkjob.kafka.producers;

import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.kafka.models.MessageInputSettings;
import com.myflinkjob.kafka.repo.UserRepository;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class CsvSourceDataProducer extends UserDataProducer {

    private static Logger LOG = LoggerFactory.getLogger(CsvSourceDataProducer.class);

    private UserRepository userRepository;
    private final MessageInputSettings messageInputSettings;

    public CsvSourceDataProducer(UserRepository userRepository, Producer<String, String> producer, MessageInputSettings messageInputSettings) {
        super(producer);
        this.userRepository = userRepository;
        this.messageInputSettings = messageInputSettings;
    }

    @Override
    public void send() {
        try {
            List<String> usersInfo = userRepository.getAllUsersFromCsv();

            usersInfo.forEach(userInfo -> {
                        String userInfoWithMessageText = appedMessageTexts(userInfo, messageInputSettings);
                        producer.send(new ProducerRecord<>(KafkaConfig.Topics.USER_DATA, userInfoWithMessageText));

                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("Couldn't write data to kafka. An exception occurred while reading data from csv. Please, make sure you have a valid csv file, {}", e.getMessage());
        }

    }

    private String appedMessageTexts(String userInfo, MessageInputSettings messageInput) {
        userInfo += "," +messageInputSettings.getEmail();
        userInfo += "," + messageInputSettings.getSms();
        return userInfo;
    }
}
