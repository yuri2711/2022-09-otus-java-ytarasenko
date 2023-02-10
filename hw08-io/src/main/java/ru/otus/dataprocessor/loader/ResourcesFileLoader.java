package ru.otus.dataprocessor.loader;

import com.google.gson.Gson;
import ru.otus.dataprocessor.FileProcessException;
import ru.otus.model.Measurement;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ResourcesFileLoader implements Loader {


    public ResourcesFileLoader() {
    }

    @Override
    public List<Measurement> load(String file) throws IOException {
        //читает файл, парсит и возвращает результат
        try (var bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            var scanner = new Scanner(bufferedInputStream);
            var json = scanner.nextLine();
            var gson = new Gson();
            return Arrays.asList(gson.fromJson(json, Measurement[].class));
        } catch (Exception ex) {
            throw new FileProcessException(ex.getMessage());
        }
    }

}
