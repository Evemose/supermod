package com.supermod.register;

import com.supermod.util.Reflection;
import java.lang.reflect.Modifier;

public class AbstractRegistrationManager<T> implements RegistrationManager {

    private final Class<T> baseType;

    protected AbstractRegistrationManager(Class<T> baseType) {
        this.baseType = baseType;
    }

    @Override
    public void registerAllOnClasspath() {
        Reflection.findSubtypesOf(baseType).stream()
            .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
            .forEach(this::register);
    }

    private void register(Class<? extends T> clazz) {
        // register the class
    }
}
