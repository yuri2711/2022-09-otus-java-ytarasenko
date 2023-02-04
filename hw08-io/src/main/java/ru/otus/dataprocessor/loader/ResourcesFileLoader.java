package ru.otus.dataprocessor.loader;

import com.google.gson.Gson;
import ru.otus.dataprocessor.FileProcessException;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ResourcesFileLoader implements Loader {

    private File file;

    public ResourcesFileLoader(String file) {
        setFile(file);
    }

    @Override
    public List<Measurement> load() throws IOException {
        //читает файл, парсит и возвращает результат
        try {
            var scanner = new Scanner(file);
            var json = scanner.nextLine();
            var gson = new Gson();
            return Arrays.asList(gson.fromJson(json, Measurement[].class));
        } catch (Exception ex) {
            throw new FileProcessException(ex.getMessage());
        }
    }

    private void setFile(String fileName) {
        file = new File(fileName);
    }
}
