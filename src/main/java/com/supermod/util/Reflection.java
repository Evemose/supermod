package com.supermod.util;

import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.reflections.Reflections;

@UtilityClass
public class Reflection {

    private static final String BASE_PACKAGE = "com.supermod";

    public static <T> List<Class<? extends T>> findSubtypesOf(Class<T> type) {
        var reflections = new Reflections(BASE_PACKAGE);
        return new ArrayList<>(reflections.getSubTypesOf(type));
    }

    @SneakyThrows
    public static <T> T createInstance(Class<T> type, Object... args) {
        var constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance(args);
    }

}
