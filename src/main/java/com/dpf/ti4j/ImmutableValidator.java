package com.dpf.ti4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

final class ImmutableValidator {

    private static final ImmutableValidator INSTANCE = new ImmutableValidator();

    private final ImmutableTypeInspector immutableTypeInspector = ImmutableTypeInspector.getInstance();

    private ImmutableValidator() {
    }

    static ImmutableValidator getInstance() {
        return INSTANCE;
    }

    void validate(Object instance) throws ImmutableValidationException {
        Class<?> clazz = instance.getClass();

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

            if (immutableTypeInspector.isArray(field.getType())) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is type array.");
            }

            validateFinalModifier(clazz, field);

            if (immutableTypeInspector.isPrimitive(field.getType())) {
                continue;
            }
            if (immutableTypeInspector.isJavaImmutable(field.getType())) {
                continue;
            }

            boolean originalAccessible = field.canAccess(instance);
            try {
                field.setAccessible(true);

                Object fieldValue = field.get(instance);
                if (fieldValue != null) {
                    if (immutableTypeInspector.isAtomicReference(fieldValue)) {
                        validateAtomicReference((AtomicReference<?>) fieldValue);
                        continue;
                    } else if (fieldValue instanceof Collection) {
                        if (!immutableTypeInspector.isImmutableCollection(fieldValue)) {
                            throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is a mutable Collection.");
                        }
                        validateCollection((Collection<?>) fieldValue);
                        continue;
                    } else if (fieldValue instanceof Map) {
                        if (!immutableTypeInspector.isImmutableMap(fieldValue)) {
                            throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is a mutable Map.");
                        }
                        validateMap((Map<?, ?>) fieldValue);
                        continue;
                    } else {
                        validate(fieldValue);
                    }

                    throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is not mutable");
                }

            } catch (IllegalAccessException e) {
                throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is not accessible, and therefore, its immutability cannot be guaranteed.", e);
            } finally {
                field.setAccessible(originalAccessible);
            }
        }
    }

    private void validateFinalModifier(Class<?> clazz, Field field) throws ImmutableValidationException {
        if (!Modifier.isFinal(field.getModifiers())) {
            throw new ImmutableValidationException("Field '" + field.getName() + "' in class '" + clazz.getName() + "' is not final.");
        }
    }

    private void validateArray(Object[] array) throws ImmutableValidationException {
        for (Object element : array) {
            if (element != null) {
                if (immutableTypeInspector.isJavaImmutable(element.getClass())) {
                    return;
                }
                validate(element);
            }
        }
    }

    private void validateAtomicReference(AtomicReference<?> atomicReference) throws ImmutableValidationException {
        Object value = atomicReference.get();
        if (value != null) {
            if (immutableTypeInspector.isJavaImmutable(value.getClass())) {
                return;
            }
            validate(value);
        }
    }

    private void validateCollection(Collection<?> collection) throws ImmutableValidationException {
        for (Object element : collection) {
            if (element != null) {
                if (immutableTypeInspector.isJavaImmutable(element.getClass())) {
                    return;
                }
                validate(element);
            }
        }
    }

    private void validateMap(Map<?, ?> map) throws ImmutableValidationException {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null) {
                if (immutableTypeInspector.isJavaImmutable(key.getClass())) {
                    return;
                }
                validate(key);
            }
            if (value != null) {
                if (immutableTypeInspector.isJavaImmutable(value.getClass())) {
                    return;
                }
                validate(value);
            }
        }
    }
}