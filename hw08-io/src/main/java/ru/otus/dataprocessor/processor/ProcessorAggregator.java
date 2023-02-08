package ru.otus.dataprocessor.processor;

import ru.otus.dataprocessor.FileProcessException;
import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        try {
            return data.stream()
                    .collect(Collectors.toMap(
                            Measurement::getName,
                            Measurement::getValue,
                            Double::sum)
                    );
        } catch (Exception ex) {
            throw new FileProcessException(ex.getMessage());
        }
    }
}
