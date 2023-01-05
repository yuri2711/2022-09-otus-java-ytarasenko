package ru.otus;

import ru.otus.run.Runner;
import ru.otus.test.Testing;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Runner.run(Testing.class.getName());
    }
}