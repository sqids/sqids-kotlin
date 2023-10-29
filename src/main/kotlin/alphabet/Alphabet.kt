package org.sqids.alphabet

class Alphabet(private var chars: CharArray) : CharSequence {
    constructor(initialState: String) : this(initialState.toCharArray())

    private val prefixChar: Char
        get() = if (isReversed) last() else first()

    val prefix: String
        get() = prefixChar.toString()

    val separatorChar: Char
        get() = first()

    val separator: String
        get() = separatorChar.toString()

    val workingChars: CharSequence
        get() = drop(1)

    private var isReversed = false

    override val length: Int
        get() = chars.size

    override fun get(index: Int): Char = chars[index]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        chars.concatToString(startIndex, endIndex)

    fun pivotOn(pivot: Int): Alphabet = apply {
        chars = (chars.drop(pivot) + chars.take(pivot)).toCharArray()
    }

    fun reverse(): Alphabet = apply {
        chars.reverse()
        isReversed = !isReversed
    }

    /** This is a consistent shuffle. It always produces the same result given the same input. */
    fun shuffle(): Alphabet = apply {
        for ((i, j) in (0 until chars.size - 1).zip((1 until chars.size).reversed())) {
            val r = (i * j + chars[i].code + chars[j].code) % chars.size
            chars[i] = chars[r].also { chars[r] = chars[i] }
        }
    }

    override fun toString(): String = chars.concatToString()
}
