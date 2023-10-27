package org.sqids

interface SqidsEncodeApi {
    fun encode(vararg numbers: Long): String

    fun encode(numbers: Iterable<Long>): String
}

interface SqidsDecodeApi {
    fun decode(id: String): List<Long>
}
