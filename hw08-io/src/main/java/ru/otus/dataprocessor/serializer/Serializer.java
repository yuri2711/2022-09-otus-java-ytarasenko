package ru.otus.dataprocessor.serializer;

import java.io.IOException;
import java.util.Map;

public interface Serializer {

    void serialize(Map<String, Double> data) throws IOException;
}
