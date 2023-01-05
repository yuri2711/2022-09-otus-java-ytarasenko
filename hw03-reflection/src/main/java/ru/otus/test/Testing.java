package ru.otus.test;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class Testing {

    @Before
    public void init(){
        System.out.println("Запуск before метода");
    }

    @Test
    public void test1(){
        System.out.println("Запуск test метода");
    }

    @Test
    public void testfailed2(){
        System.out.println("Запуск failed test метода");
        throw new RuntimeException();
    }

    @After
    public void closeResource(){
        System.out.println("Запуск after метода");
    }
}
