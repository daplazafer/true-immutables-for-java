package com.dpf.ti4j.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

import static com.dpf.ti4j.core.TypeInspector.*;
import static java.lang.reflect.Modifier.isFinal;
import static java.util.Objects.nonNull;

public final class ImmutableValidator {

    public static void validate(Object instance) throws ImmutableValidationException {

        final var clazz = instance.getClass();

        for (Field field : clazz.getDeclaredFields()) {

            if (Modifier.isStatic(field.getModifiers()))
                continue;

            if (field.isAnnotationPresent(Immutable.class))
                continue;

            if (!isFinal(field.getModifiers()))
                throw new ImmutableValidationException(
                        String.format("Field '%s' in class '%s' is not final.", field.getName(), clazz.getName()));

            if (field.getType().isAnnotationPresent(Immutable.class))
                continue;

            if (isKnownImmutable(field.getType()))
                continue;

            if (isKnownMutable(field.getType()))
                throw new ImmutableValidationException(
                        String.format("Field '%s' in class '%s' is a known mutable type.", field.getName(), clazz.getName()));

            if (field.getType().isArray())
                throw new ImmutableValidationException(
                        String.format("Field '%s' in class '%s' is of type array.", field.getName(), clazz.getName()));


            if (field.getType().isPrimitive())
                continue;

            if (isJavaImmutable(field.getType()))
                continue;

            final var originalAccessible = field.canAccess(instance);
            try {
                field.setAccessible(true);

                final var fieldValue = field.get(instance);
                if (nonNull(fieldValue)) {

                    if (fieldValue instanceof Collection) {
                        if (!isImmutableCollection(fieldValue))
                            throw new ImmutableValidationException(
                                    String.format("Field '%s' in class '%s' is a mutable Collection.", field.getName(), clazz.getName()));

                        validateCollection((Collection<?>) fieldValue);
                        continue;
                    }

                    if (fieldValue instanceof Map) {
                        if (!isImmutableMap(fieldValue))
                            throw new ImmutableValidationException(
                                    String.format("Field '%s' in class '%s' is a mutable Map.", field.getName(), clazz.getName()));

                        validateMap((Map<?, ?>) fieldValue);
                        continue;
                    }

                    validate(fieldValue);
                }
            } catch (IllegalAccessException e) {
                throw new ImmutableValidationException(
                        String.format("Field '%s' in class '%s' is not accessible, and therefore, its immutability cannot be guaranteed.",
                                field.getName(), clazz.getName()), e);
            } finally {
                field.setAccessible(originalAccessible);
            }
        }
    }

    private static void validateCollection(Collection<?> collection) throws ImmutableValidationException {

        for (Object element : collection) {

            if (nonNull(element)) {
                if (isJavaImmutable(element.getClass()))
                    continue;

                validate(element);
            }
        }
    }

    private static void validateMap(Map<?, ?> map) throws ImmutableValidationException {

        for (Map.Entry<?, ?> entry : map.entrySet()) {

            final var key = entry.getKey();
            final var value = entry.getValue();

            if (nonNull(key) && !isJavaImmutable(key.getClass()))
                validate(key);

            if (nonNull(value) && !isJavaImmutable(value.getClass()))
                validate(value);
        }
    }
}