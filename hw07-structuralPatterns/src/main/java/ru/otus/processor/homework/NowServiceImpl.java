package ru.otus.processor.homework;

import java.time.LocalDateTime;

public class NowServiceImpl implements NowService {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
