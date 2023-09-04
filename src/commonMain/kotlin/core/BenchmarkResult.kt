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
    SINGLE_LINE_GENERIC,
    MULTI_LINE_GENERIC,
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
        private val testNameRegex = "[A-Z].*_[a-z].*".toRegex()

        fun parse(form: FormData): Pair<InputType, List<BenchmarkResult>> {
            println("parsing input...")
            if (form.isGenericInput()) return parseGenericInput(form)

            println("parsing machine generated benchmark input...")
            val benchmarkResults = mutableListOf<BenchmarkResult>()

            val blocks = form.data
                .split("\n").joinToString(separator = "\n") { it.trim() }
                .split("^\\s+".toRegex(RegexOption.MULTILINE)).map { it.trim() }

            for ((index, block) in blocks.withIndex()) {
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

        private fun parseGenericInput(form: FormData): Pair<InputType, List<BenchmarkResult>> {

            val blocks = form.data
                .split("\n").joinToString(separator = "\n") { it.trim() }
                .split("^\\s+".toRegex(RegexOption.MULTILINE)).map { it.trim() }

            return if (blocks.areSingleLineGenericInput()) {
                println("parsing single line generic input")
                Pair(InputType.SINGLE_LINE_GENERIC, parseSingleLineGenericInput(blocks))
            } else {
                println("parsing multi line generic input")
                Pair(InputType.MULTI_LINE_GENERIC, parseMultiLineGenericInput(blocks))
            }
        }


        private fun List<String>.areSingleLineGenericInput(): Boolean {
            for (block in this) {
                val lines = block.split("\n").map { it.trim() }.filter { it.isNotBlank() }
                if (lines.size > 2) { // 2 = 1 for title and 1 for value
                    return false
                }
            }
            return true
        }

        private fun parseMultiLineGenericInput(blocks: List<String>): List<BenchmarkResult> {
            val benchmarkResults = mutableListOf<BenchmarkResult>()
            val blockRows = mutableListOf<BlockRow>()
            for ((index, block) in blocks.withIndex()) {
                val lines = block.split("\n").map { it.trim() }
                var title: String? = null
                val values = mutableMapOf<String, Float>()
                for (line in lines) {

                    if (title == null && isHumanLine(line)) {
                        title = line
                        continue
                    }

                    val textNumberLine = TextNumberLine.parse(line)
                    values[parseGenericTitleForMultiLine(textNumberLine.text)] = textNumberLine.number
                }



                if (title == null) {
                    title = "benchmark $index"
                }

                title = parseGenericTitleForMultiLine(title)

                blockRows.add(
                    BlockRow(
                        title = title,
                        data = values
                    )
                )
            }

            benchmarkResults.add(
                BenchmarkResult(
                    title = "test title",
                    testName = "sample test name",
                    blockRows = blockRows
                )
            )

            return benchmarkResults
        }

        private fun parseSingleLineGenericInput(blocks: List<String>): List<BenchmarkResult> {
            val values = mutableMapOf<String, Float>()
            for ((index, block) in blocks.withIndex()) {
                val lines = block.split("\n").map { it.trim() }
                var title: String? = null

                for (line in lines) {
                    if (title == null && isHumanLine(line)) {
                        title = parseGenericTitleForSingleLine(line)
                        continue
                    }

                    val value = TextNumberLine.parse(line)
                    values[title ?: "key $index"] = value.number
                }
            }

            if (values.isEmpty()) {
                throw InvalidGenericDataException("Couldn't parse generic data")
            }

            return listOf(
                BenchmarkResult(
                    title = "",
                    testName = "",
                    blockRows = listOf(
                        BlockRow(
                            title = "",
                            data = values
                        )
                    )
                )
            )
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

        private fun parseGenericTitleForSingleLine(title: String): String {
            return title
                .replace("#", "")
                .replace("\\s{2,}".toRegex(), " ")
                .trim()
        }

        private fun parseGenericTitleForMultiLine(title: String): String {
            return parseTitle(title).also {
                println("$title -> $it")
            }
        }

        private fun isHumanLine(line: String): Boolean {
            return !isMachineLine(line)
        }

        private fun isMachineLine(line: String): Boolean {
            return line.matches(machineLineRegEx)
        }

        private fun parseValues(key: String, data: String): Map<String, Float> {
            if (!data.startsWith(key)) {
                error("Invalid $key. Expected to start with '$key' but found '$data'")
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

private val digitRegex = "\\d+(.\\d+)?".toRegex()

private data class TextNumberLine(
    val text: String,
    val number: Float
) {
    companion object {
        fun parse(line: String): TextNumberLine {
            val number = digitRegex.findAll(line)
                .firstOrNull()
                ?.groupValues
                ?.firstOrNull()
                ?: error("$line deosn't have numbers in it")
            val newLine = line.replaceFirst(number, "")
            return TextNumberLine(newLine, number.toFloat())
        }
    }
}

private fun FormData.isGenericInput(): Boolean {
    return !this.data.contains(
        SupportedMetrics.values().joinToString(separator = "|", prefix = "(", postfix = ")") { it.key }.toRegex()
    )
}

