package com.dpf.ti4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ImmutableValidationAspect {

    @Pointcut("execution(*.new(..)) && @within(com.dpf.ti4j.Immutable)")
    public void constructorOfImmutableClass() {
    }

    @AfterReturning("constructorOfImmutableClass()")
    public void validateImmutableClass(final JoinPoint joinPoint) {

        Object instance = joinPoint.getTarget();

        try {

            ImmutableValidator.getInstance().validate(instance);

        } catch (ImmutableValidationException e) {
            throw new IllegalStateException("Instance of '" + instance.getClass().getName() + "' is not immutable.", e);
        }
    }
}