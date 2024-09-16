package com.dpf.ti4j;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ImmutableValidatorTest {

    @Nested
    class PrimitiveFieldTest {

        @Immutable
        record PrimitiveInt(int value) {
        }

        @Test
        void primitiveInt() {
            new PrimitiveInt(1);
        }

        @Immutable
        record PrimitiveArray(int[] array) {
        }

        @Test
        void primitiveArray() {
            assertThrows(IllegalStateException.class, () ->
                    new PrimitiveArray(new int[]{1, 2, 3}));
        }

        @Test
        void nullPrimitiveArray() {
            assertThrows(IllegalStateException.class, () ->
                    new PrimitiveArray(null));
        }
    }

    @Nested
    class JavaImmutableFieldTest {

        @Immutable
        record JavaImmutableString(String value) {
        }

        @Test
        void javaImmutableField() {
            new JavaImmutableString("test");
        }

        @Test
        void nullJavaImmutableField() {
            new JavaImmutableString(null);
        }

        @Immutable
        record JavaImmutableArray(String[] array) {
        }

        @Test
        void JavaImmutableArray() {
            assertThrows(IllegalStateException.class, () ->
                    new JavaImmutableArray(new String[]{"test1", "test2"}));
        }

        @Test
        void nullJavaImmutableArray() {
            assertThrows(IllegalStateException.class, () ->
                    new JavaImmutableArray(null));
        }
    }

    @Nested
    class AtomicReferenceTest {

        @Immutable
        record AtomicReferenceField(AtomicReference<String> ref) {
        }

        @Test
        void atomicReferenceField() {
            assertThrows(IllegalStateException.class, () ->
                    new AtomicReferenceField(new AtomicReference<>("test")));
        }

        @Test
        void nullAtomicReferenceField() {
            new AtomicReferenceField(null);
        }
    }

    @Nested
    class ListFieldTest {

        @Immutable
        record ListField(List<String> list) {
        }

        @Test
        void immutableListField() {
            new ListField(List.of("test1", "test2"));
        }

        @Test
        void unmodifiableListField() {
            new ListField(Collections.unmodifiableList(new ArrayList<>()));
        }

        @Test
        void nullListField() {
            new ListField(null);
        }

        @Test
        void mutableListField() {
            assertThrows(IllegalStateException.class, () ->
                    new ListField(new ArrayList<>()));
        }
    }

    @Nested
    class NonFinalPrimitiveTest {

        @Immutable
        static class NonFinalPrimitive {
            int value;

            NonFinalPrimitive(int value) {
                this.value = value;
            }
        }

        @Test
        void nonFinalPrimitiveField() {
            assertThrows(IllegalStateException.class, () ->
                    new NonFinalPrimitive(1));
        }
    }

    @Nested
    class MutableTest {

        @Immutable
        record MutableField(StringBuilder stringBuilder) {
        }

        @Test
        void mutableField() {
            assertThrows(IllegalStateException.class, () ->
                    new MutableField(new StringBuilder("test")));
        }

        @Test
        void nullMutableField() {
            new MutableField(null);
        }
    }
}