package com.dpf.ti4j.core;

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
            PrimitiveInt instance = new PrimitiveInt(1);
            ImmutableValidator.validate(instance);
        }

        @Immutable
        record PrimitiveArray(int[] array) {
        }

        @Test
        void primitiveArray() {
            PrimitiveArray instance = new PrimitiveArray(new int[]{1, 2, 3});
            assertThrows(ImmutableValidationException.class, () ->
                    ImmutableValidator.validate(instance));
        }

        @Test
        void nullPrimitiveArray() {
            PrimitiveArray instance = new PrimitiveArray(null);
            assertThrows(ImmutableValidationException.class, () ->
                    ImmutableValidator.validate(instance));
        }
    }

    @Nested
    class JavaImmutableFieldTest {

        @Immutable
        record JavaImmutableString(String value) {
        }

        @Test
        void javaImmutableField() {
            JavaImmutableString instance = new JavaImmutableString("test");
            ImmutableValidator.validate(instance);
        }

        @Test
        void nullJavaImmutableField() {
            JavaImmutableString instance = new JavaImmutableString(null);
            ImmutableValidator.validate(instance);
        }

        @Immutable
        record JavaImmutableArray(String[] array) {
        }

        @Test
        void JavaImmutableArray() {
            JavaImmutableArray instance = new JavaImmutableArray(new String[]{"test1", "test2"});
            assertThrows(ImmutableValidationException.class, () ->
                    ImmutableValidator.validate(instance));
        }

        @Test
        void nullJavaImmutableArray() {
            JavaImmutableArray instance = new JavaImmutableArray(null);
            assertThrows(ImmutableValidationException.class, () ->
                    ImmutableValidator.validate(instance));
        }
    }

    @Nested
    class AtomicReferenceTest {

        @Immutable
        record AtomicReferenceField(AtomicReference<String> ref) {
        }

        @Test
        void atomicReferenceField() {
            AtomicReferenceField instance = new AtomicReferenceField(new AtomicReference<>("test"));
            assertThrows(ImmutableValidationException.class, () ->
                    ImmutableValidator.validate(instance));
        }

        @Test
        void nullAtomicReferenceField() {
            AtomicReferenceField instance = new AtomicReferenceField(null);
            ImmutableValidator.validate(instance);
        }
    }

    @Nested
    class ListFieldTest {

        @Immutable
        record ListField(List<String> list) {
        }

        @Test
        void immutableListField() {
            ListField instance = new ListField(List.of("test1", "test2"));
            ImmutableValidator.validate(instance);
        }

        @Test
        void unmodifiableListField() {
            ListField instance = new ListField(Collections.unmodifiableList(new ArrayList<>()));
            ImmutableValidator.validate(instance);
        }

        @Test
        void nullListField() {
            ListField instance = new ListField(null);
            ImmutableValidator.validate(instance);
        }

        @Test
        void mutableListField() {
            ListField instance = new ListField(new ArrayList<>());
            assertThrows(ImmutableValidationException.class, () ->
                    ImmutableValidator.validate(instance));
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
            NonFinalPrimitive instance = new NonFinalPrimitive(1);
            assertThrows(ImmutableValidationException.class, () ->
                    ImmutableValidator.validate(instance));
        }
    }

    @Nested
    class MutableTest {

        @Immutable
        record MutableField(MutableClass mutableClass) {
        }

        static class MutableClass {
            String value;

            MutableClass(String value) {
                this.value = value;
            }
        }

        @Test
        void mutableField() {
            MutableField instance = new MutableField(new MutableClass("test"));
            assertThrows(ImmutableValidationException.class, () ->
                    ImmutableValidator.validate(instance));
        }

        @Test
        void nullMutableField() {
            MutableField instance = new MutableField(null);
            ImmutableValidator.validate(instance);
        }
    }
}