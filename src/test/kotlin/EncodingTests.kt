import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import org.sqids.Sqids

class EncodingTests :
    FunSpec({
        test("simple") { Sqids().shouldTranscodeBetweenValues(listOf(1, 2, 3), "86Rf07") }
        test("different inputs") {
            Sqids()
                .shouldHaveDecodeThatIsInverseOfEncode(
                    listOf(0, 0, 0, 1, 2, 3, 100, 1_000, 100_000, 1_000_000, Long.MAX_VALUE)
                )
        }
        context("incremental numbers") {
            Sqids().run {
                withData(
                    SqidsPair(output = "bM", input = listOf(0)),
                    SqidsPair(output = "Uk", input = listOf(1)),
                    SqidsPair(output = "gb", input = listOf(2)),
                    SqidsPair(output = "Ef", input = listOf(3)),
                    SqidsPair(output = "Vq", input = listOf(4)),
                    SqidsPair(output = "uw", input = listOf(5)),
                    SqidsPair(output = "OI", input = listOf(6)),
                    SqidsPair(output = "AX", input = listOf(7)),
                    SqidsPair(output = "p6", input = listOf(8)),
                    SqidsPair(output = "nJ", input = listOf(9))
                ) { (input, output) ->
                    shouldTranscodeBetweenValues(input, output)
                }
            }
        }

        context("incremental numbers, same index 0") {
            Sqids().run {
                withData(
                    SqidsPair(output = "SvIz", input = listOf(0, 0)),
                    SqidsPair(output = "n3qa", input = listOf(0, 1)),
                    SqidsPair(output = "tryF", input = listOf(0, 2)),
                    SqidsPair(output = "eg6q", input = listOf(0, 3)),
                    SqidsPair(output = "rSCF", input = listOf(0, 4)),
                    SqidsPair(output = "sR8x", input = listOf(0, 5)),
                    SqidsPair(output = "uY2M", input = listOf(0, 6)),
                    SqidsPair(output = "74dI", input = listOf(0, 7)),
                    SqidsPair(output = "30WX", input = listOf(0, 8)),
                    SqidsPair(output = "moxr", input = listOf(0, 9))
                ) { (input, output) ->
                    shouldTranscodeBetweenValues(input, output)
                }
            }
        }

        context("incremental numbers, same index 1") {
            Sqids().run {
                withData(
                    SqidsPair(output = "SvIz", input = listOf(0, 0)),
                    SqidsPair(output = "nWqP", input = listOf(1, 0)),
                    SqidsPair(output = "tSyw", input = listOf(2, 0)),
                    SqidsPair(output = "eX68", input = listOf(3, 0)),
                    SqidsPair(output = "rxCY", input = listOf(4, 0)),
                    SqidsPair(output = "sV8a", input = listOf(5, 0)),
                    SqidsPair(output = "uf2K", input = listOf(6, 0)),
                    SqidsPair(output = "7Cdk", input = listOf(7, 0)),
                    SqidsPair(output = "3aWP", input = listOf(8, 0)),
                    SqidsPair(output = "m2xn", input = listOf(9, 0))
                ) { (input, output) ->
                    shouldTranscodeBetweenValues(input, output)
                }
            }
        }
        test("multi input") {
            Sqids().shouldHaveDecodeThatIsInverseOfEncode((0L until 100L).toList())
        }
        test("encoding no numbers") { Sqids().encode(emptyList()) shouldBe "" }
        test("decoding empty string") { Sqids().decode("") shouldBe emptyList() }
        test("decoding an ID with an invalid character") {
            Sqids().decode("*") shouldBe emptyList()
        }
        test("encode out-of-range numbers") {
            val encodingError = "Encoding supports numbers between 0 and ${Long.MAX_VALUE}"
            Sqids().run {
                shouldThrowWithMessage<RuntimeException>(encodingError) { encode(-1) }
                shouldThrowWithMessage<RuntimeException>(encodingError) {
                    @Suppress("INTEGER_OVERFLOW") encode(Long.MAX_VALUE + 1)
                }
            }
        }
    })
