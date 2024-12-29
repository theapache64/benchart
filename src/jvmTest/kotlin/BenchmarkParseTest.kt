import core.BenchmarkResult
import core.BenchmarkResult.Companion.FOCUS_GROUP_ALL
import core.BlockRow
import core.InputType
import core.InvalidBenchmarkDataException
import core.ResultContainer
import model.FormData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class BenchmarkParseTest {

    @Test
    fun parseCustomMetric() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                # before
                someCustomMetric                 min      0.0,   median      0.0,   max      4.9
                anotherCustomMetric             P50       4.0,   P90       9.1,   P95      14.6,   P99      43.2

                # after
                someCustomMetric                     min    210.0,   median    217.5,   max    272.0
                anotherCustomMetric             P50       4.1,   P90       9.7,   P95      15.0,   P99      45.0

            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "before",
                testName = null,
                blockRows = mutableListOf(
                    BlockRow(
                        title = "someCustomMetric",
                        fullData = mapOf(
                            "min" to listOf(0.0f),
                            "median" to listOf(0.0f),
                            "max" to listOf(4.9f),
                        )
                    ),

                    BlockRow(
                        title = "anotherCustomMetric",
                        fullData = mapOf(
                            "P50" to listOf(4.0f),
                            "P90" to listOf(9.1f),
                            "P95" to listOf(14.6f),
                            "P99" to listOf(43.2f),
                        ),
                    ),
                ),
            ),

            BenchmarkResult(
                title = "after",
                testName = null,
                blockRows = mutableListOf(

                    BlockRow(
                        title = "someCustomMetric",
                        fullData = mapOf(
                            "min" to listOf(210.0f),
                            "median" to listOf(217.5f),
                            "max" to listOf(272.0f),
                        )
                    ),

                    BlockRow(
                        title = "anotherCustomMetric",
                        fullData = mapOf(
                            "P50" to listOf(4.1f),
                            "P90" to listOf(9.7f),
                            "P95" to listOf(15.0f),
                            "P99" to listOf(45.0f),
                        ),
                    ),


                    ),
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())
        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }


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
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.5f),
                            "P90" to listOf(20.8f),
                            "P95" to listOf(25.4f),
                            "P99" to listOf(47.4f),
                        )
                    ),
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-5.9f),
                            "P90" to listOf(7.0f),
                            "P95" to listOf(20.1f),
                            "P99" to listOf(64.4f),
                        ),
                    )
                ),
            ),

            BenchmarkResult(
                title = "Before 2",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.4f),
                            "P90" to listOf(20.7f),
                            "P95" to listOf(24.4f),
                            "P99" to listOf(51.2f),
                        )
                    ),
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-6.5f),
                            "P90" to listOf(5.4f),
                            "P95" to listOf(15.0f),
                            "P99" to listOf(60.3f),
                        ),
                    )
                ),
            ),

            BenchmarkResult(
                title = "After 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.6f),
                            "P90" to listOf(21.8f),
                            "P95" to listOf(27.5f),
                            "P99" to listOf(49.4f),
                        )
                    ),
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-6.2f),
                            "P90" to listOf(7.3f),
                            "P95" to listOf(19.5f),
                            "P99" to listOf(61.7f),
                        ),
                    )
                ),
            ),

            BenchmarkResult(
                title = "After 2",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.8f),
                            "P90" to listOf(21.9f),
                            "P95" to listOf(27.3f),
                            "P99" to listOf(53.4f),
                        )
                    ),
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-5.7f),
                            "P90" to listOf(7.4f),
                            "P95" to listOf(22.4f),
                            "P99" to listOf(63.2f),
                        ),
                    )
                ),
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())

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
  
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.5f),
                            "P90" to listOf(20.8f),
                            "P95" to listOf(25.4f),
                            "P99" to listOf(47.4f),
                        )
                    ),
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-5.9f),
                            "P90" to listOf(7.0f),
                            "P95" to listOf(20.1f),
                            "P99" to listOf(64.4f),
                        ),
                    )
                ),
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())

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
  
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.5f),
                            "P90" to listOf(20.8f),
                            "P95" to listOf(25.4f),
                            "P99" to listOf(47.4f),
                        )
                    ),
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-5.9f),
                            "P90" to listOf(7.0f),
                            "P95" to listOf(20.1f),
                            "P99" to listOf(64.4f),
                        ),
                    )
                ),
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())

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
  
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.5f),
                            "P90" to listOf(20.8f),
                            "P95" to listOf(25.4f),
                            "P99" to listOf(47.4f),
                        ),
                    ),
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-5.9f),
                            "P90" to listOf(7.0f),
                            "P95" to listOf(20.1f),
                            "P99" to listOf(64.4f),
                        ),
                    )
                )
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())

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
  
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-5.9f),
                            "P90" to listOf(7.0f),
                            "P95" to listOf(20.1f),
                            "P99" to listOf(64.4f),
                        ),
                    ),
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.5f),
                            "P90" to listOf(20.8f),
                            "P95" to listOf(25.4f),
                            "P99" to listOf(47.4f),
                        )
                    ),
                ),
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())

        assertEquals(expectedBenchmarkResult, actualBenchmarkResult)
    }

    @Test
    fun testNameParseSuccess() {
        val actualBenchmarkResult = BenchmarkResult.parse(
            """
                ### Before 1
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4 
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = null,
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs",
                        fullData = mapOf(
                            "P50" to listOf(13.5f),
                            "P90" to listOf(20.8f),
                            "P95" to listOf(25.4f),
                            "P99" to listOf(47.4f),
                        )
                    ),
                    BlockRow(
                        title = "frameOverrunMs",
                        fullData = mapOf(
                            "P50" to listOf(-5.9f),
                            "P90" to listOf(7.0f),
                            "P95" to listOf(20.1f),
                            "P99" to listOf(64.4f),
                        ),
                    )
                )
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())

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
                
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs", fullData = mapOf(
                            "P50" to listOf(13.5f),
                            "P90" to listOf(20.8f),
                            "P95" to listOf(25.4f),
                            "P99" to listOf(47.4f),
                        )
                    ), BlockRow(
                        title = "frameOverrunMs", fullData = mapOf(
                            "P50" to listOf(-5.9f),
                            "P90" to listOf(7.0f),
                            "P95" to listOf(20.1f),
                            "P99" to listOf(64.4f),
                        )
                    )
                )
            ),

            BenchmarkResult(
                title = "Before 2",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs", fullData = mapOf(
                            "P50" to listOf(13.4f),
                            "P90" to listOf(20.7f),
                            "P95" to listOf(24.4f),
                            "P99" to listOf(51.2f),
                        )
                    ), BlockRow(
                        title = "frameOverrunMs", fullData = mapOf(
                            "P50" to listOf(-6.5f),
                            "P90" to listOf(5.4f),
                            "P95" to listOf(15.0f),
                            "P99" to listOf(60.3f),
                        )
                    )
                )
            ),

            BenchmarkResult(
                title = "After 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs", fullData = mapOf(
                            "P50" to listOf(13.6f),
                            "P90" to listOf(21.8f),
                            "P95" to listOf(27.5f),
                            "P99" to listOf(49.4f),
                        )
                    ), BlockRow(
                        title = "frameOverrunMs", fullData = mapOf(
                            "P50" to listOf(-6.2f),
                            "P90" to listOf(7.3f),
                            "P95" to listOf(19.5f),
                            "P99" to listOf(61.7f),
                        )
                    )
                )
            ),

            BenchmarkResult(
                title = "After 2",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs", fullData = mapOf(
                            "P50" to listOf(13.8f),
                            "P90" to listOf(21.9f),
                            "P95" to listOf(27.3f),
                            "P99" to listOf(53.4f),
                        )
                    ), BlockRow(
                        title = "frameOverrunMs", fullData = mapOf(
                            "P50" to listOf(-5.7f),
                            "P90" to listOf(7.4f),
                            "P95" to listOf(22.4f),
                            "P99" to listOf(63.2f),
                        )
                    )
                )
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())

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
  
            """.trimIndent().toFormData(),
                focusGroup = FOCUS_GROUP_ALL
            )
            assertTrue(false)
        } catch (e: InvalidBenchmarkDataException) {
            assertTrue(true)
        }
    }

    @Test
    fun parseSingleGenericInput() {
        val actualResult = BenchmarkResult.parse(
            """
                # PNG + Image
                SplashContent: image took ms to render = 18

                # PNG + HsImage
                SplashContent: image took ms to render = 120.625
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "PNG Image vs PNG HsImage",
                testName = null,
                blockRows = listOf(
                    BlockRow(
                        title = "PNG Image",
                        fullData = mapOf(
                            "SplashContent image took ms to render" to listOf(18f),
                        )
                    ),
                    BlockRow(
                        title = "PNG HsImage",
                        fullData = mapOf(
                            "SplashContent image took ms to render" to listOf(120.625f)
                        )
                    )
                )
            )
        ).typify(InputType.GENERIC, setOf())

        assertEquals(expectedBenchmarkResult, actualResult)
    }

    @Test
    fun parseMultiGenericInput() {
        val actualResult = BenchmarkResult.parse(
            """
                 # first
                 x: 1
                 y: 2.5
                 z: 3
                 
                 # second
                 x: 5
                 y: 4
                 z: 3
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "first vs second",
                testName = null,
                blockRows = listOf(
                    BlockRow(
                        title = "first",
                        fullData = mapOf(
                            "x" to listOf(1f),
                            "y" to listOf(2.5f),
                            "z" to listOf(3f)
                        )
                    ),
                    BlockRow(
                        title = "second",
                        fullData = mapOf(
                            "x" to listOf(5f),
                            "y" to listOf(4f),
                            "z" to listOf(3f)
                        )
                    ),
                )
            )
        ).typify(InputType.GENERIC, setOf())

        assertEquals(expectedBenchmarkResult, actualResult)
    }

    @Test
    fun parseNumericKeys() {
        val actualResult = BenchmarkResult.parse(
            """
                # orange price
                2019 = 20
                2020 = 30
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "orange price",
                testName = null,
                blockRows = listOf(
                    BlockRow(
                        title = "orange price",
                        fullData = mapOf(
                            "2019" to listOf(20f),
                            "2020" to listOf(30f),
                        )
                    ),
                )
            )
        ).typify(InputType.GENERIC, setOf())

        assertEquals(expectedBenchmarkResult, actualResult)
    }

    @Test
    fun parseWithUnit() {
        val actualResult = BenchmarkResult.parse(
            """
                # before
                apple = 100
                orange = 150 rs

                # after
                apple = 130 rs
                orange = 110 rs
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "before vs after",
                testName = null,
                blockRows = listOf(
                    BlockRow(
                        title = "before",
                        fullData = mapOf(
                            "apple" to listOf(100f),
                            "orange" to listOf(150f),
                        )
                    ),
                    BlockRow(
                        title = "after",
                        fullData = mapOf(
                            "apple" to listOf(130f),
                            "orange" to listOf(110f),
                        )
                    ),
                )
            )
        ).typify(InputType.GENERIC, setOf())

        assertEquals(expectedBenchmarkResult, actualResult)
    }

    @Test
    fun parseAvgizerResposne() {
        val actualResult = BenchmarkResult.parse(
            """
                # before
                splash time is: = 846.3 (input count : 10)
                startup time is: = 1356 (input count : 10)
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "before",
                testName = null,
                blockRows = listOf(
                    BlockRow(
                        title = "before",
                        fullData = mapOf(
                            "splash time is" to listOf(846.3f),
                            "startup time is" to listOf(1356f),
                        )
                    ),
                )
            )
        ).typify(InputType.GENERIC, setOf())

        assertEquals(expectedBenchmarkResult, actualResult)
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
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "Before 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs", fullData = mapOf(
                            "P50" to listOf(13.5f),
                            "P90" to listOf(20.8f),
                            "P95" to listOf(25.4f),
                            "P99" to listOf(47.4f),
                        )
                    ), BlockRow(
                        title = "frameOverrunMs", fullData = mapOf(
                            "P50" to listOf(-5.9f),
                            "P90" to listOf(7.0f),
                            "P95" to listOf(20.1f),
                            "P99" to listOf(64.4f),
                        )
                    )
                )
            ),

            BenchmarkResult(
                title = "Before 2",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs", fullData = mapOf(
                            "P50" to listOf(13.4f),
                            "P90" to listOf(20.7f),
                            "P95" to listOf(24.4f),
                            "P99" to listOf(51.2f),
                        )
                    ), BlockRow(
                        title = "frameOverrunMs", fullData = mapOf(
                            "P50" to listOf(-6.5f),
                            "P90" to listOf(5.4f),
                            "P95" to listOf(15.0f),
                            "P99" to listOf(60.3f),
                        )
                    )
                )
            ),

            BenchmarkResult(
                title = "After 1",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs", fullData = mapOf(
                            "P50" to listOf(13.6f),
                            "P90" to listOf(21.8f),
                            "P95" to listOf(27.5f),
                            "P99" to listOf(49.4f),
                        )
                    ), BlockRow(
                        title = "frameOverrunMs", fullData = mapOf(
                            "P50" to listOf(-6.2f),
                            "P90" to listOf(7.3f),
                            "P95" to listOf(19.5f),
                            "P99" to listOf(61.7f),
                        )
                    )
                )
            ),

            BenchmarkResult(
                title = "After 2",
                testName = "HomeScrollBenchmark_scrollTest",
                blockRows = listOf(
                    BlockRow(
                        title = "frameDurationCpuMs", fullData = mapOf(
                            "P50" to listOf(13.8f),
                            "P90" to listOf(21.9f),
                            "P95" to listOf(27.3f),
                            "P99" to listOf(53.4f),
                        )
                    ), BlockRow(
                        title = "frameOverrunMs", fullData = mapOf(
                            "P50" to listOf(-5.7f),
                            "P90" to listOf(7.4f),
                            "P95" to listOf(22.4f),
                            "P99" to listOf(63.2f),
                        )
                    )
                )
            ),
        ).typify(InputType.MACRO_BENCHMARK, setOf())

        assertEquals(expectedBenchmarkResult, actualResult)
    }


    @Test
    fun parseAverageResponse() {
        val actualResult = BenchmarkResult.parse(
            """
                # before
                orange = 100
                orange = 200
                apple = 500
                apple = 600

                # after
                orange = 300
                orange = 400
                apple = 700
                apple = 800
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "before vs after",
                testName = null,
                blockRows = listOf(
                    BlockRow(
                        title = "before",
                        fullData = mapOf(
                            "orange" to listOf(100f, 200f),
                            "apple" to listOf(500f, 600f),
                        )
                    ),
                    BlockRow(
                        title = "after",
                        fullData = mapOf(
                            "orange" to listOf(300f, 400f),
                            "apple" to listOf(700f, 800f),
                        )
                    ),
                )
            )
        ).typify(InputType.GENERIC, setOf("orange", "apple"))

        assertEquals(expectedBenchmarkResult, actualResult)
    }

    @Test
    fun parseAverageResponseWithFocusGroup() {
        val actualResult = BenchmarkResult.parse(
            form = """
                # before
                orange = 100
                orange = 200
                orange = 60
                apple = 500
                apple = 600

                # after
                orange = 300
                orange = 400
                orange = 500
                apple = 700
                apple = 800
            """.trimIndent().toFormData(),
            focusGroup = "orange"
        )

        val expectedBenchmarkResult = listOf(
            BenchmarkResult(
                title = "orange - before vs after",
                testName = null,
                blockRows = listOf(
                    BlockRow(
                        title = "before",
                        fullData = mapOf(
                            "1st" to listOf(100f),
                            "2nd" to listOf(200f),
                            "3rd" to listOf(60f),
                        )
                    ),
                    BlockRow(
                        title = "after",
                        fullData = mapOf(
                            "1st" to listOf(300f),
                            "2nd" to listOf(400f),
                            "3rd" to listOf(500f),
                        )
                    ),
                )
            )
        ).typify(InputType.GENERIC, setOf("orange", "apple"))

        assertEquals(expectedBenchmarkResult, actualResult)
    }

    @Test
    fun checkBlockRowItemCountIntegrity() {
        try {
            BenchmarkResult.parse(
                form = """
                # before
                orange = 100
                orange = 200
                orange = 60
                apple = 500
                apple = 600

                # after
                orange = 300
                orange = 400
                orange = 500
                orange = 500
                apple = 700
                apple = 800
            """.trimIndent().toFormData(),
                focusGroup = FOCUS_GROUP_ALL
            )
            assert(false) { "Data integrity failed" }
        } catch (e: Exception) {
            assert(true)
        }
    }

    @Test
    fun focusGroupShouldBeEmptyForSingleRowItems() {

        val resultContainer = BenchmarkResult.parse(
            form = """
                # before
                orange = 100
                apple = 500

                # after
                orange = 300
                apple = 800
            """.trimIndent().toFormData(),
            focusGroup = FOCUS_GROUP_ALL
        )
        assertEquals(
            setOf(FOCUS_GROUP_ALL),
            resultContainer!!.focusGroups
        )
    }

}

private fun List<BenchmarkResult>.typify(type: InputType, focusGroups: Set<String>): ResultContainer {
    return ResultContainer(type, this, setOf(FOCUS_GROUP_ALL, *focusGroups.toTypedArray()))
}

private fun String.toFormData(): FormData {
    return FormData(data = this, isTestNameDetectionEnabled = true, isAutoGroupEnabled = false, isLoading = false)
}