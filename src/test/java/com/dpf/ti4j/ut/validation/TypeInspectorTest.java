package com.dpf.ti4j.ut.validation;

import com.dpf.ti4j.validation.TypeInspector;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TypeInspectorTest {

    @InjectMocks
    private TypeInspector typeInspector;

    @Nested
    class JavaImmutableTypesTest {

        @Test
        void testIntegerIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Integer.class));
        }

        @Test
        void testLongIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Long.class));
        }

        @Test
        void testDoubleIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Double.class));
        }

        @Test
        void testBooleanIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Boolean.class));
        }

        @Test
        void testCharIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Character.class));
        }

        @Test
        void testByteIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Byte.class));
        }

        @Test
        void testShortIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Short.class));
        }

        @Test
        void testFloatIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Float.class));
        }

        @Test
        void testCharacterIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Character.class));
        }

        @Test
        void testStringIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(String.class));
        }

        @Test
        void testInstantIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Instant.class));
        }

        @Test
        void testLocalDateIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(LocalDate.class));
        }

        @Test
        void testLocalTimeIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(LocalTime.class));
        }

        @Test
        void testLocalDateTimeIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(LocalDateTime.class));
        }

        @Test
        void testZonedDateTimeIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(ZonedDateTime.class));
        }

        @Test
        void testOffsetDateTimeIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(OffsetDateTime.class));
        }

        @Test
        void testOffsetTimeIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(OffsetTime.class));
        }

        @Test
        void testDurationIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Duration.class));
        }

        @Test
        void testPeriodIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Period.class));
        }

        @Test
        void testOptionalIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Optional.class));
        }

        @Test
        void testOptionalIntIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(OptionalInt.class));
        }

        @Test
        void testOptionalLongIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(OptionalLong.class));
        }

        @Test
        void testOptionalDoubleIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(OptionalDouble.class));
        }

        @Test
        void testUUIDIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(UUID.class));
        }

        @Test
        void testPathIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(Path.class));
        }

        @Test
        void testFileSystemsIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(FileSystems.class));
        }

        @Test
        void testBigIntegerIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(BigInteger.class));
        }

        @Test
        void testBigDecimalIsJavaImmutable() {
            assertTrue(typeInspector.isJavaImmutable(BigDecimal.class));
        }
    }

    @Nested
    class AtomicReferenceTest {

        @Test
        void testIsAtomicReference() {
            final var reference = new AtomicReference<>("Test");
            assertTrue(typeInspector.isAtomicReference(reference));
        }

        @Test
        void testIsNotAtomicReference() {
            assertFalse(typeInspector.isAtomicReference("Not an AtomicReference"));
        }
    }

    @Nested
    class ImmutableListTest {

        @Test
        void testImmutableListFromListOf() {
            final var immutableList = List.of("item1", "item2");
            assertTrue(typeInspector.isImmutableCollection(immutableList));
        }

        @Test
        void testUnmodifiableListIsImmutable() {
            final var unmodifiableList = Collections.unmodifiableList(new ArrayList<>());
            assertTrue(typeInspector.isImmutableCollection(unmodifiableList));
        }

        @Test
        void testMutableListIsNotImmutable() {
            final var mutableList = new ArrayList<>();
            assertFalse(typeInspector.isImmutableCollection(mutableList));
        }
    }

    @Nested
    class ImmutableSetTest {

        @Test
        void testImmutableSetFromSetOf() {
            final var immutableSet = Set.of("item1", "item2");
            assertTrue(typeInspector.isImmutableCollection(immutableSet));
        }

        @Test
        void testUnmodifiableSetIsImmutable() {
            final var unmodifiableSet = Collections.unmodifiableSet(new HashSet<>());
            assertTrue(typeInspector.isImmutableCollection(unmodifiableSet));
        }

        @Test
        void testMutableSetIsNotImmutable() {
            final var mutableSet = new HashSet<>();
            assertFalse(typeInspector.isImmutableCollection(mutableSet));
        }
    }

    @Nested
    class ImmutableMapTest {

        @Test
        void testImmutableMapFromMapOf() {
            final var immutableMap = Map.of("key", "value");
            assertTrue(typeInspector.isImmutableMap(immutableMap));
        }

        @Test
        void testUnmodifiableMapIsImmutable() {
            final var unmodifiableMap = Collections.unmodifiableMap(new HashMap<>());
            assertTrue(typeInspector.isImmutableMap(unmodifiableMap));
        }

        @Test
        void testMutableMapIsNotImmutable() {
            final var mutableMap = new HashMap<>();
            assertFalse(typeInspector.isImmutableMap(mutableMap));
        }
    }
}