package org.sqids

import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EncodeTests {
    private val sqids = Sqids()

    @Test
    fun simple() {
        val numbers = listOf(1L, 2L, 3L)
        val id = "86Rf07"
        assertEquals(sqids.encode(numbers), id)
        assertEquals(sqids.decode(id), numbers)
    }

    @Test
    fun differentInputs() {
        val numbers = listOf(
            0L,
            0L,
            0L,
            1L,
            2L,
            3L,
            100L,
            1000L,
            100000L,
            1000000L,
            Long.MAX_VALUE
        )
        assertEquals(sqids.decode(sqids.encode(numbers)), numbers)
    }

    @Test
    fun incrementalNumber() {
        val ids = mapOf(
                "bM" to listOf(0L),
                "Uk" to listOf(1L),
                "gb" to listOf(2L),
                "Ef" to listOf(3L),
                "Vq" to listOf(4L),
                "uw" to listOf(5L),
                "OI" to listOf(6L),
                "AX" to listOf(7L),
                "p6" to listOf(8L),
                "nJ" to listOf(9L)
        )
        for (id in ids.keys) {
            val numbers = ids[id]!!
            assertEquals(sqids.encode(numbers), id)
            assertEquals(sqids.decode(id), numbers)
        }
    }

    @Test
    fun incrementalNumbers() {
        var ids = mapOf(
                "SvIz" to listOf(0L, 0L),
                "n3qa" to listOf(0L, 1L),
                "tryF" to listOf(0L, 2L),
                "eg6q" to listOf(0L, 3L),
                "rSCF" to listOf(0L, 4L),
                "sR8x" to listOf(0L, 5L),
                "uY2M" to listOf(0L, 6L),
                "74dI" to listOf(0L, 7L),
                "30WX" to listOf(0L, 8L),
                "moxr" to listOf(0L, 9L)
        )

        for ((id, numbers) in ids.entries) {
            assertEquals(sqids.encode(numbers), id)
            assertEquals(sqids.decode(id), numbers)
        }

        ids = mapOf(
            "SvIz" to listOf(0L, 0L),
            "nWqP" to listOf(1L, 0L),
            "tSyw" to listOf(2L, 0L),
            "eX68" to listOf(3L, 0L),
            "rxCY" to listOf(4L, 0L),
            "sV8a" to listOf(5L, 0L),
            "uf2K" to listOf(6L, 0L),
            "7Cdk" to listOf(7L, 0L),
            "3aWP" to listOf(8L, 0L),
            "m2xn" to listOf(9L, 0L)
        )

        for ((id, numbers) in ids.entries) {
            assertEquals(sqids.encode(numbers), id)
            assertEquals(sqids.decode(id), numbers)
        }
    }

    @Test
    fun multiInput() {
        val numbers = listOf(
            0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L,
            21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L, 31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L,
            39L, 40L, 41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L, 50L, 51L, 52L, 53L, 54L, 55L, 56L,
            57L, 58L, 59L, 60L, 61L, 62L, 63L, 64L, 65L, 66L, 67L, 68L, 69L, 70L, 71L, 72L, 73L, 74L,
            75L, 76L, 77L, 78L, 79L, 80L, 81L, 82L, 83L, 84L, 85L, 86L, 87L, 88L, 89L, 90L, 91L, 92L,
            93L, 94L, 95L, 96L, 97L, 98L, 99L
        )
        assertEquals(sqids.decode(sqids.encode(numbers)), numbers)
    }

    @Test
    fun encodeNoNumbers() {
        assertEquals(sqids.encode(listOf()), "")
    }

    @Test
    fun decodeEmptyString() {
        assertEquals(sqids.decode(""), listOf())
    }

    @Test
    fun decodeInvalidCharacter() {
        assertEquals(sqids.decode("*"), listOf())
    }

    @Test
    fun encodeOutOfRangeNumbers() {
        val expectedException = "Encoding supports numbers between 0 and ${Long.MAX_VALUE}"
        var exception = assertFailsWith<RuntimeException> { sqids.encode(listOf(-1L)) }
        assertEquals(expectedException ,exception.message)
        exception = assertFailsWith<RuntimeException> { sqids.encode(listOf(Long.MAX_VALUE + 1)) }
        assertEquals(expectedException ,exception.message)
    }
}