package com.myflinkjob.flink.etl;

import com.myflinkjob.commons.models.models.NotificationMessage;
import com.myflinkjob.commons.models.models.User;
import com.myflinkjob.flink.config.FlinkConfig;
import com.myflinkjob.flink.etl.operators.filters.ActiveUserFilter;
import com.myflinkjob.flink.etl.operators.filters.ContactByFilter;
import com.myflinkjob.flink.etl.operators.filters.EmailUserFilter;
import com.myflinkjob.flink.etl.operators.filters.SmsUserFilter;
import com.myflinkjob.flink.etl.operators.mappers.EmailMessageMapper;
import com.myflinkjob.flink.etl.operators.mappers.SmsMessageMapper;
import com.myflinkjob.flink.etl.operators.mappers.UserMapper;
import com.myflinkjob.flink.etl.serializers.EmailMessageSerializer;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Objects;

public class NotificationsFlinkTopology {

    private static Logger LOG = LoggerFactory.getLogger(EmailMessageSerializer.class);

    private SourceFunction<String> userDataEventKafkaSource;
    private SinkFunction<NotificationMessage> emailNotificationKafkaSink;
    private SinkFunction<NotificationMessage> smsNotificationKafkaSink;

    public NotificationsFlinkTopology(SourceFunction<String> userDataEventKafkaSource, SinkFunction<NotificationMessage> emailNotificationKafkaSink, SinkFunction<NotificationMessage> smsNotificationKafkaSink) {

        this.userDataEventKafkaSource = userDataEventKafkaSource;
        this.emailNotificationKafkaSink = emailNotificationKafkaSink;
        this.smsNotificationKafkaSink = smsNotificationKafkaSink;
    }

    public void execute() {
        try {

            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

            DataStream<User> userDataEventStream = env.addSource(this.userDataEventKafkaSource)
                    .map(new UserMapper())
                    .filter(new ActiveUserFilter())
                    .filter(new ContactByFilter())
                    .setParallelism(FlinkConfig.PARALLELISM)
                    .name(FlinkConfig.Labels.USER_DATA_KAFKA_SOURCE_STREAM_LABEL)
                    .keyBy(user -> user.getId());

            DataStream<NotificationMessage> emailMessageDataStream = userDataEventStream
                    .filter(new EmailUserFilter())
                    .map(new EmailMessageMapper())
                    .filter(Objects::nonNull)
                    .name(FlinkConfig.Labels.EMAIL_NOTIFICATION_MESSAGE_STREAM_LABEL);

            DataStream<NotificationMessage> smsMessageDataStream = userDataEventStream
                    .filter(new SmsUserFilter())
                    .map(new SmsMessageMapper())
                    .filter(Objects::nonNull)
                    .name(FlinkConfig.Labels.SMS_NOTIFICATION_MESSAGE_STREAM_LABEL);

            emailMessageDataStream.addSink(this.emailNotificationKafkaSink)
                    .name(FlinkConfig.Labels.EMAIL_NOTIFICATION_MESSAGE_KAFKA_SINK_LABEL);

            smsMessageDataStream.addSink(this.smsNotificationKafkaSink)
                    .name(FlinkConfig.Labels.SMS_NOTIFICATION_MESSAGE_KAFKA_SINK_LABEL);

            env.execute(FlinkConfig.Labels.NOTIFICATION_SERVICE_FLINK_TOPOLOGY_LABEL);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("An exception occurred during streaming user data in the flink topolgoy. {}", e.getMessage());
        }
    }
}
