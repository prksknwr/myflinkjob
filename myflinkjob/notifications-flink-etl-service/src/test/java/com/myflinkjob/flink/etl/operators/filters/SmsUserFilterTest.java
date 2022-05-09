package com.myflinkjob.flink.etl.operators.filters;

import com.myflinkjob.commons.models.models.ContactBy;
import com.myflinkjob.commons.models.models.User;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SmsUserFilterTest {

    private SmsUserFilter smsUserFilter;

    @BeforeEach
    public void setUp() {
        smsUserFilter = new SmsUserFilter();
    }

    @Test
    public void shouldReturnFalse() {
        User user = new User.Builder()
                .id(1)
                .contactBy(ContactBy.NONE)
                .build();

        User user1 = new User.Builder()
                .id(1)
                .contactBy(ContactBy.EMAIL)
                .build();

        assertFalse(smsUserFilter.filter(user));
        assertFalse(smsUserFilter.filter(user1));
        assertFalse(smsUserFilter.filter(null));
    }

    @Test
    public void shouldReturnTrue() {
        User user = new User.Builder()
                .id(1)
                .contactBy(ContactBy.ALL)
                .build();

        assertTrue(smsUserFilter.filter(user));

        User user1 = new User.Builder()
                .id(1)
                .contactBy(ContactBy.PHONE)
                .build();

        assertTrue(smsUserFilter.filter(user1));

    }
}