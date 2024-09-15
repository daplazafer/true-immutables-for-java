package com.dpf.ti4j.validation

import java.math.BigDecimal
import java.math.BigInteger
import java.nio.file.FileSystems
import java.nio.file.Path
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.ZonedDateTime
import java.util.Optional
import java.util.OptionalDouble
import java.util.OptionalInt
import java.util.OptionalLong
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

object TypeInspector {

    fun isJavaImmutable(clazz: Class<*>): Boolean {
        return when (clazz) {
            Integer::class.java,
            java.lang.Long::class.java,
            java.lang.Double::class.java,
            java.lang.Boolean::class.java,
            Character::class.java,
            java.lang.Byte::class.java,
            java.lang.Short::class.java,
            java.lang.Float::class.java,
            String::class.java,
            Instant::class.java,
            LocalDate::class.java,
            LocalTime::class.java,
            LocalDateTime::class.java,
            ZonedDateTime::class.java,
            OffsetDateTime::class.java,
            OffsetTime::class.java,
            Duration::class.java,
            Period::class.java,
            Optional::class.java,
            OptionalInt::class.java,
            OptionalLong::class.java,
            OptionalDouble::class.java,
            UUID::class.java,
            Path::class.java,
            FileSystems::class.java,
            BigInteger::class.java,
            BigDecimal::class.java,
                -> true

            else -> false
        }
    }

    fun isAtomicReference(obj: Any?): Boolean {
        return obj is AtomicReference<*>
    }

    fun isImmutableCollection(obj: Any?): Boolean {
        return when (obj?.javaClass?.name) {
            "java.util.Collections\$UnmodifiableList",
            "java.util.Collections\$UnmodifiableRandomAccessList",
            "java.util.Collections\$UnmodifiableSet" -> true

            else -> obj?.javaClass?.name?.startsWith("java.util.ImmutableCollections") == true
        }
    }

    fun isImmutableMap(obj: Any?): Boolean {
        return when (obj?.javaClass?.name) {
            "java.util.Collections\$UnmodifiableMap" -> true
            else -> obj?.javaClass?.name?.startsWith("java.util.ImmutableCollections") == true
        }
    }
}