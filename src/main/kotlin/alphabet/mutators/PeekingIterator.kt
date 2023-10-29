package org.sqids.alphabet.mutators

class PeekingIterator<T>(private val iterator: Iterator<T>) : Iterator<T> {
    private var nextValue: T? = consumeNext()

    fun peek(): T? = nextValue

    override fun hasNext(): Boolean = nextValue != null

    override fun next(): T = nextValue!!.also { nextValue = consumeNext() }

    private fun consumeNext(): T? = if (iterator.hasNext()) iterator.next() else null
}

fun CharSequence.peekingIterator(): PeekingIterator<Char> = PeekingIterator(iterator())
