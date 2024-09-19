package com.dpf.ti4j.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.*;
import java.util.*;

final class TypeInspector {

    private static final Class<?>[] KNOWN_IMMUTABLES = {
            // Add known immutable classes here
    };

    private static final Class<?>[] KNOWN_MUTABLES = {
            // Add known mutable classes here
    };

    private static final Class<?>[] JAVA_IMMUTABLES = {
            Integer.class,
            Long.class,
            Double.class,
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Float.class,
            String.class,
            Instant.class,
            LocalDate.class,
            LocalTime.class,
            LocalDateTime.class,
            ZonedDateTime.class,
            OffsetDateTime.class,
            OffsetTime.class,
            Duration.class,
            Period.class,
            Optional.class,
            OptionalInt.class,
            OptionalLong.class,
            OptionalDouble.class,
            UUID.class,
            Path.class,
            FileSystems.class,
            BigInteger.class,
            BigDecimal.class,
    };

    private static final String[] IMMUTABLE_COLLECTIONS = {
            "java.util.Collections$Unmodifiable",
            "java.util.ImmutableCollections$"
    };

    private static final String[] IMMUTABLE_MAPS = {
            "java.util.Collections$Unmodifiable",
            "java.util.ImmutableCollections$"
    };

    static boolean isKnownMutable(Class<?> clazz) {
        return Arrays.asList(KNOWN_MUTABLES).contains(clazz);
    }

    static boolean isKnownImmutable(Class<?> clazz) {
        return Arrays.asList(KNOWN_IMMUTABLES).contains(clazz);
    }

    static boolean isJavaImmutable(Class<?> clazz) {
        return Arrays.asList(JAVA_IMMUTABLES).contains(clazz);
    }

    static boolean isImmutableCollection(Object obj) {
        if (obj == null) {
            return false;
        }
        final var className = obj.getClass().getName();
        return Arrays.stream(IMMUTABLE_COLLECTIONS)
                .anyMatch(className::startsWith);
    }

    static boolean isImmutableMap(Object obj) {
        if (obj == null) {
            return false;
        }
        final var className = obj.getClass().getName();
        return Arrays.stream(IMMUTABLE_MAPS)
                .anyMatch(className::startsWith);
    }
}