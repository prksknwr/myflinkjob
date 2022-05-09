package com.myflinkjob.flink.etl.operators.filters;

import com.myflinkjob.commons.models.models.User;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActiveUserFilterTest {

    private ActiveUserFilter activeUserFilter;

    @BeforeEach
    public void setUp() {
        activeUserFilter = new ActiveUserFilter();
    }

    @Test
    public void shouldReturnFalse() {
        User user = new User.Builder()
                .id(1)
                .active(false)
                .build();

        assertFalse(activeUserFilter.filter(user));
        assertFalse(activeUserFilter.filter(null));
    }

    @Test
    public void shouldReturnTrue() {
        User user = new User.Builder()
                .id(1)
                .active(true)
                .build();

        assertTrue(activeUserFilter.filter(user));
    }
}