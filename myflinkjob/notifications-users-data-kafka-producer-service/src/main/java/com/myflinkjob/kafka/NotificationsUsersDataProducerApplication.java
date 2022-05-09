package com.myflinkjob.kafka;

import com.myflinkjob.commons.models.config.KafkaConfig;
import com.myflinkjob.kafka.models.MessageInputSettings;
import com.myflinkjob.kafka.producers.CsvSourceDataProducer;
import com.myflinkjob.kafka.repo.CsvSourceUserRepository;
import com.myflinkjob.kafka.repo.UserRepository;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.kafka.clients.producer.KafkaProducer;;

import java.io.File;
import java.io.IOException;

public class NotificationsUsersDataProducerApplication {

    private static Logger LOG = LoggerFactory.getLogger(NotificationsUsersDataProducerApplication.class);

    public static void main(String[] args) throws IOException {

        if (args[0] == null || args[0] == "") {
            LOG.debug("Couldn't write user data to kafka Queue. File path is missing in the input arguments. Producer application exiting!");
            System.exit(1);
        }

        if (args[1] == null || args[1] == "") {
            LOG.debug("Couldn't write user data to kafka Queue. Email Text is missing in the input arguments. Producer application exiting!");
            System.exit(1);
        }

        if (args[2] == null || args[2] == "") {
            LOG.debug("Couldn't write user data to kafka Queue. Sms Text is missing in the input arguments. Producer application exiting");
            System.exit(1);
        }

        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(KafkaConfig.getProducerConsumerConfig())) {

            UserRepository repository = new CsvSourceUserRepository(new File(args[0]));
            MessageInputSettings messageInput = new MessageInputSettings(args[1], args[2]);
            CsvSourceDataProducer usersProducer = new CsvSourceDataProducer(repository, kafkaProducer, messageInput);

            usersProducer.send();
        }

    }
}
