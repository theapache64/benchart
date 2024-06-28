package core

import model.FormData

open class InvalidDataException(message: String?) : Throwable(message)
class InvalidBenchmarkDataException(message: String?) : InvalidDataException(message)
class InvalidGenericDataException(message: String?) : InvalidDataException(message)

data class BlockRow(
    val title: String,
    val fullData: Map<String, List<Float>>
) {
    val avgData: Map<String, Float> = fullData.mapValues { it.value.average().toFloat() }
}

enum class SupportedMetrics(
    val key: String,
    val emoji: String,
    val title: String
) {
    Duration(
        emoji = "⏱",
        key = "frameDurationCpuMs",
        title = "Duration Summary"
    ),
    Overrun(
        emoji = "🏃🏻‍♂️",
        key = "frameOverrunMs",
        title = "Overrun Summary"
    ),
    InitialDisplay(
        emoji = "🌘",
        key = "timeToInitialDisplayMs",
        title = "Initial Display Summary"
    ),
    FullDisplay(
        emoji = "🌕",
        key = "timeToFullDisplayMs",
        title = "Full Display Summary"
    ),
}

enum class InputType {
    GENERIC,
    NORMAL_BENCHMARK
}

data class ResultContainer(
    val inputType: InputType,
    val benchmarkResults: List<BenchmarkResult>,
    val focusGroups: Set<String>
)

