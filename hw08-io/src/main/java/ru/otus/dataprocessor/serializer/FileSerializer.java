package ru.otus.dataprocessor.serializer;

import ru.otus.dataprocessor.FileProcessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSerializer implements Serializer {

    private final Path path;

    public FileSerializer(String fileName) {
        this.path = Paths.get(fileName);
    }

    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        //формирует результирующий json и сохраняет его в файл
        try {
            String json = data.entrySet().stream()
                    .map(e -> "\"" + e.getKey() + "\":" + e.getValue())
                    .sorted(String::compareTo)
                    .collect(Collectors.joining(",", "{", "}"));
            Files.writeString(path, json);
        } catch (Exception ex) {
            throw new FileProcessException(ex.getMessage());
        }
    }
}
