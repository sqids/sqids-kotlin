# [Sqids Kotlin](https://sqids.org/kotlin)

[Sqids](https://sqids.org/kotlin) (*pronounced "squids"*) is a small library that lets you **generate unique IDs from numbers**. It's good for link shortening, fast & URL-safe ID generation and decoding back into numbers for quicker database lookups.

Features:

- **Encode multiple numbers** - generate short IDs from one or several non-negative numbers
- **Quick decoding** - easily decode IDs back into numbers
- **Unique IDs** - generate unique IDs by shuffling the alphabet once
- **ID padding** - provide minimum length to make IDs more uniform
- **URL safe** - auto-generated IDs do not contain common profanity
- **Randomized output** - Sequential input provides nonconsecutive IDs
- **Many implementations** - Support for [40+ programming languages](https://sqids.org/)

## 🧰 Use-cases

Good for:

- Generating IDs for public URLs (eg: link shortening)
- Generating IDs for internal systems (eg: event tracking)
- Decoding for quicker database lookups (eg: by primary keys)

Not good for:

- Sensitive data (this is not an encryption library)
- User IDs (can be decoded revealing user count)

## 🚀 Getting started

Install Sqids via:

Gradle
```
implementation 'org.sqids:sqids-kotlin:0.1.0'
```

or 

Maven
```
<dependency>
  <groupId>org.sqids</groupId>
  <artifactId>sqids-kotlin</artifactId>
  <version>0.1.0</version>
</dependency>
```

Import into project

```kotlin
import org.sqids.Sqids
```

## 👩‍💻 Examples

Simple encode & decode:

```kotlin
val sqids = Sqids()
val id = sqids.encode(listOf<Long>(1, 2, 3)) // "86Rf07"
val numbers = sqids.decode(id) // [1, 2, 3]
```

> **Note**
> 🚧 Because of the algorithm's design, **multiple IDs can decode back into the same sequence of numbers**. If it's important to your design that IDs are canonical, you have to manually re-encode decoded numbers and check that the generated ID matches.

Enforce a *minimum* length for IDs:

```kotlin
val sqids = Sqids(minLength = 10)
val id = sqids.encode(listOf<Long>(1, 2, 3)) // "86Rf07xd4z"
val numbers = sqids.decode(id) // [1, 2, 3]
```

Randomize IDs by providing a custom alphabet:

```kotlin
val sqids = Sqids(alphabet = "FxnXM1kBN6cuhsAvjW3Co7l2RePyY8DwaU04Tzt9fHQrqSVKdpimLGIJOgb5ZE")
val id = sqids.encode(listOf<Long>(1, 2, 3)) // "B4aajs"
val numbers = sqids.decode(id) // [1, 2, 3]
```

Prevent specific words from appearing anywhere in the auto-generated IDs:

```kotlin
val sqids = Sqids(blockList = setOf("86Rf07"))
val id = sqids.encode(listOf<Long>(1, 2, 3)) // "se8ojk"
val numbers = sqids.decode(id) // [1, 2, 3]
```

## 📝 License

[MIT](LICENSE)