import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FunSpec
import org.sqids.Sqids

class AlphabetTests :
    FunSpec({
        test("simple") {
            Sqids(alphabet = "0123456789abcdef")
                .shouldTranscodeBetweenValues(listOf(1, 2, 3), "489158")
        }
        test("short alphabet") { Sqids(alphabet = "abc").shouldHaveDecodeThatIsInverseOfEncode() }
        test("long alphabet") {
            Sqids(
                    alphabet =
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#\$%^&*()-_+|{}[];:\\'\"/?.>,<`~"
                )
                .shouldHaveDecodeThatIsInverseOfEncode()
        }
        test("multibyte characters") {
            shouldThrowWithMessage<IllegalArgumentException>(
                "Alphabet cannot contain multibyte characters"
            ) {
                Sqids(alphabet = "ë1092")
            }
        }
        test("repeating alphabet characters") {
            shouldThrowWithMessage<IllegalArgumentException>(
                "Alphabet must contain unique characters"
            ) {
                Sqids(alphabet = "aabcdefg")
            }
        }
        test("too short of an alphabet") {
            shouldThrowWithMessage<IllegalArgumentException>("Alphabet length must be at least 3") {
                Sqids(alphabet = "ab")
            }
        }
    })
