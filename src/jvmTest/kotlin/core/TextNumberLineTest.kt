package core

import org.junit.Assert.assertEquals
import org.junit.Test


class TextNumberLineTest {
    @Test
    fun simple() {
        TextNumberLine.parse("a = 10").also {
            assertEquals(TextNumberLine("a = ", 10f), it)
        }
    }

    @Test
    fun withUnit() {
        TextNumberLine.parse("a = 10 rs").also {
            assertEquals(TextNumberLine("a = ", 10f), it)
        }
    }

    @Test
    fun numeric() {
        TextNumberLine.parse("2019 = 20").also {
            assertEquals(TextNumberLine("2019 = ", 20f), it)
        }
    }

}