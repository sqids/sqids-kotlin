package org.sqids

class Alphabet(private var chars: CharArray) : CharSequence {
    constructor(initialState: String) : this(initialState.toCharArray())

    override val length: Int
        get() = chars.size

    override fun get(index: Int): Char = chars[index]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        chars.concatToString(startIndex, endIndex)

    /** This is a consistent shuffle. It always produces the same result given the same input. */
    fun shuffle(): Alphabet = apply {
        for ((i, j) in (0 until chars.size - 1).zip((1 until chars.size).reversed())) {
            val r = (i * j + chars[i].code + chars[j].code) % chars.size
            chars[i] = chars[r].also { chars[r] = chars[i] }
        }
    }

    override fun toString(): String = chars.concatToString()
}
