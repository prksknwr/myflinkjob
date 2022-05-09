package com.myflinkjob.kafka.repo;

import java.io.IOException;
import java.util.List;

public interface UserRepository {

    List<String> getAllEmails() throws IOException;
    List<String> getAllUsersFromCsv() throws IOException;
}
