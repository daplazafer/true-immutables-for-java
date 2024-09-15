package com.dpf.ti4j.aop

import com.dpf.ti4j.validation.ImmutableValidator
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut

@Aspect
class ImmutabilityCheckerAspect {

    @Pointcut("execution(*.new(..)) && @within(Immutable)")
    fun constructorOfImmutableClass() {}

    @Before("constructorOfImmutableClass()")
    fun validateImmutableClass() {
        val clazz = this::class.java

        try {
            ImmutableValidator.validate(clazz)
        } catch (e: Exception) {
            throw IllegalStateException("Class ${clazz.name} is not immutable", e)
        }
    }
}