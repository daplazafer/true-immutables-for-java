# TI4J (True Immutables for Java)

**TI4J** is a lightweight library designed to enforce true immutability in Java records. It uses **AspectJ** for runtime validation, ensuring that records annotated with `@Immutable` follow strict immutability rules, checking all fields. Users can exclude specific fields and classes using the `@Mutable` annotation.

This library requires at least **Java 17**.

## Installation

### Using Maven

First, add the `jitpack.io` repository to your `pom.xml`:

```xml
<repositories>
    ...
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    ...
</repositories>
```

Then, add the following dependency to your `pom.xml`:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.github.daplazafer</groupId>
        <artifactId>ti4j</artifactId>
        <version>1.0.0</version>
    </dependency>
    ...
</dependencies>
```

### Using Gradle

Add the following lines to your `build.gradle`:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
    implementation 'com.github.daplazafer:ti4j:1.0.0'
}
```

## Permitted Data Types in TI4J

In **TI4J**, the following types are considered immutable by default and will pass the immutability validation. Any **record** annotated with `@Immutable` is also allowed, unless explicitly marked with `@Mutable`.

#### General Rules:
- **Primitives**: All primitive data types (`int`, `long`, `double`, etc.) are allowed.
- **Wrapper Classes and Known Immutables**: Java wrapper classes (such as `Long`, `Integer`) and well-known immutable types (like `String`, `Instant`, `Optional`) are permitted without further validation.
- **Immutable Collections**: Immutable collections (like those created using `Collections.unmodifiableList()`, etc.) are allowed.
- **`AtomicReference`**: `AtomicReference` types are allowed if they contain immutable values.
- **Custom Records or Classes**: Any **record** or **class** annotated with `@Immutable` will be validated for immutability.
- **Mutable Fields**: You can annotate any record, class, or field with `@Mutable`, and these will be excluded from validation.

#### Immutable Types:
- **Primitives**: `int`, `long`, `double`, `boolean`, `char`, `byte`, `short`, `float`
- **Wrapper Classes**:
    - `Integer`
    - `Long`
    - `Double`
    - `Boolean`
    - `Character`
    - `Byte`
    - `Short`
    - `Float`
- **Java Time API**:
    - `java.time.Instant`
    - `java.time.LocalDate`
    - `java.time.LocalTime`
    - `java.time.LocalDateTime`
    - `java.time.ZonedDateTime`
    - `java.time.OffsetDateTime`
    - `java.time.OffsetTime`
    - `java.time.Duration`
    - `java.time.Period`
- **Optional Types**:
    - `java.util.Optional`
    - `java.util.OptionalInt`
    - `java.util.OptionalLong`
    - `java.util.OptionalDouble`
- **Other known Immutable Types**:
    - `String`
    - `UUID`
    - `Path` (from `java.nio.file.Path`)
    - `FileSystems` (from `java.nio.file.FileSystems`)
    - `BigInteger`
    - `BigDecimal`

#### Immutable Collections:
- **Lists**: Immutable lists created with `Collections.unmodifiableList()` (and also `List.of()`).
- **Sets**: Immutable sets created with `Collections.unmodifiableSet()` (and also `Set.of()`).
- **Maps**: Immutable maps created with `Collections.unmodifiableMap()` (and also `Map.of()`).

## Usage

Annotate your record with `@Immutable` to enforce immutability checks. Use the `@Mutable` annotation to exclude specific fields from immutability validation.

### Example

```java
@Immutable
public record MyImmutableRecord(
    Long id, // Immutable field by default
    MyOtherImmutableRecord myCustomField, // MyOtherImmutableRecord must be annotated with @Immutable
    AtomicReference<MyThirdImmutableRecord> myAtomicReferenceField ,// MyThirdImmutableRecord must be annotated with @Immutable too
    @Mutable List<String> mutableList // Mutable field excluded from validation
) {}
```

The `@Immutable` annotation enforces immutability rules on all fields except those marked with `@Mutable`, which are excluded from the checks.
