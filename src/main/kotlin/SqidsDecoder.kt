package org.sqids

import org.sqids.alphabet.Alphabet
import org.sqids.alphabet.mutators.splitToChunks

internal class SqidsDecoder(private val options: SqidsOptions) : SqidsDecodeApi {
    private val alphabet: String by options::alphabet

    override fun decode(id: String): List<Long> =
        when {
            id.isEmpty() -> emptyList()
            id.any { c -> c !in alphabet } -> emptyList()
            else -> decodeId(id)
        }

    private fun decodeId(id: String): List<Long> =
        Alphabet(alphabet)
            .apply {
                val prefix = id.first()
                val offset = indexOf(prefix)
                pivotOn(offset)
                reverse()
            }
            .splitToChunks(id.drop(1)) { chunk -> chunk.toNumber(workingChars) }

    private fun String.toNumber(chars: CharSequence): Long =
        fold(0) { acc, value -> acc * chars.length + chars.indexOf(value) }
}
