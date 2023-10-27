import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import org.sqids.Sqids

class BlocklistTests :
    FunSpec({
        test("if no custom blocklist param, use the default blocklist") {
            val blockedInput = listOf(4572721L)
            Sqids().run {
                decode("aho1e") shouldBe blockedInput
                encode(blockedInput) shouldBe "JExTR"
            }
        }
        test("if an empty blocklist param passed, don't use any blocklist") {
            Sqids(blocklist = emptySet()).shouldTranscodeBetweenValues(listOf(4572721), "aho1e")
        }
        test("if a non-empty blocklist param passed, use only that") {
            Sqids(blocklist = setOf("ArUO")).run {
                shouldTranscodeBetweenValues(listOf(4572721), "aho1e")
                val blockedInput = listOf(100000L)
                assertSoftly {
                    decode("ArUO") shouldBe blockedInput
                    encode(blockedInput) shouldBe "QyG4"
                    decode("QyG4") shouldBe blockedInput
                }
            }
        }
        test("blocklist") {
            // This test blocks 5 sequential encodings in various ways, see the spec for more info
            Sqids(blocklist = setOf("JSwXFaosAN", "OCjV9JK64o", "rBHf", "79SM", "7tE6"))
                .shouldTranscodeBetweenValues(listOf(1_000_000, 2_000_000), "1aYeB7bRUt")
        }
        test("decoding blocklist words should still work") {
            val blocklist = setOf("86Rf07", "se8ojk", "ARsz1p", "Q8AI49", "5sQRZO")
            Sqids(blocklist = blocklist).run {
                assertSoftly { blocklist.forEach { decode(it) shouldBe listOf(1, 2, 3) } }
            }
        }
        test("match against a short blocklist word") {
            Sqids(blocklist = setOf("pnd")).shouldHaveDecodeThatIsInverseOfEncode(listOf(1000))
        }
        // Why is this test called this?
        test("blocklist filtering in constructor") {
            Sqids(alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ", blocklist = setOf("sxnzkl"))
                .shouldTranscodeBetweenValues(listOf(1, 2, 3), "IBSHOZ")
        }
        test("max encoding attempts") {
            val minLength = 3
            shouldThrowWithMessage<RuntimeException>("Reached max attempts to re-generate the ID") {
                Sqids(
                        alphabet = "abc" shouldHaveLength minLength,
                        minLength = minLength,
                        blocklist =
                            (setOf("abc", "bca", "cab") shouldHaveSize minLength) as Set<String>
                    )
                    .encode(0)
            }
        }
    })
