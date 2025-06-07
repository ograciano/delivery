package com.delivery.delivery.mappers;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class Mappers {
    public static <T, U> U map(T source, Class<U> targetClass) {
        try {
            U target = targetClass.getDeclaredConstructor().newInstance();
            for (Field sourceField : source.getClass().getDeclaredFields()) {
                sourceField.setAccessible(true);
                Field targetField;
                try {
                    targetField = targetClass.getDeclaredField(sourceField.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, sourceField.get(source));
                } catch (NoSuchFieldException ignored) {
                    log.warn("Campo {} no encontrado en la clase destino {}", sourceField.getName(), targetClass.getSimpleName());
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear objetos", e);
        }
    }

    public static <T, U> T mapBack(U source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            for (Field sourceField : source.getClass().getDeclaredFields()) {
                sourceField.setAccessible(true);
                Field targetField;
                try {
                    targetField = targetClass.getDeclaredField(sourceField.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, sourceField.get(source));
                } catch (NoSuchFieldException ignored) {
                    log.warn("Campo {} no encontrado en la clase destino {}", sourceField.getName(), targetClass.getSimpleName());
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear objetos", e);
        }
    }
}
