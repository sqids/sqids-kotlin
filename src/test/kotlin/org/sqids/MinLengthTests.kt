package org.sqids

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MinLengthTests {

    @Test
    fun simple() {
        val sqids = Sqids(minLength = DEFAULT_ALPHABET.length)
        val numbers = listOf(1L, 2L, 3L)
        val id = "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTM"
        assertEquals(sqids.encode(numbers), id)
        assertEquals(sqids.decode(id), numbers)
    }

    @Test
    fun incremental() {
        val numbers = listOf(1L, 2L, 3L)
        val ids = mapOf(
            6 to "86Rf07",
            7 to "86Rf07x",
            8 to "86Rf07xd",
            9 to "86Rf07xd4",
            10 to "86Rf07xd4z",
            11 to "86Rf07xd4zB",
            12 to "86Rf07xd4zBm",
            13 to "86Rf07xd4zBmi",
            DEFAULT_ALPHABET.length + 0 to "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTM",
            DEFAULT_ALPHABET.length + 1 to "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTMy",
            DEFAULT_ALPHABET.length + 2 to "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTMyf",
            DEFAULT_ALPHABET.length + 3 to "86Rf07xd4zBmiJXQG6otHEbew02c3PWsUOLZxADhCpKj7aVFv9I8RquYrNlSTMyf1"
        )
        for ((minLength, id) in ids.entries) {
            val sqids = Sqids(minLength = minLength)
            assertEquals(sqids.encode(numbers), id)
            assertEquals(sqids.decode(id), numbers)
        }
    }

    @Test
    fun incrementalNumbers() {
        val sqids = Sqids(minLength = DEFAULT_ALPHABET.length)
        val ids = mapOf(
            "SvIzsqYMyQwI3GWgJAe17URxX8V924Co0DaTZLtFjHriEn5bPhcSkfmvOslpBu" to listOf(0L, 0L),
            "n3qafPOLKdfHpuNw3M61r95svbeJGk7aAEgYn4WlSjXURmF8IDqZBy0CT2VxQc" to listOf(0L, 1L),
            "tryFJbWcFMiYPg8sASm51uIV93GXTnvRzyfLleh06CpodJD42B7OraKtkQNxUZ" to listOf(0L, 2L),
            "eg6ql0A3XmvPoCzMlB6DraNGcWSIy5VR8iYup2Qk4tjZFKe1hbwfgHdUTsnLqE" to listOf(0L, 3L),
            "rSCFlp0rB2inEljaRdxKt7FkIbODSf8wYgTsZM1HL9JzN35cyoqueUvVWCm4hX" to listOf(0L, 4L),
            "sR8xjC8WQkOwo74PnglH1YFdTI0eaf56RGVSitzbjuZ3shNUXBrqLxEJyAmKv2" to listOf(0L, 5L),
            "uY2MYFqCLpgx5XQcjdtZK286AwWV7IBGEfuS9yTmbJvkzoUPeYRHr4iDs3naN0" to listOf(0L, 6L),
            "74dID7X28VLQhBlnGmjZrec5wTA1fqpWtK4YkaoEIM9SRNiC3gUJH0OFvsPDdy" to listOf(0L, 7L),
            "30WXpesPhgKiEI5RHTY7xbB1GnytJvXOl2p0AcUjdF6waZDo9Qk8VLzMuWrqCS" to listOf(0L, 8L),
            "moxr3HqLAK0GsTND6jowfZz3SUx7cQ8aC54Pl1RbIvFXmEJuBMYVeW9yrdOtin" to listOf(0L, 9L)
        )
        for ((id, numbers) in ids.entries) {
            assertEquals(sqids.encode(numbers), id)
            assertEquals(sqids.decode(id), numbers)
        }
    }

    @Test
    fun minLengths() {
        val minLengths = listOf(0, 1, 5, 10, DEFAULT_ALPHABET.length)
        val numbers =  listOf(
            listOf(0L),
            listOf(0L, 0L, 0L, 0L, 0L),
            listOf(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L),
            listOf(100L, 200L, 300L),
            listOf(1_000L, 2_000L, 3_000L),
            listOf(Long.MAX_VALUE),
        )
        for (minLength in minLengths) {
            val sqids = Sqids(minLength = minLength)
            for (number in numbers) {
                val id = sqids.encode(number)
                assertTrue(id.length >= minLength)
                assertEquals(sqids.decode(id), number)
            }
        }
    }

    @Test
    fun encodeOutOfRangeNumbers() {
        val expectedException = "Minimum length has to be between 0 and $MIN_LENGTH_LIMIT"
        val minLengthLimit = 255
        var exception = assertFailsWith<RuntimeException> { Sqids(minLength = -1) }
        assertEquals(expectedException, exception.message)
        exception = assertFailsWith<RuntimeException> { Sqids(minLength = minLengthLimit + 1) }
        assertEquals(expectedException, exception.message)
    }
}