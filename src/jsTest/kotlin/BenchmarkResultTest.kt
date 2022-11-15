import core.BenchmarkResult
import core.InvalidBenchmarkDataException
import core.InvalidFrameDurationNodeException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class BenchmarkResultTest {

    @Test
    fun parseFullSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
                frameOverrunMs   P50   -5.7,   P90    8.6,   P95   23.0,   P99   64.7
                Traces: Iteration 0 1 2 3 4
            """.trimIndent()
        )

        val expectedBenchmarkResult = BenchmarkResult(
            title = "HomeScrollBenchmark_scrollTest",
            frameDurationMs = mapOf(
                "P50" to 14.0f,
                "P90" to 22.8f,
                "P95" to 28.5f,
                "P99" to 50.8f,
            ),
            frameOverrunMs = mapOf(
                "P50" to -5.7f,
                "P90" to 8.6f,
                "P95" to 23.0f,
                "P99" to 64.7f,
            ),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun parsePartialSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
                Traces: Iteration 0 1 2 3 4
            """.trimIndent()
        )

        val expectedBenchmarkResult = BenchmarkResult(
            title = "HomeScrollBenchmark_scrollTest",
            frameDurationMs = mapOf(
                "P50" to 14.0f,
                "P90" to 22.8f,
                "P95" to 28.5f,
                "P99" to 50.8f,
            ),
            frameOverrunMs = null,
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun parseWithMissingHeaderError() {
        try {
            BenchmarkResult.parse(
                """
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
                frameOverrunMs   P50   -5.7,   P90    8.6,   P95   23.0,   P99   64.7
                Traces: Iteration 0 1 2 3 4
            """.trimIndent()
            )
            error("Expected to fail")
        } catch (e: InvalidBenchmarkDataException) {
            assertTrue(true)
        }
    }

    @Test
    fun parseWithMissingFrameDurationCpuMsError() {
        try {
            BenchmarkResult.parse(
                """
                HomeScrollBenchmark_scrollTest
                frameOverrunMs   P50   -5.7,   P90    8.6,   P95   23.0,   P99   64.7
                Traces: Iteration 0 1 2 3 4
            """.trimIndent()
            )
            error("Expected to fail")
        } catch (e: InvalidFrameDurationNodeException) {
            assertTrue(true)
        }
    }

    @Test
    fun parseWithMissingTracesLineError() {
        try {
            BenchmarkResult.parse(
                """
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
                frameOverrunMs   P50   -5.7,   P90    8.6,   P95   23.0,   P99   64.7
            """.trimIndent()
            )
            error("Expected to fail")
        } catch (e: InvalidBenchmarkDataException) {
            assertTrue(true)
        }
    }

}