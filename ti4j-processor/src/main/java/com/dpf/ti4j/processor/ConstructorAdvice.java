package com.dpf.ti4j.processor;

import com.dpf.ti4j.core.Immutable;
import com.dpf.ti4j.core.ImmutableValidator;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.This;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConstructorAdvice {

    public static final Logger logger = LoggerFactory.getLogger(ConstructorAdvice.class);

    @OnMethodExit
    static void validate(@This Object instance) {

        logger.debug("Validating '{}'", instance.getClass().getName());

        if (instance.getClass().isAnnotationPresent(Immutable.class)) {
            ImmutableValidator.validate(instance);
        }
    }

}