data class BenchmarkResult(
    val title: String,
    val testName: String?,
    val blockRows: List<BlockRow>
) {
    companion object {
        const val FOCUS_GROUP_ALL = "All"
        private val metricKeys = SupportedMetrics.values().map { it.key }

        private val machineLineRegEx = "^(Traces|${metricKeys.joinToString(separator = "|")}).+".toRegex()
        private val titleStripRegEx = "\\W+".toRegex()
        private val genericTitleStripRegEx = "\\W+".toRegex()
        private val testNameRegex = "[A-Z].*_[a-z].*".toRegex()

        fun parse(form: FormData, focusGroup: String): ResultContainer? {

            val blocks = form.data
                .split("\n").joinToString(separator = "\n") { it.trim() }
                .split("^\\s+".toRegex(RegexOption.MULTILINE)).map { it.trim() }
                .filter { it.isNotBlank() }

            println("parsing input...")
            if (blocks.isEmpty()) return null
            if (form.isGenericInput()) return parseGenericInput(blocks, focusGroup)

            println("parsing machine generated benchmark input...")
            val benchmarkResults = mutableListOf<BenchmarkResult>()

            for ((index, block) in blocks.withIndex()) {
                println("block: '$block'")
                val lines = block.split("\n").map { it.trim() }
                var title: String? = null
                var testName: String? = null
                val blockRows = mutableListOf<BlockRow>()
                for (line in lines) {

                    if (title == null && isHumanLine(line)) {
                        title = line
                    }

                    if (form.isTestNameDetectionEnabled && isTestName(line)) {
                        if (testName != null && blockRows.isNotEmpty()) {

                            if (title == null) {
                                title = "benchmark $index $testName"
                            }

                            // We already have an unsaved testData, so let's save it
                            benchmarkResults.add(
                                BenchmarkResult(
                                    title = title,
                                    testName = testName,
                                    blockRows = blockRows
                                )
                            )

                            blockRows.clear()
                        }

                        testName = line
                    }

                    val metricName = line.findMetricKeyOrNull()
                    println("QuickTag: BenchmarkResult:parse: metric name is $metricName")
                    if (metricName != null) {
                        val isMetricAlreadyAdded = blockRows.find { it.title == metricName } != null
                        if (isMetricAlreadyAdded) {
                            throw InvalidBenchmarkDataException("Two $metricName found in block ${index + 1}. Expected only one")
                        }

                        blockRows.add(
                            BlockRow(
                                title = metricName,
                                fullData = parseValues(metricName, line).map { (key, value) ->
                                    key to listOf(value)
                                }.toMap()
                            )
                        )
                    }
                }

                if (title == null) {
                    title = "benchmark $index"
                }

                title = parseTitle(title)

                if (blockRows.isNotEmpty()) {
                    benchmarkResults.add(
                        BenchmarkResult(
                            title = title,
                            testName = testName,
                            blockRows = blockRows
                        )
                    )
                }
            }

            return ResultContainer(InputType.NORMAL_BENCHMARK, benchmarkResults, setOf(FOCUS_GROUP_ALL))
        }

        private fun parseGenericInput(
            blocks: List<String>,
            focusGroup: String
        ): ResultContainer {
            val (focusGroups, benchmarkResults) = parseMultiLineGenericInput(blocks, focusGroup)
            return ResultContainer(
                InputType.GENERIC,
                benchmarkResults,
                focusGroups
            )
        }

        private fun createChartTitle(blockRows: MutableList<BlockRow>): String {
            return blockRows.joinToString(separator = " vs ") { it.title }
        }

        private fun parseMultiLineGenericInput(
            blocks: List<String>,
            focusGroup: String
        ): Pair<Set<String>, List<BenchmarkResult>> {
            val benchmarkResults = mutableListOf<BenchmarkResult>()
            val blockRows = mutableListOf<BlockRow>()
            val focusGroups = mutableSetOf(FOCUS_GROUP_ALL)
            for ((index, block) in blocks.withIndex()) {
                val lines = block.split("\n").map { it.trim() }
                var title: String? = null
                val valuesMap = mutableMapOf<String, MutableList<Float>>()
                for ((lineIndex, line) in lines.withIndex()) {

                    if (title == null && isHumanLine(line)) {
                        title = line
                        continue
                    }

                    if (line.shouldSkip()) {
                        continue
                    }

                    val textNumberLine = TextNumberLine.parse(lineIndex, line)
                    val genericTitle = parseGenericTitle(textNumberLine.text)
                    valuesMap.getOrPut(genericTitle) { mutableListOf() }.add(textNumberLine.number)
                }

                if (title == null) {
                    title = "benchmark $index"
                }

                title = parseGenericTitle(title)

                blockRows.add(
                    BlockRow(
                        title = title,
                        fullData = valuesMap
                    )
                )
            }

            for (blockRow in blockRows) {
                for ((key, value) in blockRow.fullData) {
                    if (value.size > 1) {
                        focusGroups.add(key)
                    }
                }
            }

            checkDataIntegrity(blockRows)

            val chartTitle = createChartTitle(blockRows)

            benchmarkResults.add(
                BenchmarkResult(
                    title = chartTitle,
                    testName = "",
                    blockRows = blockRows
                )
            )

            return if (focusGroup == FOCUS_GROUP_ALL || focusGroup !in focusGroups) {
                Pair(focusGroups, benchmarkResults)
            } else {
                Pair(focusGroups, focus(benchmarkResults, focusGroup))
            }
        }

        private fun focus(benchmarkResults: List<BenchmarkResult>, focusGroup: String): List<BenchmarkResult> {
            val newBenchmarkResult = mutableListOf<BenchmarkResult>()
            for (result in benchmarkResults) {
                val blockRows = mutableListOf<BlockRow>()
                for (blockRow in result.blockRows) {
                    blockRows.add(
                        BlockRow(
                            title = blockRow.title,
                            fullData = blockRow.fullData[focusGroup]?.mapIndexed { index, value ->
                                Pair(getPositionText(index + 1), listOf(value))
                            }?.toMap() ?: error("Invalid focus group '$focusGroup' for ${blockRow.title}")
                        )
                    )
                }
                newBenchmarkResult.add(
                    BenchmarkResult(
                        title = "$focusGroup - ${result.title}",
                        testName = result.testName,
                        blockRows = blockRows
                    )
                )
            }
            return newBenchmarkResult
        }

        private fun getPositionText(index: Int): String {
            val suffix = when {
                index % 100 in 11..13 -> "th"
                index % 10 == 1 -> "st"
                index % 10 == 2 -> "nd"
                index % 10 == 3 -> "rd"
                else -> "th"
            }
            return "$index$suffix"
        }


        private fun checkDataIntegrity(blockRows: List<BlockRow>) {
            if (blockRows.size >= 2) {
                val originalValueOrder = blockRows.first().avgData.keys.toList()
                for ((index, blockRow) in blockRows.withIndex()) {
                    if (index == 0) {
                        continue
                    }
                    val currentValueOrder = blockRow.avgData.keys.toList()
                    if (originalValueOrder != currentValueOrder) {
                        error("Invalid order. Expected '$originalValueOrder', but found '$currentValueOrder'")
                    }
                }
            }

            val keyLengthMap = mutableMapOf<String, Int>()
            blockRows.forEach { blockRow ->
                blockRow.fullData.forEach { (key, values) ->
                    if (keyLengthMap.containsKey(key) && keyLengthMap[key] != values.size) {
                        error("Item count mismatch. For '$key', ${keyLengthMap[key]} rows expected, but found ${values.size} in '${blockRow.title}' block")
                    } else {
                        keyLengthMap[key] = values.size
                    }
                }
            }
        }


        private fun isTestName(line: String): Boolean {
            return testNameRegex.matches(line)
        }

        private fun parseTitle(title: String): String {
            return title
                .replace(titleStripRegEx, " ")
                .replace("\\s{2,}".toRegex(), " ")
                .trim()
        }

        private fun parseGenericTitle(title: String): String {
            return title
                .replace(genericTitleStripRegEx, " ")
                .replace("\\s{2,}".toRegex(), " ")
                .trim()
        }

        private fun isHumanLine(line: String): Boolean {
            return !isMachineLine(line)
        }

        private fun isMachineLine(line: String): Boolean {
            return line.matches(machineLineRegEx)
        }

        private fun parseValues(key: String, data: String): Map<String, Float> {
            if (!data.startsWith(key)) {
                error("Invalid $key.Expected to start with '$key' but found '$data'")
            }

            val transformedList = data.replace(key, "")
                .replace("\\s+".toRegex(), " ")
                .split(", ")
                // remove commas in numbers
                .map { it.replace(",", "").trim().split(" ") }

            val valueMap = mutableMapOf<String, Float>()
            for (item in transformedList) {
                valueMap[item[0]] = item[1].toFloat()
            }
            return valueMap
        }


        private fun String.findMetricKeyOrNull(): String? {
            return metricKeys.find { this.startsWith(it) }
        }

        private fun String.shouldSkip(): Boolean {
            return this == "startup type is: cold" || this == "startup type is: warm" || this == "startup type is: hot"
        }
    }


}


private fun FormData.isGenericInput(): Boolean {
    return !this.data.contains(
        SupportedMetrics.values().joinToString(separator = "|", prefix = "(", postfix = ")") { it.key }.toRegex()
    )
}

