package com.myflinkjob.flink.etl;

import com.myflinkjob.commons.models.models.EmailMessage;
import com.myflinkjob.commons.models.NotificationMessage;
import com.myflinkjob.commons.models.models.SmsMessage;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationsFlinkTopologyTest {

    @ClassRule
    public static MiniClusterWithClientResource flinkCluster =
            new MiniClusterWithClientResource(
                    new MiniClusterResourceConfiguration.Builder()
                            .setNumberSlotsPerTaskManager(2)
                            .setNumberTaskManagers(1)
                            .build());

    @Test
    void execute() {

        UserDataEventSource userDataEventSource = new UserDataEventSource();
        EmailCollectSink emailCollectSink = new EmailCollectSink();
        SmsCollectSink smsCollectSink = new SmsCollectSink();

        NotificationsFlinkTopology flinkTopology = new NotificationsFlinkTopology(
                userDataEventSource, emailCollectSink, smsCollectSink
        );

        flinkTopology.execute();


        assertNotNull(UserDataEventSource.values);
        assertNotNull(EmailCollectSink.values);
        assertNotNull(SmsCollectSink.values);


        assertThat(UserDataEventSource.values).hasSize(6);

        assertThat(EmailCollectSink.values).hasSize(3);
        assertThat(EmailCollectSink.values).hasOnlyElementsOfType(EmailMessage.class);

        assertThat(SmsCollectSink.values).hasSize(2);
        assertThat(SmsCollectSink.values).hasOnlyElementsOfType(SmsMessage.class);

    }

    private static class UserDataEventSource implements SourceFunction<String> {

        // must be static
        public static final List<String> values = new ArrayList<>();

        @Override
        public void run(SourceContext<String> sourceContext) throws Exception {
            String userCsvData = "1,true,email,test1@mail.com,+999999999999,test email,test sms,\n" +
                    "2,true,none,test2@mail.com,+999999999998,test email,test sms,\n" +
                    "3,false,phone,test3mail.com,+999999999997,test email,test sms,\n" +
                    "4,true,email,test4@mail.com,+999999999996,test email,test sms,\n" +
                    "5,true,all,test5@mail.com,+999999999996,test email,test sms,\n" +
                    "6,true,phone,test6@mail.com,+999999999996,test email,test sms";

            String[] userInfo = userCsvData.split(("\n"));
            Arrays.stream(userInfo)
                    .forEach(user -> {

                        if (!StringUtils.isBlank(user)) {
                            sourceContext.collect(user);
                            values.add(user);
                        }

                    });
        }

        @Override
        public void cancel() {

        }
    }

    private static class EmailCollectSink implements SinkFunction<NotificationMessage> {

        // must be static
        public static final List<NotificationMessage> values = new ArrayList<>();

        @Override
        public synchronized void invoke(NotificationMessage value, SinkFunction.Context context) throws Exception {
            values.add(value);
        }
    }

    private static class SmsCollectSink implements SinkFunction<NotificationMessage> {

        // must be static
        public static final List<NotificationMessage> values = new ArrayList<>();

        @Override
        public synchronized void invoke(NotificationMessage value, SinkFunction.Context context) throws Exception {
            values.add(value);
        }
    }
}