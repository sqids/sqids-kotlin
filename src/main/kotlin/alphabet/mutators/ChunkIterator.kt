package org.sqids.alphabet.mutators

import org.sqids.alphabet.Alphabet

internal class ChunkIterator(
    private val peekingIterator: PeekingIterator<Char>,
    private val alphabet: Alphabet
) : Iterator<String> {
    private var generation: Int = 0
    private val separator: Char
        get() = alphabet.separatorChar

    override fun hasNext(): Boolean {
        if (generation++ > 0) alphabet.shuffle()
        return peekingIterator.hasNext() && peekingIterator.peek() != separator
    }

    override fun next(): String = buildString { appendUntilSeparator(peekingIterator, separator) }

    private fun StringBuilder.appendUntilSeparator(
        iter: Iterator<Char>,
        separator: Char
    ): StringBuilder = apply {
        iter
            .next()
            .takeUnless { it == separator }
            ?.also {
                append(it)
                if (iter.hasNext()) appendUntilSeparator(iter, separator)
            }
    }
}
