package ru.otus;

import com.google.common.base.Objects;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");System.out.println("Test Guava!");
        String name = "Ivan";
        int hashCode = Objects.hashCode(name);
        System.out.println("hashCode = " + hashCode);
    }
}