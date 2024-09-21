package com.dpf.ti4j.processor;

import com.dpf.ti4j.core.ImmutableValidationException;

import com.dpf.ti4j.processor.model.ImmutableFoo;
import com.dpf.ti4j.processor.model.MutableFoo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


class ImmutableAnnotationIT {

    @BeforeAll
    static void setup() {
        ImmutableAnnotationProcessor.processImmutableAnnotations();
    }

    @Test
    void testValid() {
        new ImmutableFoo(1);
    }

    @Test
    void testInvalid() {
        assertThrows(ImmutableValidationException.class, () ->
                new MutableFoo(1));
    }
}
