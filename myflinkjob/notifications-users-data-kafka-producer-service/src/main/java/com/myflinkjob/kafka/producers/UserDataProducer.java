package com.myflinkjob.kafka.producers;

import org.apache.kafka.clients.producer.Producer;

import java.io.IOException;

public abstract class UserDataProducer {

    protected Producer<String, String> producer;

    public UserDataProducer(Producer<String, String> producer) {
        this.producer = producer;
    }
    protected abstract void send() throws IOException;
}
