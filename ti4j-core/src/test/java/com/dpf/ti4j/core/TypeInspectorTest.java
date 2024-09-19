package com.dpf.ti4j.core;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.*;
import java.util.*;

import static com.dpf.ti4j.core.TypeInspector.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeInspectorTest {

    @Nested
    class JavaImmutableTypesTest {

        @Test
        void testIntegerIsJavaImmutable() {
            assertTrue(isJavaImmutable(Integer.class));
        }

        @Test
        void testLongIsJavaImmutable() {
            assertTrue(isJavaImmutable(Long.class));
        }

        @Test
        void testDoubleIsJavaImmutable() {
            assertTrue(isJavaImmutable(Double.class));
        }

        @Test
        void testBooleanIsJavaImmutable() {
            assertTrue(isJavaImmutable(Boolean.class));
        }

        @Test
        void testCharIsJavaImmutable() {
            assertTrue(isJavaImmutable(Character.class));
        }

        @Test
        void testByteIsJavaImmutable() {
            assertTrue(isJavaImmutable(Byte.class));
        }

        @Test
        void testShortIsJavaImmutable() {
            assertTrue(isJavaImmutable(Short.class));
        }

        @Test
        void testFloatIsJavaImmutable() {
            assertTrue(isJavaImmutable(Float.class));
        }

        @Test
        void testCharacterIsJavaImmutable() {
            assertTrue(isJavaImmutable(Character.class));
        }

        @Test
        void testStringIsJavaImmutable() {
            assertTrue(isJavaImmutable(String.class));
        }

        @Test
        void testInstantIsJavaImmutable() {
            assertTrue(isJavaImmutable(Instant.class));
        }

        @Test
        void testLocalDateIsJavaImmutable() {
            assertTrue(isJavaImmutable(LocalDate.class));
        }

        @Test
        void testLocalTimeIsJavaImmutable() {
            assertTrue(isJavaImmutable(LocalTime.class));
        }

        @Test
        void testLocalDateTimeIsJavaImmutable() {
            assertTrue(isJavaImmutable(LocalDateTime.class));
        }

        @Test
        void testZonedDateTimeIsJavaImmutable() {
            assertTrue(isJavaImmutable(ZonedDateTime.class));
        }

        @Test
        void testOffsetDateTimeIsJavaImmutable() {
            assertTrue(isJavaImmutable(OffsetDateTime.class));
        }

        @Test
        void testOffsetTimeIsJavaImmutable() {
            assertTrue(isJavaImmutable(OffsetTime.class));
        }

        @Test
        void testDurationIsJavaImmutable() {
            assertTrue(isJavaImmutable(Duration.class));
        }

        @Test
        void testPeriodIsJavaImmutable() {
            assertTrue(isJavaImmutable(Period.class));
        }

        @Test
        void testOptionalIsJavaImmutable() {
            assertTrue(isJavaImmutable(Optional.class));
        }

        @Test
        void testOptionalIntIsJavaImmutable() {
            assertTrue(isJavaImmutable(OptionalInt.class));
        }

        @Test
        void testOptionalLongIsJavaImmutable() {
            assertTrue(isJavaImmutable(OptionalLong.class));
        }

        @Test
        void testOptionalDoubleIsJavaImmutable() {
            assertTrue(isJavaImmutable(OptionalDouble.class));
        }

        @Test
        void testUUIDIsJavaImmutable() {
            assertTrue(isJavaImmutable(UUID.class));
        }

        @Test
        void testPathIsJavaImmutable() {
            assertTrue(isJavaImmutable(Path.class));
        }

        @Test
        void testFileSystemsIsJavaImmutable() {
            assertTrue(isJavaImmutable(FileSystems.class));
        }

        @Test
        void testBigIntegerIsJavaImmutable() {
            assertTrue(isJavaImmutable(BigInteger.class));
        }

        @Test
        void testBigDecimalIsJavaImmutable() {
            assertTrue(isJavaImmutable(BigDecimal.class));
        }
    }

    @Nested
    class ImmutableListTest {

        @Test
        void testImmutableListFromListOf() {
            final var immutableList = List.of("item1", "item2");
            assertTrue(isImmutableCollection(immutableList));
        }

        @Test
        void testUnmodifiableListIsImmutable() {
            final var unmodifiableList = Collections.unmodifiableList(new ArrayList<>());
            assertTrue(isImmutableCollection(unmodifiableList));
        }

        @Test
        void testMutableListIsNotImmutable() {
            final var mutableList = new ArrayList<>();
            assertFalse(isImmutableCollection(mutableList));
        }
    }

    @Nested
    class ImmutableSetTest {

        @Test
        void testImmutableSetFromSetOf() {
            final var immutableSet = Set.of("item1", "item2");
            assertTrue(isImmutableCollection(immutableSet));
        }

        @Test
        void testUnmodifiableSetIsImmutable() {
            final var unmodifiableSet = Collections.unmodifiableSet(new HashSet<>());
            assertTrue(isImmutableCollection(unmodifiableSet));
        }

        @Test
        void testMutableSetIsNotImmutable() {
            final var mutableSet = new HashSet<>();
            assertFalse(isImmutableCollection(mutableSet));
        }
    }

    @Nested
    class ImmutableMapTest {

        @Test
        void testImmutableMapFromMapOf() {
            final var immutableMap = Map.of("key", "value");
            assertTrue(isImmutableMap(immutableMap));
        }

        @Test
        void testUnmodifiableMapIsImmutable() {
            final var unmodifiableMap = Collections.unmodifiableMap(new HashMap<>());
            assertTrue(isImmutableMap(unmodifiableMap));
        }

        @Test
        void testMutableMapIsNotImmutable() {
            final var mutableMap = new HashMap<>();
            assertFalse(isImmutableMap(mutableMap));
        }
    }
}