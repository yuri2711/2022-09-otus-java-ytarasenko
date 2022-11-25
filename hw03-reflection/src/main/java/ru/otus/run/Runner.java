package ru.otus.run;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Runner {
    private Runner() {
    }

    public static void run(String clazzName) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(clazzName);
        Method[] methods = clazz.getDeclaredMethods();
        String simpleName = clazz.getSimpleName();

        if (methods.length == 0) {
            System.out.println("Отсутствуют методы в классе " + simpleName);
        }

        String beforeMethodName = null;
        String afterMethodName = null;
        List<String> testMethodList = new ArrayList<>();

        for (Method method : methods) {
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();

            for (Annotation annotation : declaredAnnotations) {

                if (annotation instanceof Before) {
                    beforeMethodName = method.getName();
                } else if (annotation instanceof After) {
                    afterMethodName = method.getName();
                } else if (annotation instanceof Test) {
                    testMethodList.add(method.getName());
                }
            }
        }

        if (testMethodList.isEmpty()) {
            System.out.println("Отсутствуют методы для тестирования");
        }
        Map<String, Exception> resultMap = new LinkedHashMap<>();

        for (String test : testMethodList) {
            Object object;
            try {
                object = Helper.instantiate(clazz);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка создания объекта");
            }

            RuntimeException runtimeException = null;

            try {
                Helper.call(object, beforeMethodName);
                Helper.call(object, test);
            } catch (RuntimeException e) {
                runtimeException = e;
            }
            try {
                Helper.call(object, afterMethodName);
            } catch (RuntimeException e) {
                runtimeException = e;
            }
            resultMap.put(test, runtimeException);
        }

        resultMap.entrySet().stream().forEach(stringExceptionEntry -> {
                    String key = stringExceptionEntry.getKey();
                    Exception value = stringExceptionEntry.getValue();
                    System.out.println(simpleName + " > " + key + "()" + (value == null ? " PASSED" : " FAILED ----> " + value));
                }
        );

        long count = resultMap.values().stream().filter(Objects::nonNull).count();
        System.out.println("\nTests passed: " + (testMethodList.size() - count));
        System.out.println("Tests failed: " + count);
        System.out.println("Total: " + testMethodList.size());
    }
}
