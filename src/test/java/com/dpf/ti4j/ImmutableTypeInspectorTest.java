package com.dpf.ti4j;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImmutableTypeInspectorTest {

    private final ImmutableTypeInspector immutableTypeInspector = ImmutableTypeInspector.getInstance();

    @Nested
    class JavaImmutableTypesTest {

        @Test
        void testIntegerIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Integer.class));
        }

        @Test
        void testLongIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Long.class));
        }

        @Test
        void testDoubleIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Double.class));
        }

        @Test
        void testBooleanIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Boolean.class));
        }

        @Test
        void testCharIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Character.class));
        }

        @Test
        void testByteIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Byte.class));
        }

        @Test
        void testShortIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Short.class));
        }

        @Test
        void testFloatIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Float.class));
        }

        @Test
        void testCharacterIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Character.class));
        }

        @Test
        void testStringIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(String.class));
        }

        @Test
        void testInstantIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Instant.class));
        }

        @Test
        void testLocalDateIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(LocalDate.class));
        }

        @Test
        void testLocalTimeIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(LocalTime.class));
        }

        @Test
        void testLocalDateTimeIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(LocalDateTime.class));
        }

        @Test
        void testZonedDateTimeIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(ZonedDateTime.class));
        }

        @Test
        void testOffsetDateTimeIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(OffsetDateTime.class));
        }

        @Test
        void testOffsetTimeIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(OffsetTime.class));
        }

        @Test
        void testDurationIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Duration.class));
        }

        @Test
        void testPeriodIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Period.class));
        }

        @Test
        void testOptionalIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Optional.class));
        }

        @Test
        void testOptionalIntIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(OptionalInt.class));
        }

        @Test
        void testOptionalLongIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(OptionalLong.class));
        }

        @Test
        void testOptionalDoubleIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(OptionalDouble.class));
        }

        @Test
        void testUUIDIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(UUID.class));
        }

        @Test
        void testPathIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(Path.class));
        }

        @Test
        void testFileSystemsIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(FileSystems.class));
        }

        @Test
        void testBigIntegerIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(BigInteger.class));
        }

        @Test
        void testBigDecimalIsJavaImmutable() {
            assertTrue(immutableTypeInspector.isJavaImmutable(BigDecimal.class));
        }
    }

    @Nested
    class AtomicReferenceTest {

        @Test
        void testIsAtomicReference() {
            final var reference = new AtomicReference<>("Test");
            assertTrue(immutableTypeInspector.isAtomicReference(reference));
        }

        @Test
        void testIsNotAtomicReference() {
            assertFalse(immutableTypeInspector.isAtomicReference("Not an AtomicReference"));
        }
    }

    @Nested
    class ImmutableListTest {

        @Test
        void testImmutableListFromListOf() {
            final var immutableList = List.of("item1", "item2");
            assertTrue(immutableTypeInspector.isImmutableCollection(immutableList));
        }

        @Test
        void testUnmodifiableListIsImmutable() {
            final var unmodifiableList = Collections.unmodifiableList(new ArrayList<>());
            assertTrue(immutableTypeInspector.isImmutableCollection(unmodifiableList));
        }

        @Test
        void testMutableListIsNotImmutable() {
            final var mutableList = new ArrayList<>();
            assertFalse(immutableTypeInspector.isImmutableCollection(mutableList));
        }
    }

    @Nested
    class ImmutableSetTest {

        @Test
        void testImmutableSetFromSetOf() {
            final var immutableSet = Set.of("item1", "item2");
            assertTrue(immutableTypeInspector.isImmutableCollection(immutableSet));
        }

        @Test
        void testUnmodifiableSetIsImmutable() {
            final var unmodifiableSet = Collections.unmodifiableSet(new HashSet<>());
            assertTrue(immutableTypeInspector.isImmutableCollection(unmodifiableSet));
        }

        @Test
        void testMutableSetIsNotImmutable() {
            final var mutableSet = new HashSet<>();
            assertFalse(immutableTypeInspector.isImmutableCollection(mutableSet));
        }
    }

    @Nested
    class ImmutableMapTest {

        @Test
        void testImmutableMapFromMapOf() {
            final var immutableMap = Map.of("key", "value");
            assertTrue(immutableTypeInspector.isImmutableMap(immutableMap));
        }

        @Test
        void testUnmodifiableMapIsImmutable() {
            final var unmodifiableMap = Collections.unmodifiableMap(new HashMap<>());
            assertTrue(immutableTypeInspector.isImmutableMap(unmodifiableMap));
        }

        @Test
        void testMutableMapIsNotImmutable() {
            final var mutableMap = new HashMap<>();
            assertFalse(immutableTypeInspector.isImmutableMap(mutableMap));
        }
    }
}