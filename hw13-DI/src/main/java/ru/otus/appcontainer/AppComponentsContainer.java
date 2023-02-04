package ru.otus.appcontainer;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass);
    <C> C getAppComponent(String componentName);
}
