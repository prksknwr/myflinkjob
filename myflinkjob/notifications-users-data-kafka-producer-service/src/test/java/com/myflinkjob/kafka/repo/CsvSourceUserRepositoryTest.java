package com.myflinkjob.kafka.repo;

import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvSourceUserRepositoryTest {

    private UserRepository classUnderTest;
    @BeforeEach
    public void setUp() {
        classUnderTest = new CsvSourceUserRepository(new File(this.getClass().getResource("/test_user_data.txt").getFile()));
    }

    @Test
    public void shouleReturnAllUserEmailAddresses() throws IOException {
       List<String> emails = classUnderTest.getAllEmails();

        assertThat(emails).hasSize(5);
        assertThat(emails).containsExactly("test1@mail.com", "test2@mail.com", "test3mail.com", "test4@mail.com", "test4@mail.com");
        System.out.println(emails);
    }

    @Test
    public void shouldReturnAllUserLinesFromCsv() throws IOException {
        List<String> emails = classUnderTest.getAllUsersFromCsv();

        assertThat(emails).hasSize(5);
        assertThat(emails).containsExactly(
                "1,true,email,test1@mail.com,+999999999999",
                "2,true,none,test2@mail.com,+999999999998",
                "3,false,phone,test3mail.com,+999999999997",
                "4,true,email,test4@mail.com,+999999999996",
                "5,true,all,test4@mail.com,+999999999996"
        );
    }

    @Test
    public void shouldThrowIOExceptionWhenFileDoesnotExist() {
        classUnderTest = new CsvSourceUserRepository(new File("non-existing-file.csv"));

        assertThrows(IOException.class, () -> {
            classUnderTest.getAllUsersFromCsv();
        });

    }

}