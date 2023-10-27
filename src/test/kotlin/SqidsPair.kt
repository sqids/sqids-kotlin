import io.kotest.datatest.IsStableType

@IsStableType
data class SqidsPair(val input: List<Long>, val output: String) {
    override fun toString(): String = "$output [${input.joinToString(", ")}]"
}
