package org.sqids

const val MIN_ALPHABET_LENGTH = 3
const val MIN_LENGTH_LIMIT = 255
const val MIN_BLOCK_LIST_WORD_LENGTH = 3

internal class SqidsOptions(alphabet: String, val minLength: Int, blocklist: Set<String>) {
    init {
        when {
            alphabet.length < MIN_ALPHABET_LENGTH ->
                throw IllegalArgumentException(
                    "Alphabet length must be at least $MIN_ALPHABET_LENGTH"
                )
            alphabet.length != alphabet.toByteArray().size ->
                throw IllegalArgumentException("Alphabet cannot contain multibyte characters")
            alphabet.toSet().size != alphabet.length ->
                throw IllegalArgumentException("Alphabet must contain unique characters")
            minLength !in 0..MIN_LENGTH_LIMIT ->
                throw IllegalArgumentException(
                    "Minimum length has to be between 0 and $MIN_LENGTH_LIMIT"
                )
        }
    }

    val alphabet: String = Alphabet(alphabet).shuffle().toString()
    val blocklist: Set<String> =
        alphabet.lowercase().toSet().let { alphabetChars ->
            blocklist.mapNotNullTo(mutableSetOf()) { word ->
                word
                    .takeIf { it.length >= MIN_BLOCK_LIST_WORD_LENGTH }
                    ?.lowercase()
                    ?.takeIf { it.all { c -> c in alphabetChars } }
            }
        }
}
