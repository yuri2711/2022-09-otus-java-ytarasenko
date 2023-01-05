package ru.otus.run;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Helper {
    private Helper() {
    }
    public static Object call(Object object, String name, Object ... args) {
        try {
            Method declaredMethod = object.getClass().getDeclaredMethod(name, toClass(args));
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(object,args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClass(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static Class<?>[] toClass(Object[] args) {
        Class<?>[] classes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
        return classes;
    }
}
