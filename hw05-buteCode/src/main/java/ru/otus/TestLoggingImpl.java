package ru.otus;

import ru.otus.annotations.Log;

public class TestLoggingImpl implements TestLogging{
    @Log
    @Override
    public void calculation(int p1) {
        System.out.println("Work calculation(int) method");
    }

    @Override
    public void calculation(int p1, int p2) {
        System.out.println("Work calculation(int, int) method");
    }

    @Log
    @Override
    public void calculation(int p1, int p2, String p3) {
        System.out.println("Work calculation(int, int, String) method");
    }
}
