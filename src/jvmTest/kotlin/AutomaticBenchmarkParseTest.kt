import core.BenchmarkResult
import core.InvalidBenchmarkDataException
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test


class AutoBenchmarkParseTest {

    @Test
    fun parseFullSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                ### Before 1
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                Traces: Iteration 0 1 2 3 4
                
                ### Before 2
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.4,   P90   20.7,   P95   24.4,   P99   51.2
                frameOverrunMs   P50   -6.5,   P90    5.4,   P95   15.0,   P99   60.3
                Traces: Iteration 0 1 2 3 4
                
                ### After 1
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.6,   P90   21.8,   P95   27.5,   P99   49.4
                frameOverrunMs   P50   -6.2,   P90    7.3,   P95   19.5,   P99   61.7
                Traces: Iteration 0 1 2 3 4
                
                ### After 2
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.8,   P90   21.9,   P95   27.3,   P99   53.4
                frameOverrunMs   P50   -5.7,   P90    7.4,   P95   22.4,   P99   63.2
                Traces: Iteration 0 1 2 3 4
            """.trimIndent().toAutoFormData()
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.5f,
                    "P90" to 20.8f,
                    "P95" to 25.4f,
                    "P99" to 47.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.9f,
                    "P90" to 7.0f,
                    "P95" to 20.1f,
                    "P99" to 64.4f,
                ),
            ),

            BenchmarkResult(
                title = "Before 2",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.4f,
                    "P90" to 20.7f,
                    "P95" to 24.4f,
                    "P99" to 51.2f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -6.5f,
                    "P90" to 5.4f,
                    "P95" to 15.0f,
                    "P99" to 60.3f,
                ),
            ),

            BenchmarkResult(
                title = "After 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.6f,
                    "P90" to 21.8f,
                    "P95" to 27.5f,
                    "P99" to 49.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -6.2f,
                    "P90" to 7.3f,
                    "P95" to 19.5f,
                    "P99" to 61.7f,
                ),
            ),

            BenchmarkResult(
                title = "After 2",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.8f,
                    "P90" to 21.9f,
                    "P95" to 27.3f,
                    "P99" to 53.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.7f,
                    "P90" to 7.4f,
                    "P95" to 22.4f,
                    "P99" to 63.2f,
                ),
            ),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun parseOneSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                ### Before 1
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                Traces: Iteration 0 1 2 3 4
  
            """.trimIndent().toAutoFormData()
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.5f,
                    "P90" to 20.8f,
                    "P95" to 25.4f,
                    "P99" to 47.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.9f,
                    "P90" to 7.0f,
                    "P95" to 20.1f,
                    "P99" to 64.4f,
                ),
            ),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun parseDirtySuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                ### Before 1
                HomeScrollBenchmark_scrollTest
                this is some weird shit
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                this is also some weird shit
                Traces: Iteration 0 1 2 3 4
  
            """.trimIndent().toAutoFormData()
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.5f,
                    "P90" to 20.8f,
                    "P95" to 25.4f,
                    "P99" to 47.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.9f,
                    "P90" to 7.0f,
                    "P95" to 20.1f,
                    "P99" to 64.4f,
                ),
            ),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun parseSuperDirtySuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                ### Before 1
                HomeScrollBenchmark_scrollTest
                this is some weird shit
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                SOME MAJOR SHIT
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                this is also some weird shit
                Traces: Iteration 0 1 2 3 4
  
            """.trimIndent().toAutoFormData()
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.5f,
                    "P90" to 20.8f,
                    "P95" to 25.4f,
                    "P99" to 47.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.9f,
                    "P90" to 7.0f,
                    "P95" to 20.1f,
                    "P99" to 64.4f,
                ),
            ),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }


    @Test
    fun parseSuperDirtyWithoutOrderSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                ### Before 1
                HomeScrollBenchmark_scrollTest
                this is some weird shit
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                SOME MAJOR SHIT
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                this is also some weird shit
                Traces: Iteration 0 1 2 3 4
  
            """.trimIndent().toAutoFormData()
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.5f,
                    "P90" to 20.8f,
                    "P95" to 25.4f,
                    "P99" to 47.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.9f,
                    "P90" to 7.0f,
                    "P95" to 20.1f,
                    "P99" to 64.4f,
                ),
            ),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun testNameParseSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                ### Before 1
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4 
            """.trimIndent().toAutoFormData()
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = null,
                frameDurationMs = mapOf(
                    "P50" to 13.5f,
                    "P90" to 20.8f,
                    "P95" to 25.4f,
                    "P99" to 47.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.9f,
                    "P90" to 7.0f,
                    "P95" to 20.1f,
                    "P99" to 64.4f,
                ),
            ),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }



    @Test
    fun parseSuperDirtyFullSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                ### Before 1
                HomeScrollBenchmark_scrollTest
                weird-shit
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                weird-shit
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                Traces: Iteration 0 1 2 3 4
                
                ### Before 2
                weird-shit
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.4,   P90   20.7,   P95   24.4,   P99   51.2
                frameOverrunMs   P50   -6.5,   P90    5.4,   P95   15.0,   P99   60.3
                weird-shit2
                Traces: Iteration 0 1 2 3 4
                
                ### After 1
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.6,   P90   21.8,   P95   27.5,   P99   49.4
                frameOverrunMs   P50   -6.2,   P90    7.3,   P95   19.5,   P99   61.7
                Traces: Iteration 0 1 2 3 4
                
                ### After 2
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.8,   P90   21.9,   P95   27.3,   P99   53.4
                frameOverrunMs   P50   -5.7,   P90    7.4,   P95   22.4,   P99   63.2
                Traces: Iteration 0 1 2 3 4
                
            """.trimIndent().toAutoFormData()
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.5f,
                    "P90" to 20.8f,
                    "P95" to 25.4f,
                    "P99" to 47.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.9f,
                    "P90" to 7.0f,
                    "P95" to 20.1f,
                    "P99" to 64.4f,
                ),
            ),

            BenchmarkResult(
                title = "Before 2",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.4f,
                    "P90" to 20.7f,
                    "P95" to 24.4f,
                    "P99" to 51.2f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -6.5f,
                    "P90" to 5.4f,
                    "P95" to 15.0f,
                    "P99" to 60.3f,
                ),
            ),

            BenchmarkResult(
                title = "After 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.6f,
                    "P90" to 21.8f,
                    "P95" to 27.5f,
                    "P99" to 49.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -6.2f,
                    "P90" to 7.3f,
                    "P95" to 19.5f,
                    "P99" to 61.7f,
                ),
            ),

            BenchmarkResult(
                title = "After 2",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.8f,
                    "P90" to 21.9f,
                    "P95" to 27.3f,
                    "P99" to 53.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.7f,
                    "P90" to 7.4f,
                    "P95" to 22.4f,
                    "P99" to 63.2f,
                ),
            ),
        )

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun parseDuplicateMetricsFailure() {
        try {
            BenchmarkResult.parse(
                """
                ### Before 1
                HomeScrollBenchmark_scrollTest
                this is some weird shit
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                SOME MAJOR SHIT
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                this is also some weird shit
                Traces: Iteration 0 1 2 3 4
                
                ### Before 1
                HomeScrollBenchmark_scrollTest
                this is some weird shit
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                SOME MAJOR SHIT
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                this is also some weird shit
                Traces: Iteration 0 1 2 3 4
  
            """.trimIndent().toAutoFormData()
            )
            assertTrue(false)
        } catch (e: InvalidBenchmarkDataException) {
            assertTrue(true)
        }
    }

    @Test
    fun parseTitleTestSuccess() {
        val actualResult = BenchmarkResult.parse(
            """
                - Before 1
                # first line will be treated as title of the block
                  special chars will be stripped from the title
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                Traces: Iteration 0 1 2 3 4
                
                ## Before 2
                # line breaks are used to separate the block
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.4,   P90   20.7,   P95   24.4,   P99   51.2
                frameOverrunMs   P50   -6.5,   P90    5.4,   P95   15.0,   P99   60.3
                Traces: Iteration 0 1 2 3 4
                
                After 1
                you can include whatever text you want anywhere you want
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.6,   P90   21.8,   P95   27.5,   P99   49.4
                the order doesn't matter
                frameOverrunMs   P50   -6.2,   P90    7.3,   P95   19.5,   P99   61.7
                Traces: Iteration 0 1 2 3 4
                
                > After 2
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.8,   P90   21.9,   P95   27.3,   P99   53.4
                see.. am some random text
                frameOverrunMs   P50   -5.7,   P90    7.4,   P95   22.4,   P99   63.2
                Traces: Iteration 0 1 2 3 4
            """.trimIndent().toAutoFormData()
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.5f,
                    "P90" to 20.8f,
                    "P95" to 25.4f,
                    "P99" to 47.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.9f,
                    "P90" to 7.0f,
                    "P95" to 20.1f,
                    "P99" to 64.4f,
                ),
            ),

            BenchmarkResult(
                title = "Before 2",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.4f,
                    "P90" to 20.7f,
                    "P95" to 24.4f,
                    "P99" to 51.2f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -6.5f,
                    "P90" to 5.4f,
                    "P95" to 15.0f,
                    "P99" to 60.3f,
                ),
            ),

            BenchmarkResult(
                title = "After 1",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.6f,
                    "P90" to 21.8f,
                    "P95" to 27.5f,
                    "P99" to 49.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -6.2f,
                    "P90" to 7.3f,
                    "P95" to 19.5f,
                    "P99" to 61.7f,
                ),
            ),

            BenchmarkResult(
                title = "After 2",
                testName = "HomeScrollBenchmark_scrollTest",
                frameDurationMs = mapOf(
                    "P50" to 13.8f,
                    "P90" to 21.9f,
                    "P95" to 27.3f,
                    "P99" to 53.4f,
                ),
                frameOverrunMs = mapOf(
                    "P50" to -5.7f,
                    "P90" to 7.4f,
                    "P95" to 22.4f,
                    "P99" to 63.2f,
                ),
            ),
        )

        assertEquals(expectedBenchmarkResult, actualResult)
    }


}

private fun String.toAutoFormData(): FormData {
    return FormData(data = this)
}