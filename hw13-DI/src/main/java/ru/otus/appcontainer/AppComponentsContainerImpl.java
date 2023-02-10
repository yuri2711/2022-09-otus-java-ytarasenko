package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final char PKG_SEPARATOR = '.';
    private static final char DIR_SEPARATOR = System.getProperty("file.separator").toCharArray()[0];
    private static final String CLASS_FILE_SUFFIX = ".class";


    private final List<Class<?>> configs = new ArrayList<>();
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    /** Вариант 1 */
    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    /** Вариант 2 */
    public AppComponentsContainerImpl(Class<?>... appConfigs) {
        Arrays.stream(appConfigs)
                .filter(method -> method.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted((c1, c2) -> {
                    Integer o1 = c1.getDeclaredAnnotation(AppComponentsContainerConfig.class).order();
                    Integer o2 = c2.getDeclaredAnnotation(AppComponentsContainerConfig.class).order();
                    return o1.compareTo(o2);
                })
                .forEach(this::processConfig);
    }

    /** Вариант 3 */
    public AppComponentsContainerImpl(String configPackage) {
        scanPackage(configPackage);
        configs.stream()
                .filter(method -> method.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted((c1, c2) -> {
                    Integer o1 = c1.getDeclaredAnnotation(AppComponentsContainerConfig.class).order();
                    Integer o2 = c2.getDeclaredAnnotation(AppComponentsContainerConfig.class).order();
                    return o1.compareTo(o2);
                })
                .forEach(this::processConfig);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object component : appComponents) {
            if (componentClass.isInstance(component)) {
                return (C) component;
            }
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }


    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        Constructor<?> constructor = Arrays.stream(configClass.getDeclaredConstructors())
                .filter(c -> c.getParameters().length == 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Default constructor not found"));
        try {
            final Object configInstance = constructor.newInstance();

            List<Method> methods = Arrays.stream(configClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(AppComponent.class))
                    .sorted((m1, m2) -> {
                        Integer o1 = m1.getDeclaredAnnotation(AppComponent.class).order();
                        Integer o2 = m2.getDeclaredAnnotation(AppComponent.class).order();
                        return o1.compareTo(o2);
                    })
                    .toList();

            for (Method method : methods) {
                processComponent(method, configInstance);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void processComponent(Method method, Object configInstance) {
        Object bean = createBean(method, configInstance);
        String name = method.getDeclaredAnnotation(AppComponent.class).name();
        appComponents.add(bean);
        appComponentsByName.put(name, bean);
    }

    private Object createBean(Method method, Object configInstance) {
        try {
            Parameter[] parameters = method.getParameters();
            Object[] objects = new Object[parameters.length];
            for (int i = 0; i < objects.length; i++) {
                objects[i] = getAppComponent(parameters[i].getType());
            }
            return method.invoke(configInstance, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void scanPackage(final String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException();
        }
        for (File file : Objects.requireNonNull(new File(scannedUrl.getFile()).listFiles())) {
            find(file, scannedPackage);
        }
    }

    private void find(final File file, final String scannedPackage) {
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : Objects.requireNonNull(file.listFiles())) {
                find(child, resource);
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            try {
                String substring = resource.substring(0, resource.length() - CLASS_FILE_SUFFIX.length());
                configs.add(Class.forName(substring));
            } catch (ClassNotFoundException ignore) {
                System.out.printf("Class %s not found%n", resource);
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
