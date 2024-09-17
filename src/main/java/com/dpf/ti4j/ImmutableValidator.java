package com.dpf.ti4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

import static com.dpf.ti4j.TypeInspector.*;
import static java.lang.reflect.Modifier.isFinal;
import static java.util.Objects.nonNull;

final class ImmutableValidator {

    static void validate(Object instance) throws ImmutableValidationException {

        final var clazz = instance.getClass();

        for (Field field : clazz.getDeclaredFields()) {

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (field.isAnnotationPresent(Immutable.class)) {
                continue;
            }
            if (field.getType().isAnnotationPresent(Immutable.class)) {
                continue;
            }

            if (!isFinal(field.getModifiers())) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() +
                        "' is not final.");
            }

            if (isKnownImmutable(field.getType())) {
                continue;
            }
            if (isKnownMutable(field.getType())) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() +
                        "' is a known mutable type.");
            }

            if (field.getType().isArray()) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() +
                        "' is type array.");
            }

            if (field.getType().isPrimitive()) {
                continue;
            }
            if (isJavaImmutable(field.getType())) {
                continue;
            }

            final var originalAccessible = field.canAccess(instance);
            try {
                field.setAccessible(true);

                final var fieldValue = field.get(instance);
                if (nonNull(fieldValue)) {

                    if (fieldValue instanceof Collection) {
                        if (!isImmutableCollection(fieldValue)) {
                            throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() +
                                    "' is a mutable Collection.");
                        }
                        validateCollection((Collection<?>) fieldValue);
                        continue;
                    }
                    if (fieldValue instanceof Map) {
                        if (!isImmutableMap(fieldValue)) {
                            throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() +
                                    "' is a mutable Map.");
                        }
                        validateMap((Map<?, ?>) fieldValue);
                        continue;
                    }

                    validate(fieldValue);
                }
            } catch (IllegalAccessException e) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() +
                        "' is not accessible, and therefore, its immutability cannot be guaranteed.", e);
            } finally {
                field.setAccessible(originalAccessible);
            }
        }
    }

    private static void validateCollection(Collection<?> collection) throws ImmutableValidationException {
        if (!(collection.getClass().getGenericSuperclass() instanceof ParameterizedType)) {
            throw new ImmutableValidationException("Collection '" + collection.getClass().getName() + "' uses raw types.");
        }
        for (Object element : collection) {
            if (element != null) {
                if (isJavaImmutable(element.getClass())) {
                    return;
                }
                validate(element);
            }
        }
    }

    private static void validateMap(Map<?, ?> map) throws ImmutableValidationException {
        if (!(map.getClass().getGenericSuperclass() instanceof ParameterizedType)) {
            throw new ImmutableValidationException("Collection '" + map.getClass().getName() + "' uses raw types.");
        }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            final var key = entry.getKey();
            final var value = entry.getValue();
            if (key != null) {
                if (isJavaImmutable(key.getClass())) {
                    return;
                }
                validate(key);
            }
            if (value != null) {
                if (isJavaImmutable(value.getClass())) {
                    return;
                }
                validate(value);
            }
        }
    }
}