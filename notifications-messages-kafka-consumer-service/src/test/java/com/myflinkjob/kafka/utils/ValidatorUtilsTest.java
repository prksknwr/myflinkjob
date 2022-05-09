package com.myflinkjob.kafka.utils;

import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidatorUtilsTest {

    @Test
    public void shouldReturnFalseForBlankEmail() {
        assertFalse(ValidatorUtils.isEmailValid(""));
    }

    @Test
    public void shouldReturnFalseForEmail() {
        assertFalse(ValidatorUtils.isEmailValid("test3mail.com"));
        assertFalse(ValidatorUtils.isEmailValid("testtesteset"));
        assertFalse(ValidatorUtils.isEmailValid("testt@esteset"));
    }

    @Test
    public void shouldReturnTrueForValidEmail() {
        assertTrue(ValidatorUtils.isEmailValid("test3@mail.com"));
    }

}