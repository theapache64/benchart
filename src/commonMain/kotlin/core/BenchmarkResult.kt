package core

import model.FormData

open class InvalidDataException(message: String?) : Throwable(message)
class InvalidBenchmarkDataException(message: String?) : InvalidDataException(message)
class InvalidGenericDataException(message: String?) : InvalidDataException(message)

data class BlockRow(
    val title: String,
    val data: Map<String, Float>
)

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

data class BenchmarkResult(
    val title: String,
    val testName: String?,
    val blockRows: List<BlockRow>,
) {
    companion object {

        private val metricKeys = SupportedMetrics.values().map { it.key }

        private val machineLineRegEx = "^(Traces|${metricKeys.joinToString(separator = "|")}).+".toRegex()
        private val titleStripRegEx = "\\W+".toRegex()
        private val genericTitleStripRegEx = "\\W+".toRegex()
        private val testNameRegex = "[A-Z].*_[a-z].*".toRegex()

        fun parse(form: FormData): Pair<InputType, List<BenchmarkResult>>? {

            val blocks = form.data
                .split("\n").joinToString(separator = "\n") { it.trim() }
                .split("^\\s+".toRegex(RegexOption.MULTILINE)).map { it.trim() }
                .filter { it.isNotBlank() }

            println("parsing input...")
            if (blocks.isEmpty()) return null
            if (form.isGenericInput()) return parseGenericInput(blocks)

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
                    if (metricName != null) {
                        val isMetricAlreadyAdded = blockRows.find { it.title == metricName } != null
                        if (isMetricAlreadyAdded) {
                            throw InvalidBenchmarkDataException("Two $metricName found in block ${index + 1}. Expected only one")
                        }
                        blockRows.add(
                            BlockRow(
                                title = metricName,
                                data = parseValues(metricName, line)
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

            return Pair(InputType.NORMAL_BENCHMARK, benchmarkResults)
        }

        private fun parseGenericInput(blocks: List<String>): Pair<InputType, List<BenchmarkResult>> {
            return Pair(InputType.GENERIC, parseMultiLineGenericInput(blocks))
        }

        private fun createChartTitle(blockRows: MutableList<BlockRow>): String {
            return blockRows.joinToString(separator = " vs ") { it.title }
        }

        private fun parseMultiLineGenericInput(blocks: List<String>): List<BenchmarkResult> {
            val benchmarkResults = mutableListOf<BenchmarkResult>()
            val blockRows = mutableListOf<BlockRow>()
            for ((index, block) in blocks.withIndex()) {
                val lines = block.split("\n").map { it.trim() }
                var title: String? = null
                val valuesMap = mutableMapOf<String, MutableList<Float>>()
                for (line in lines) {

                    if (title == null && isHumanLine(line)) {
                        title = line
                        continue
                    }

                    val textNumberLine = TextNumberLine.parse(line)
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
                        data = valuesMap.map { (key, value) ->
                            key to value.average().toFloat()
                        }.toMap()
                    )
                )
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

            return benchmarkResults
        }

        private fun checkDataIntegrity(blockRows: List<BlockRow>) {
            if (blockRows.size >= 2) {
                val originalValueOrder = blockRows.first().data.keys.toList()
                for ((index, blockRow) in blockRows.withIndex()) {
                    if (index == 0) {
                        continue
                    }
                    val currentValueOrder = blockRow.data.keys.toList()
                    if (originalValueOrder != currentValueOrder) {
                        error("Invalid order. Expected '$originalValueOrder', but found '$currentValueOrder'")
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
    }
}


private fun FormData.isGenericInput(): Boolean {
    return !this.data.contains(
        SupportedMetrics.values().joinToString(separator = "|", prefix = "(", postfix = ")") { it.key }.toRegex()
    )
}

