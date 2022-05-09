package com.myflinkjob.kafka.consumers;

public interface NotificationConsumer extends Runnable {

    void subscribe(PollingStrategy strategy);
}
