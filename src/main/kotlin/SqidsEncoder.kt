package org.sqids

import org.sqids.alphabet.Alphabet
import org.sqids.alphabet.mutators.joinValuesTo
import org.sqids.alphabet.mutators.padTo

private val DIGIT_REGEX = Regex("""/\d/""")

internal class SqidsEncoder(private val options: SqidsOptions) : SqidsEncodeApi {
    private val alphabet: String by options::alphabet
    private val blocklist: Set<String> by options::blocklist
    private val minLength: Int by options::minLength

    override fun encode(vararg numbers: Long): String = encode(numbers.asIterable())

    override fun encode(numbers: Iterable<Long>): String =
        EncoderInput(numbers).let { input -> if (input.isEmpty()) "" else encodeNumbers(input) }

    private fun encodeNumbers(numbers: EncoderInput, increment: Int = 0): String =
        initAlphabet(numbers, increment).let { alphabet ->
            buildString {
                    append(alphabet.prefix)
                    alphabet.joinValuesTo(this, numbers) { it.toId(alphabet.drop(1)) }
                    alphabet.padTo(this, minLength)
                }
                .let { id -> if (isBlockedId(id)) encodeNumbers(numbers, increment + 1) else id }
        }

    private fun initAlphabet(numbers: EncoderInput, increment: Int): Alphabet =
        if (increment > alphabet.length)
            throw RuntimeException("Reached max attempts to re-generate the ID")
        else
            Alphabet(alphabet).apply {
                val offset = (numbers.toSeed() + increment) % alphabet.length
                pivotOn(offset)
                reverse()
            }

    private fun EncoderInput.toSeed(): Int =
        (foldIndexed(size.toLong()) { index, acc, value ->
                acc + alphabet[(value % alphabet.length).toInt()].code + index
            } % alphabet.length)
            .toInt()

    private fun Long.toId(chars: CharSequence): String = buildString {
        var result = this@toId
        do {
            insert(0, chars[(result % chars.length).toInt()])
            result /= chars.length
        } while (result > 0)
    }

    private fun isBlockedId(id: String): Boolean =
        id.lowercase().let {
            blocklist.any { word ->
                word.length <= it.length &&
                    when {
                        it.length <= 3 || word.length <= 3 -> it == word
                        DIGIT_REGEX.matches(word) -> it.startsWith(word) || it.endsWith(word)
                        else -> it.contains(word)
                    }
            }
        }

    @Suppress("DataClassPrivateConstructor")
    private data class EncoderInput private constructor(val numbers: List<Long>) :
        Collection<Long> by numbers {

        constructor(numbers: Iterable<Long>) : this(numbers.toList())

        init {
            if (numbers.any { it !in 0..Long.MAX_VALUE }) {
                throw RuntimeException("Encoding supports numbers between 0 and ${Long.MAX_VALUE}")
            }
        }
    }
}
