package com.dpf.ti4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import static com.dpf.ti4j.ImmutableValidator.validate;

@Aspect
public class ImmutableValidationAspect {

    @Pointcut("execution(*.new(..)) && @within(com.dpf.ti4j.Immutable)")
    public void constructorOfImmutableClass() {
    }

    @AfterReturning("constructorOfImmutableClass()")
    public void validateImmutableClass(final JoinPoint joinPoint) {

        final var instance = joinPoint.getTarget();

        try {

            validate(instance);

        } catch (ImmutableValidationException e) {
            throw new IllegalStateException("Instance of '" + instance.getClass().getName() +
                    "' annotated as @Immutable is not immutable.", e);
        }
    }
}