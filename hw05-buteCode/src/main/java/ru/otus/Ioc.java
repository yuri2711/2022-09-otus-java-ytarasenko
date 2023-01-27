package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Ioc {
    private Ioc() {}

    static TestLogging createTestLogging() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Demo.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }


    static class DemoInvocationHandler implements InvocationHandler {
        private final Object instance;
        private final Set<String> methods = new HashSet<>();

        DemoInvocationHandler(Object instance) {
            this.instance = instance;

            for (Method method : instance.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    methods.add(method.getName() + Arrays.toString(method.getParameterTypes()));
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String key = method.getName() + Arrays.toString(method.getParameterTypes());
            if (methods.contains(key)) {
                System.out.print("executed method:" + key);

                for (int n = 0; n < args.length; n++) {
                    System.out.print(", param" + (n + 1) + "=" + args[n]);
                }
                System.out.println();
            }
            return method.invoke(instance, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler {" +
                    "testLoggingClass = " + instance +
                    '}';
        }
    }
}
