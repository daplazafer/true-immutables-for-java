package com.dpf.ti4j.validation

import com.dpf.ti4j.Immutable
import com.dpf.ti4j.Mutable
import java.util.*
import java.util.concurrent.atomic.AtomicReference

object ImmutableValidator {

    private val validatedClasses = Collections.newSetFromMap(WeakHashMap<Class<*>, Boolean>())

    fun validate(record: Class<*>) {

        if (validatedClasses.contains(record)) return

        for (field in record.declaredFields) {

            if (field.isAnnotationPresent(Mutable::class.java)) continue
            if (field.type.isAnnotationPresent(Mutable::class.java)) continue
            if (field.type.isAnnotationPresent(Immutable::class.java)) continue
            if (field.type.isPrimitive) continue
            if (TypeInspector.isJavaImmutable(field.type)) continue

            val fieldValue = field.get(null)
            if (fieldValue != null && AtomicReference::class.java.isAssignableFrom(field.type)) {
                validateAtomicReferenceElement(record.name, field.name, fieldValue as AtomicReference<*>)
                continue
            }
            if (fieldValue != null && TypeInspector.isImmutableCollection(fieldValue)) {
                validateCollectionElements(record.name, field.name, fieldValue as Collection<*>)
                continue
            }
            if (fieldValue != null && TypeInspector.isImmutableMap(fieldValue)) {
                validateMapElements(record.name, field.name, fieldValue as Map<*, *>)
                continue
            }

            throw IllegalStateException("Field ${field.name} in record ${record.name} is mutable")
        }

        validatedClasses.add(record)
    }

    private fun validateAtomicReferenceElement(
        recordName: String,
        fieldName: String,
        atomicReference: AtomicReference<*>
    ) {
        val innerValue = atomicReference.get()
        try {
            if (innerValue != null)
                validate(innerValue::class.java)
        } catch (e: IllegalStateException) {
            throw IllegalStateException(
                "Field $fieldName in record $recordName is a AtomicReference containing mutable element: ${innerValue::class.java.name}",
                e
            )
        }
    }

    private fun validateCollectionElements(
        recordName: String,
        fieldName: String,
        collection: Collection<*>
    ) {
        collection.forEach { item ->
            if (item != null) {
                try {
                    validate(item::class.java)
                } catch (e: IllegalStateException) {
                    throw IllegalStateException(
                        "Field $fieldName in record $recordName is a Collection containing mutable element: ${item::class.java.name}",
                        e
                    )
                }
            }
        }
    }

    private fun validateMapElements(
        recordName: String,
        fieldName: String,
        map: Map<*, *>
    ) {
        map.forEach { (key, value) ->
            if (key != null) {
                try {
                    validate(key::class.java)
                } catch (e: IllegalStateException) {
                    throw IllegalStateException(
                        "Field $fieldName in record $recordName is a Map containing mutable key: ${key::class.java.name}",
                        e
                    )
                }
            }
            if (value != null) {
                try {
                    validate(value::class.java)
                } catch (e: IllegalStateException) {
                    throw IllegalStateException(
                        "Field $fieldName in record $recordName is a Map containing mutable value: ${value::class.java.name}",
                        e
                    )
                }
            }
        }
    }
}