package com.myflinkjob.kafka.repo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class CsvSourceUserRepository implements UserRepository {
    private File data;

    public CsvSourceUserRepository(File data) {
        this.data = data;
    }

    public List<String> getAllEmails() throws IOException {
        return Files.lines(data.toPath())
                .skip(1)
                .map(line -> line.substring(line.indexOf(',', 8)+1, line.lastIndexOf(',')))
                .collect(Collectors.toList());
    }

    public List<String> getAllUsersFromCsv() throws IOException {
        return Files.lines(data.toPath())
                .skip(1)
                .collect(Collectors.toList());
    }
}
