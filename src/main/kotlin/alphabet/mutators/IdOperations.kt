package org.sqids.alphabet.mutators

import org.sqids.alphabet.Alphabet

fun <T, A : Appendable> Alphabet.joinValuesTo(
    buffer: A,
    values: Iterable<T>,
    transform: (T) -> CharSequence
): A =
    buffer.apply {
        val iter = values.iterator()
        while (iter.hasNext()) {
            append(transform(iter.next()))
            if (iter.hasNext()) {
                append(separator)
                shuffle()
            }
        }
    }

fun <A> Alphabet.padTo(buffer: A, length: Int): A where A : Appendable, A : CharSequence =
    if (length <= buffer.length) buffer
    else
        buffer.apply {
            append(separator)
            while (length - buffer.length > 0) {
                val charsToTake = kotlin.math.min(length - buffer.length, this@padTo.length)
                append(shuffle().take(charsToTake))
            }
        }

fun <T> Alphabet.splitToChunks(chars: CharSequence, transform: Alphabet.(String) -> T): List<T> {
    val chunkIterator = ChunkIterator(chars.peekingIterator(), this)
    val result = mutableListOf<T>()
    for (chunk in chunkIterator) {
        result.add(transform(chunk))
    }
    return result
}
