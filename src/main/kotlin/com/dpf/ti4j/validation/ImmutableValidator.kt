package com.dpf.ti4j.validation

import com.dpf.ti4j.Immutable
import com.dpf.ti4j.exception.ImmutabilityValidationException
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.concurrent.atomic.AtomicReference


class ImmutableValidator(private val typeInspector: TypeInspector) {

    fun validate(instance: Any) {
        val clazz = instance::class.java

        for (field in clazz.declaredFields) {

            if (Modifier.isStatic(field.modifiers)) continue

            val originalCanAccess = field.canAccess(instance)
            try {
                field.trySetAccessible()

                if (field.isAnnotationPresent(Immutable::class.java)) continue
                if (field.type.isAnnotationPresent(Immutable::class.java)) continue

                validateFinalModifier(clazz, field)

                if (field.type.isPrimitive) continue

                val fieldValue = field.get(instance)
                fieldValue?.let {
                    when {
                        typeInspector.isAtomicReference(fieldValue) -> {
                            validateAtomicReference(fieldValue)
                            return@let
                        }

                        fieldValue is Collection<*> -> {
                            if (!typeInspector.isImmutableCollection(fieldValue)) {
                                throw ImmutabilityValidationException("Field ${field.name} in class ${clazz.name} is a mutable Collection.")
                            }
                            validateCollection(fieldValue)
                            return@let
                        }

                        fieldValue is Map<*, *> -> {
                            if (!typeInspector.isImmutableMap(fieldValue)) {
                                throw ImmutabilityValidationException("Field ${field.name} in class ${clazz.name} is a mutable Map.")
                            }
                            validateMap(fieldValue)
                            return@let
                        }

                        else -> validate(fieldValue)
                    }
                }

                if (typeInspector.isJavaImmutable(field.type)) {
                    continue
                } else {
                    throw ImmutabilityValidationException("Field ${field.name} in class ${clazz.name} is mutable.")
                }

            } finally {
                field.isAccessible = originalCanAccess
            }
        }
    }

    private fun validateFinalModifier(clazz: Class<*>, field: Field) {
        if (!Modifier.isFinal(field.modifiers)) {
            throw ImmutabilityValidationException("Field ${field.name} in class ${clazz.name} is not final.")
        }
    }

    private fun validateAtomicReference(atomicReference: Any) {
        val ref = atomicReference as AtomicReference<*>
        ref.get()?.let { validate(it) }
    }

    private fun validateCollection(collection: Any) {
        (collection as Collection<*>).forEach { item ->
            item?.let { validate(it) }
        }
    }

    private fun validateMap(map: Any) {
        (map as Map<*, *>).forEach { (key, value) ->
            key?.let { validate(it) }
            value?.let { validate(it) }
        }
    }
}