package com.dpf.ti4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

import static com.dpf.ti4j.ImmutableTypeInspector.*;
import static java.lang.reflect.Modifier.isFinal;
import static java.util.Objects.nonNull;

final class ImmutableValidator {

    private static final ImmutableValidator INSTANCE = new ImmutableValidator();

    private ImmutableValidator() {
    }

    public static ImmutableValidator getInstance() {
        return INSTANCE;
    }

    void validate(Object instance) throws ImmutableValidationException {
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
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is not final.");
            }

            if (field.getType().isArray()) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is type array.");
            }


            if (field.getType().isPrimitive()) {
                continue;
            }
            if (isJavaImmutable(field.getType())) {
                continue;
            }
            if (isJavaMutable(field.getType())) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is a known mutable type.");
            }

            final var originalAccessible = field.canAccess(instance);
            try {
                field.setAccessible(true);

                final var fieldValue = field.get(instance);
                if (nonNull(fieldValue)) {

                    if (fieldValue instanceof Collection) {
                        if (!isImmutableCollection(fieldValue)) {
                            throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is a mutable Collection.");
                        }
                        this.validateCollection((Collection<?>) fieldValue);
                        continue;
                    }
                    if (fieldValue instanceof Map) {
                        if (!isImmutableMap(fieldValue)) {
                            throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is a mutable Map.");
                        }
                        this.validateMap((Map<?, ?>) fieldValue);
                        continue;
                    }

                    this.validate(fieldValue);
                }
            } catch (IllegalAccessException e) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is not accessible, and therefore, its immutability cannot be guaranteed.", e);
            } finally {
                field.setAccessible(originalAccessible);
            }
        }
    }

    private void validateCollection(Collection<?> collection) throws ImmutableValidationException {
        for (Object element : collection) {
            if (element != null) {
                if (isJavaImmutable(element.getClass())) {
                    return;
                }
                this.validate(element);
            }
        }
    }

    private void validateMap(Map<?, ?> map) throws ImmutableValidationException {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            final var key = entry.getKey();
            final var value = entry.getValue();
            if (key != null) {
                if (isJavaImmutable(key.getClass())) {
                    return;
                }
                this.validate(key);
            }
            if (value != null) {
                if (isJavaImmutable(value.getClass())) {
                    return;
                }
                this.validate(value);
            }
        }
    }
}