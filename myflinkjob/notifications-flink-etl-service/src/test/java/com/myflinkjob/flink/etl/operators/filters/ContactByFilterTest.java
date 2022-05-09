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
class ContactByFilterTest {

    private ContactByFilter contactByFilter;

    @BeforeEach
    public void setUp() {
        contactByFilter = new ContactByFilter();
    }

    @Test
    public void shouldReturnFalse() {
        User user = new User.Builder()
                .id(1)
                .contactBy(ContactBy.NONE)
                .build();

        assertFalse(contactByFilter.filter(user));
        assertFalse(contactByFilter.filter(null));
    }

    @Test
    public void shouldReturnTrue() {
        User user = new User.Builder()
                .id(1)
                .contactBy(ContactBy.ALL)
                .build();

        assertTrue(contactByFilter.filter(user));

        User user1 = new User.Builder()
                .id(1)
                .contactBy(ContactBy.PHONE)
                .build();

        assertTrue(contactByFilter.filter(user));

        User user2 = new User.Builder()
                .id(1)
                .contactBy(ContactBy.ALL)
                .build();

        assertTrue(contactByFilter.filter(user));
    }
}