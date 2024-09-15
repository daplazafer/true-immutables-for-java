package com.dpf.ti4j.aop

import com.dpf.ti4j.exception.ImmutabilityValidationException
import com.dpf.ti4j.validation.ImmutableValidator
import com.dpf.ti4j.validation.TypeInspector
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut

@Aspect
class ImmutabilityCheckerAspect {

    private val typeInspector: TypeInspector = TypeInspector()
    private val immutableValidator: ImmutableValidator = ImmutableValidator(typeInspector)

    @Pointcut("execution(*.new(..)) && @within(Immutable)")
    fun constructorOfImmutableClass() {
    }

    @Before("constructorOfImmutableClass()")
    fun validateImmutableClass(joinPoint: org.aspectj.lang.JoinPoint) {
        val instance = joinPoint.target

        try {
            immutableValidator.validate(instance)
        } catch (e: ImmutabilityValidationException) {
            throw IllegalStateException("Class ${instance::class.java.name} is not immutable", e)
        }
    }
}