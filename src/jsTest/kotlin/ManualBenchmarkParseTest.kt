import core.BenchmarkResult
import core.InvalidBenchmarkDataException
import core.InvalidFrameDurationNodeException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ManualBenchmarkParseTest {

    @Test
    fun parseFullSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
                frameOverrunMs   P50   -5.7,   P90    8.6,   P95   23.0,   P99   64.7
                Traces: Iteration 0 1 2 3 4
            """.trimIndent().toFormData()
        )

        val expectedBenchmarkResult = BenchmarkResult(
            title = TEMP_FORM_DATA_TITLE,
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
            """.trimIndent().toFormData()
        )

        val expectedBenchmarkResult = BenchmarkResult(
            title = TEMP_FORM_DATA_TITLE,
            frameDurationMs = mapOf(
                "P50" to 14.0f,
                "P90" to 22.8f,
                "P95" to 28.5f,
                "P99" to 50.8f,
            ),
            frameOverrunMs = emptyMap(),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun parseWithMissingHeaderSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
                frameOverrunMs   P50   -5.7,   P90    8.6,   P95   23.0,   P99   64.7
                Traces: Iteration 0 1 2 3 4
            """.trimIndent().toFormData()
        )

        val expectedBenchmarkResult = BenchmarkResult(
            title = TEMP_FORM_DATA_TITLE,
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
    fun parseWithMissingFrameDurationCpuMsError() {
        try {
            BenchmarkResult.parse(
                """
                HomeScrollBenchmark_scrollTest
                frameOverrunMs   P50   -5.7,   P90    8.6,   P95   23.0,   P99   64.7
                Traces: Iteration 0 1 2 3 4
            """.trimIndent().toFormData()
            )
            assertTrue(false)
        }catch (e  : InvalidBenchmarkDataException){
            assertTrue(true)
        }
    }

    @Test
    fun parseWithMissingTracesLineSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
                frameOverrunMs   P50   -5.7,   P90    8.6,   P95   23.0,   P99   64.7
            """.trimIndent().toFormData()
        )

        val expectedBenchmarkResult = BenchmarkResult(
            title = TEMP_FORM_DATA_TITLE,
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
    fun parseMinimalSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
            """.trimIndent().toFormData()
        )

        val expectedBenchmarkResult = BenchmarkResult(
            title = TEMP_FORM_DATA_TITLE,
            frameDurationMs = mapOf(
                "P50" to 14.0f,
                "P90" to 22.8f,
                "P95" to 28.5f,
                "P99" to 50.8f,
            ),
            frameOverrunMs = emptyMap(),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)

    }

    @Test
    fun parseMultipleError() {
        try {
            BenchmarkResult.parse(
                """
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.8
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.6
                frameDurationCpuMs   P50   14.0,   P90   22.8,   P95   28.5,   P99   50.7
            """.trimIndent().toFormData()
            )
            assertTrue(false)
        }catch (e : InvalidBenchmarkDataException){
            assertTrue(true)
        }
    }
}

private const val TEMP_FORM_DATA_TITLE = "myFormData"
private fun String.toFormData(): ManualFormData {
    return ManualFormData(
        title = TEMP_FORM_DATA_TITLE,
        data = this
    )
}
