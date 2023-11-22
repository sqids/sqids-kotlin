package org.sqidskotlin.sqids

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.Test

class AlphabetTests {
    @Test
    fun simpleAlphabet() {
        val sqids = Sqids(alphabet = "0123456789abcdef")
        val numbers = listOf(1L, 2L, 3L)
        val id = "489158"
        assertEquals(sqids.encode(numbers), id)
        assertEquals(sqids.decode(id), numbers)
    }

    @Test
    fun shortAlphabet() {
        val sqids = Sqids(alphabet = "abc")
        val numbers = listOf(1L, 2L, 3L)
        assertEquals(sqids.decode(sqids.encode(numbers)), numbers)
    }

    @Test
    fun longAlphabet() {
        val sqids = Sqids(alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#\$%^&*()-_+|{}[];:\\'\"/?.>,<`~")
        val numbers = listOf(1L, 2L, 3L)
        assertEquals(sqids.decode(sqids.encode(numbers)), numbers)
    }

    @Test
    fun multibyteCharacters() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Sqids(alphabet = "ë1092")
        }
        assertEquals("Alphabet cannot contain multibyte characters", exception.message)
    }

    @Test
    fun repeatingAlphabetCharacters() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Sqids(alphabet = "aabcdefg")
        }
        assertEquals("Alphabet must contain unique characters", exception.message)
    }

    @Test
    fun tooShortOfAnAlphabet() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Sqids("ab")
        }
        assertEquals("Alphabet length must be at least 3", exception.message)
    }
}