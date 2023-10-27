import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.sqids.Sqids

fun Sqids.shouldHaveDecodeThatIsInverseOfEncode(testInput: List<Long> = listOf(1, 2, 3)): Sqids =
    apply {
        decode(encode(testInput)) shouldBe testInput
    }

fun Sqids.shouldTranscodeBetweenValues(input: List<Long>, output: String): Sqids = apply {
    assertSoftly {
        encode(input) shouldBe output
        decode(output) shouldBe input
    }
}
