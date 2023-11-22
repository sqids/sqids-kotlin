package org.sqidskotlin.sqids

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.Test

class BlockListTests {
    @Test
    fun blockList() {
        val sqids = Sqids()
        val numbers = listOf(4572721L)
        assertEquals(sqids.decode("aho1e"), numbers)
        assertEquals(sqids.encode(numbers), "JExTR")
    }

    @Test
    fun emptyBlockList() {
        val sqids = Sqids(blockList = setOf())
        val numbers = listOf(4572721L)
        assertEquals(sqids.decode("aho1e"), numbers)
        assertEquals(sqids.encode(numbers), "aho1e")
    }

    @Test
    fun nonEmptyBlockList() {
        val sqids = Sqids(blockList = setOf("ArUO"))
        var numbers = listOf(4572721L)

        assertEquals(sqids.decode("aho1e"), numbers)
        assertEquals(sqids.encode(numbers), "aho1e")

        numbers = listOf(100000L)
        assertEquals(sqids.decode("ArUO"), numbers)
        assertEquals(sqids.encode(numbers), "QyG4")
        assertEquals(sqids.decode("QyG4"), numbers)
    }

    @Test
    fun encodeBlockList() {
        val sqids = Sqids(blockList = setOf(
            "JSwXFaosAN",  // normal result of 1st encoding, let's block that word on purpose
            "OCjV9JK64o",  // result of 2nd encoding
            "rBHf",  // result of 3rd encoding is `4rBHfOiqd3`, let's block a substring
            "79SM",  // result of 4th encoding is `dyhgw479SM`, let's block the postfix
            "7tE6" // result of 4th encoding is `7tE6jdAHLe`, let's block the prefix
        ))
        val numbers = listOf(1_000_000L, 2_000_000L)
        assertEquals(sqids.encode(numbers), "1aYeB7bRUt")
        assertEquals(sqids.decode("1aYeB7bRUt"), numbers)
    }

    @Test
    fun decodeBlockList() {
        val sqids = Sqids(blockList = setOf(
            "86Rf07",
            "se8ojk",
            "ARsz1p",
            "Q8AI49",
            "5sQRZO"
        ))
        val numbers = listOf(1L, 2L, 3L)
        assertEquals(sqids.decode("86Rf07"), numbers)
        assertEquals(sqids.decode("se8ojk"), numbers)
        assertEquals(sqids.decode("ARsz1p"), numbers)
        assertEquals(sqids.decode("Q8AI49"), numbers)
        assertEquals(sqids.decode("5sQRZO"), numbers)
    }

    @Test
    fun shortBlockList() {
        val sqids = Sqids(blockList = setOf("pnd"))
        val numbers = listOf(1000L)
        assertEquals(sqids.decode(sqids.encode(numbers)), numbers)
    }

    @Test
    fun lowercaseBlockList() {
        val sqids = Sqids(
            alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            blockList = setOf("sxnzkl")
        )
        val numbers = listOf(1L, 2L, 3L)
        assertEquals(sqids.encode(numbers), "IBSHOZ")
        assertEquals(sqids.decode("IBSHOZ"), numbers)
    }

    @Test
    fun maxBlockList() {
        val sqids = Sqids(
            alphabet = "abc",
            minLength = 3,
            blockList = setOf("cab", "abc", "bca")
        )
        val exception = assertFailsWith<RuntimeException> { sqids.encode(listOf(0L)) }
        assertEquals("Reached max attempts to re-generate the ID", exception.message)
    }
}