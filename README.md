# TI4J (True Immutables for Java)
**TI4J** is a light-weight library designed to enforce true immutability in Java records and classes. It uses **AspectJ** for runtime validation, ensuring that records and classes annotated with `@Immutable` follow strict immutability rules, checking all fields.

This library requires at least **Java 17**.

## Installation

### Using Maven
First, add the `jitpack.io` repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
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

## How This Magic Works
When you annotate a **record** or **class** with `@Immutable`, **TI4J** automatically ensures that all fields adhere to strict immutability rules. This process happens every time a constructor is invoked, thanks to **Aspect-Oriented Programming (AOP)** using **AspectJ**.

### Constructor Interception
Each time a new instance of **record** or **class** annotated with `@Immutable` is created, an **aspect** intercepts the constructor. Before the constructor completes, **TI4J** triggers the validation process for all the fields in the **record** or **class** recursively to ensure immutability.

### What Does the Validation Check?
The validation process goes through all the fields of the **record** or **class** and checks whether:
- If the field is marked as `@Immutable`, it will be skipped from validation, leaving control of immutability to the user.
- The field is **final**
- The field has **primitive type**
- The field is of a **known immutable type** (e.g., primitives, `String`, `Instant`, `Optional`, etc.).
- The field is a **collection** or **map** marked as immutable (using `Collections.unmodifiableList()`, `Map.of()`, etc. to instantiate).
- The field is another immutable **record** or **class**.

### Custom Exclusions
If you need to allow certain **field** to by-pass validation, you can annotate it with `@Immutable`, and it will be excluded from the immutability check.

**DISCLAIMER**: The use of this feature is generally **discouraged** in TI4J, as the goal of this library is to enforce strict immutability in your objects. Immutability leads to safer, more predictable, and easier-to-maintain code, as it reduces the risks of unintended side effects caused by mutable state.
However, we understand that certain software requirements may necessitate the use of mutable fields or external components that are beyond our control. Use this sparingly and only when absolutely necessary.

## Permitted Data Types in TI4J
In **TI4J**, the following types are considered immutable by default and will pass the immutability validation. Any **record** annotated with `@Immutable` is also allowed.

#### General Rules:
- **Primitives**: All primitive data types (`int`, `long`, `double`, etc.) are allowed.
- **Wrapper Classes and Known Immutables**: Java wrapper classes (such as `Long`, `Integer`) and well-known immutable types (like `String`, `Instant`, `Optional`) are permitted without further validation.
- **Immutable Collections**: Immutable collections (like those created using `Collections.unmodifiableList()`, etc.) are allowed.
- **`AtomicReference`**: `AtomicReference` types are allowed if they contain immutable values.
- **Custom Records or Classes**: Any **record** or **class** annotated with `@Immutable` will be validated for immutability.
- **Mutable Fields**: You can annotate any record, class, or field with `@Immutable`, and these will be excluded from validation.

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
    - `Instant`
    - `LocalDate`
    - `LocalTime`
    - `LocalDateTime`
    - `ZonedDateTime`
    - `OffsetDateTime`
    - `OffsetTime`
    - `Duration`
    - `Period`
- **Optional Types**:
    - `Optional`
    - `OptionalInt`
    - `OptionalLong`
    - `OptionalDouble`
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

**DISCLAIMER**: The use of `raw type` collections is not allowed due to the need for type safety and to ensure that all elements within collections are immutable. TI4J requires all collections, maps, and references to be explicitly typed with generics (e.g., `List<String>`, `Map<String, Integer>`) to enable proper validation of immutability. Raw types, such as `List` or `Map` without generic type parameters, will result in validation errors.

## Usage
Annotate your record with `@Immutable` to enforce immutability checks. Use the `@Immutable` annotation to exclude specific fields from immutability validation.

### Example
```java
@Immutable
public record MyImmutableRecord(
    Long id, // Immutable field by default
    MyOtherImmutableClass myCustomField, // MyOtherImmutableClass must be Immutable
    AtomicReference<MyThirdImmutableRecord> myAtomicReferenceField ,// MyThirdImmutableRecord must be Immutable too
    @Immutable List<String> mutableList // Mutable field excluded from validation
) {}
```

The `@Immutable` annotation enforces immutability rules on all fields except those marked with `@Immutable`, which are excluded from the checks.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

### Dependencies

This project makes use of third-party dependencies that are governed by their respective licenses:

- **Kotlin** is licensed under the **Apache License 2.0**. More details can be found at [https://www.apache.org/licenses/LICENSE-2.0](https://www.apache.org/licenses/LICENSE-2.0).
- **AspectJ** is licensed under the **Eclipse Public License (EPL) 1.0**. More details can be found at [https://www.eclipse.org/legal/epl-v10.html](https://www.eclipse.org/legal/epl-v10.html).
