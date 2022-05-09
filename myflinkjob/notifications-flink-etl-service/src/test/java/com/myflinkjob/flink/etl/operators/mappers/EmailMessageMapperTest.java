package com.myflinkjob.flink.etl.operators.mappers;

import com.myflinkjob.commons.models.models.ContactBy;
import com.myflinkjob.commons.models.models.EmailMessage;
import com.myflinkjob.commons.models.NotificationMessage;
import com.myflinkjob.commons.models.models.User;
import com.myflinkjob.flink.etl.operators.filters.ActiveUserFilter;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailMessageMapperTest {
    private EmailMessageMapper emailMessageMapper;

    @BeforeEach
    public void setUp() {
        emailMessageMapper = new EmailMessageMapper();
    }

    @Test
    public void shouldMap() {
        User user = new User.Builder()
                .id(1)
                .active(true)
                .email("test1@email.com")
                .contactBy(ContactBy.EMAIL)
                .build();

        NotificationMessage message = emailMessageMapper.map(user);
        assertThat(message).isInstanceOf(EmailMessage.class);
        assertEquals(1, message.getUserId());
    }

    @Test
    public void shouldMapToNull() {
        User user = new User.Builder()
                .id(1)
                .active(true)
                .email("test1@email.com")
                .contactBy(ContactBy.EMAIL)
                .build();

        assertNull(emailMessageMapper.map(null));
    }
}