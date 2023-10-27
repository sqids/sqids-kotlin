import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldHaveMinLength
import org.sqids.DEFAULT_ALPHABET
import org.sqids.MIN_LENGTH_LIMIT
import org.sqids.Sqids

class MinLengthTests :
    FunSpec({
        test("simple") {
            Sqids(minLength = DEFAULT_ALPHABET.length)
                .shouldTranscodeBetweenValues(
                    listOf(1, 2, 3),
                    "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTM"
                )
        }
        context("incremental") {
            val defaultAlphabetLength = DEFAULT_ALPHABET.length
            withData(
                Pair(6, "86Rf07"),
                Pair(7, "86Rf07x"),
                Pair(8, "86Rf07xd"),
                Pair(9, "86Rf07xd4"),
                Pair(10, "86Rf07xd4z"),
                Pair(11, "86Rf07xd4zB"),
                Pair(12, "86Rf07xd4zBm"),
                Pair(13, "86Rf07xd4zBmi"),
                Pair(
                    defaultAlphabetLength + 0,
                    "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTM"
                ),
                Pair(
                    defaultAlphabetLength + 1,
                    "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTMy"
                ),
                Pair(
                    defaultAlphabetLength + 2,
                    "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTMyf"
                ),
                Pair(
                    defaultAlphabetLength + 3,
                    "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTMyf1"
                ),
            ) { (minLength, id) ->
                Sqids(minLength = minLength).run {
                    assertSoftly {
                        encode(listOf(1, 2, 3)).shouldBe(id).shouldHaveLength(minLength)
                        decode(id) shouldBe listOf(1, 2, 3)
                    }
                }
            }
        }
        context("incremental numbers") {
            withData(
                SqidsPair(
                    listOf(0, 0),
                    "SvIzsqYMyQwI3GWgJAe17URxX8V924Co0DaTZLtFjHriEn5bPhcSkfmvOslpBu"
                ),
                SqidsPair(
                    listOf(0, 1),
                    "n3qafPOLKdfHpuNw3M61r95svbeJGk7aAEgYn4WlSjXURmF8IDqZBy0CT2VxQc"
                ),
                SqidsPair(
                    listOf(0, 2),
                    "tryFJbWcFMiYPg8sASm51uIV93GXTnvRzyfLleh06CpodJD42B7OraKtkQNxUZ"
                ),
                SqidsPair(
                    listOf(0, 3),
                    "eg6ql0A3XmvPoCzMlB6DraNGcWSIy5VR8iYup2Qk4tjZFKe1hbwfgHdUTsnLqE"
                ),
                SqidsPair(
                    listOf(0, 4),
                    "rSCFlp0rB2inEljaRdxKt7FkIbODSf8wYgTsZM1HL9JzN35cyoqueUvVWCm4hX"
                ),
                SqidsPair(
                    listOf(0, 5),
                    "sR8xjC8WQkOwo74PnglH1YFdTI0eaf56RGVSitzbjuZ3shNUXBrqLxEJyAmKv2"
                ),
                SqidsPair(
                    listOf(0, 6),
                    "uY2MYFqCLpgx5XQcjdtZK286AwWV7IBGEfuS9yTmbJvkzoUPeYRHr4iDs3naN0"
                ),
                SqidsPair(
                    listOf(0, 7),
                    "74dID7X28VLQhBlnGmjZrec5wTA1fqpWtK4YkaoEIM9SRNiC3gUJH0OFvsPDdy"
                ),
                SqidsPair(
                    listOf(0, 8),
                    "30WXpesPhgKiEI5RHTY7xbB1GnytJvXOl2p0AcUjdF6waZDo9Qk8VLzMuWrqCS"
                ),
                SqidsPair(
                    listOf(0, 9),
                    "moxr3HqLAK0GsTND6jowfZz3SUx7cQ8aC54Pl1RbIvFXmEJuBMYVeW9yrdOtin"
                )
            ) { (input, output) ->
                Sqids(minLength = DEFAULT_ALPHABET.length)
                    .shouldTranscodeBetweenValues(input, output)
            }
        }
        context("min lengths") {
            withData(0, 1, 5, 10, DEFAULT_ALPHABET.length) { minLength ->
                withData<List<Long>>(
                    nameFn = { "[${it.joinToString(", ")}]" },
                    listOf(0),
                    listOf(0, 0, 0, 0, 0),
                    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                    listOf(100, 200, 300),
                    listOf(1_000, 2_000, 3_000),
                    listOf(1_000_000),
                    listOf(Long.MAX_VALUE),
                ) { numbers ->
                    Sqids(minLength = minLength).run {
                        val id = encode(numbers)
                        assertSoftly {
                            id shouldHaveMinLength minLength
                            decode(id) shouldBe numbers
                        }
                    }
                }
            }
        }
        test("out-of-range invalid min length") {
            val minLengthError = "Minimum length has to be between 0 and $MIN_LENGTH_LIMIT"
            shouldThrowWithMessage<IllegalArgumentException>(minLengthError) {
                Sqids(minLength = -1)
            }
            shouldThrowWithMessage<IllegalArgumentException>(minLengthError) {
                Sqids(minLength = MIN_LENGTH_LIMIT + 1)
            }
        }
    })
