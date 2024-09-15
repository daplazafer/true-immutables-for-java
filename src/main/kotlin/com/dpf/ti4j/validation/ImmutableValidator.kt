package com.dpf.ti4j.validation

import com.dpf.ti4j.Immutable
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType


object ImmutableValidator {

    fun validate(clazz: Class<*>) {

        for (field in clazz.declaredFields) {

            if (Modifier.isStatic(field.modifiers)) continue

            val originalCanAccess = field.canAccess(null)
            try {
                field.trySetAccessible()

                if (field.isAnnotationPresent(Immutable::class.java)) continue
                if (field.type.isAnnotationPresent(Immutable::class.java)) continue

                validateFinalModifier(clazz, field)

                if (field.type.isPrimitive) continue
                if (TypeInspector.isJavaImmutable(field.type)) continue

                field.get(null)?.let { fieldValue ->
                    when {
                        TypeInspector.isAtomicReference(fieldValue) -> {
                            validateAtomicReference(clazz, field)
                            return@let
                        }

                        TypeInspector.isImmutableCollection(fieldValue) -> {
                            validateCollection(clazz, field)
                            return@let
                        }

                        TypeInspector.isImmutableMap(fieldValue) -> {
                            validateMap(clazz, field)
                            return@let
                        }
                    }
                }

                validateSubclasses(field.type)
                validate(field.type)

            } finally {
                field.isAccessible = originalCanAccess
            }
        }
    }

    private fun validateFinalModifier(clazz: Class<*>, field: Field) {

        if (!Modifier.isFinal(field.modifiers))
            throw IllegalStateException("Field ${field.name} in class ${clazz.name} is not final.")
    }

    private fun validateAtomicReference(clazz: Class<*>, field: Field) {

        if (field.type.genericSuperclass !is ParameterizedType)
            throw IllegalStateException("Field ${field.name} in class ${clazz.name} is an AtomicReference without generic types.")

        val parameterizedType = field.type.genericSuperclass as ParameterizedType
        val actualTypeArgument = parameterizedType.actualTypeArguments[0] as Class<*>

        validate(actualTypeArgument)
    }

    private fun validateCollection(clazz: Class<*>, field: Field) {

        if (field.type.genericSuperclass !is ParameterizedType)
            throw IllegalStateException("Field ${field.name} in class ${clazz.name} is a raw Collection without generic types.")

        val parameterizedType = field.type.genericSuperclass as ParameterizedType
        val actualTypeArgument = parameterizedType.actualTypeArguments[0] as Class<*>

        validate(actualTypeArgument)
    }

    private fun validateMap(clazz: Class<*>, field: Field) {

        if (field.type.genericSuperclass !is ParameterizedType)
            throw IllegalStateException("Field ${field.name} in class ${clazz.name} is a raw Map without generic types.")

        val parameterizedType = field.type.genericSuperclass as ParameterizedType
        val keyTypeArgument = parameterizedType.actualTypeArguments[0] as Class<*>
        val valueTypeArgument = parameterizedType.actualTypeArguments[1] as Class<*>

        validate(keyTypeArgument)
        validate(valueTypeArgument)
    }

    private fun validateSubclasses(clazz: Class<*>) {
        val reflections = Reflections(
            ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.SubTypes)
        )
        val subclasses = reflections.getSubTypesOf(clazz)

        for (subclass in subclasses)
            validate(subclass)
    }
}