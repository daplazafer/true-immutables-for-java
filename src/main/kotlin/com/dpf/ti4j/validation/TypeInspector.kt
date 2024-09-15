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

object TypeInspector {

    fun isJavaImmutable(obj: Class<*>): Boolean {
        return when (obj) {
            Integer::class.java,
            Long::class.java,
            Double::class.java,
            Boolean::class.java,
            Char::class.java,
            Byte::class.java,
            Short::class.java,
            Float::class.java,
            Character::class.java,
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

    fun isImmutableCollection(obj: Any?): Boolean {
        return when (obj) {
            is List<*> -> obj::class.java.name == "java.util.Collections\$UnmodifiableList"
            is Set<*> -> obj::class.java.name == "java.util.Collections\$UnmodifiableSet"
            else -> false
        }
    }

    fun isImmutableMap(obj: Any?): Boolean {
        return when (obj) {
            is Map<*, *> -> obj::class.java.name == "java.util.Collections\$UnmodifiableMap"
            else -> false
        }
    }
}