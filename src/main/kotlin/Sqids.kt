package org.sqids

import org.sqids.blocklist as defaultBlocklist

const val DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

class Sqids private constructor(options: SqidsOptions) :
    SqidsEncodeApi by SqidsEncoder(options), SqidsDecodeApi by SqidsDecoder(options) {
    constructor(
        alphabet: String = DEFAULT_ALPHABET,
        minLength: Int = 0,
        blocklist: Set<String> = defaultBlocklist
    ) : this(SqidsOptions(alphabet, minLength, blocklist))
}
