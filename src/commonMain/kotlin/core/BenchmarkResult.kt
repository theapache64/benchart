package core

import model.FormData

class InvalidBenchmarkDataException(message: String?) : Throwable(message)
class InvalidFrameDurationNodeException(message: String?) : Throwable(message)
class InvalidFrameOverrunNodeException(message: String?) : Throwable(message)

data class BenchmarkResult(
    val title: String,
    val testName: String?,
    val frameDurationMs: Map<String, Float>,
    val frameOverrunMs: Map<String, Float>?,
) {
    companion object {
        const val KEY_FRAME_DURATION_MS = "frameDurationCpuMs"
        const val KEY_FRAME_OVERRUN_MS = "frameOverrunMs"

        private val machineLineRegEx = "^(frameDurationCpuMs|frameOverrunMs|Traces).+".toRegex()
        private val titleStripRegEx = "\\W+".toRegex()
        private val testNameRegex = "[A-Z].*_[a-z].*".toRegex()

        fun parse(form: FormData): List<BenchmarkResult> {
            val benchmarkResults = mutableListOf<BenchmarkResult>()

            val blocks = form.data
                .split("\n").joinToString(separator = "\n") { it.trim() }
                .split("^\\s+".toRegex(RegexOption.MULTILINE)).map { it.trim() }

            for ((index, block) in blocks.withIndex()) {
                val lines = block.split("\n").map { it.trim() }
                var title: String? = null
                var testName: String? = null
                var durationMs: Map<String, Float>? = null
                var overrunMs: Map<String, Float>? = null
                for (line in lines) {

                    if (title == null && isHumanLine(line)) {
                        title = line
                    }

                    if (isTestName(line)) {
                        if (testName != null && durationMs != null) {

                            if (title == null) {
                                title = "benchmark $index $testName"
                            }

                            // We already have an unsaved testData, so let's save it
                            benchmarkResults.add(
                                BenchmarkResult(
                                    title = title,
                                    testName = testName,
                                    frameDurationMs = durationMs,
                                    frameOverrunMs = overrunMs
                                )
                            )

                            durationMs = null
                            overrunMs = null
                        }

                        testName = line
                    }

                    if (line.startsWith(KEY_FRAME_DURATION_MS)) {
                        if (durationMs != null) {
                            throw InvalidBenchmarkDataException("Two $KEY_FRAME_DURATION_MS found in block ${index + 1}. Expected only one")
                        }
                        durationMs = parseDurationMs(line)
                    }

                    if (line.startsWith(KEY_FRAME_OVERRUN_MS)) {
                        if (overrunMs != null) {
                            throw InvalidBenchmarkDataException("Two $KEY_FRAME_OVERRUN_MS found in block ${index + 1}. Expected only one")
                        }
                        overrunMs = parseOverrunMs(line)
                    }
                }

                if (title == null) {
                    title = "benchmark $index"
                }

                title = parseTitle(title)

                if (durationMs != null) {
                    benchmarkResults.add(
                        BenchmarkResult(
                            title = title,
                            testName = testName,
                            frameDurationMs = durationMs,
                            frameOverrunMs = overrunMs
                        )
                    )
                }
            }

            return benchmarkResults
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

        private fun isHumanLine(line: String): Boolean {
            return !isMachineLine(line)
        }

        private fun isMachineLine(line: String): Boolean {
            return line.matches(machineLineRegEx)
        }


        private fun parseOverrunMs(frameOverrunMsData: String): Map<String, Float> {
            try {
                return parseBenchmarkValues(KEY_FRAME_OVERRUN_MS, frameOverrunMsData)
            } catch (e: IllegalStateException) {
                throw InvalidFrameOverrunNodeException(e.message)
            }
        }

        private fun parseDurationMs(frameDurationMsData: String): Map<String, Float> {
            try {
                return parseBenchmarkValues(KEY_FRAME_DURATION_MS, frameDurationMsData)
            } catch (e: IllegalStateException) {
                throw InvalidFrameDurationNodeException(e.message)
            }
        }

        private fun parseBenchmarkValues(key: String, data: String): Map<String, Float> {
            if (!data.startsWith(key)) {
                error("Invalid $key. Expected to start with '$key' but found '$data'")
            }

            val transformedList = data.replace(key, "")
                .replace("\\s+".toRegex(), " ")
                .split(",")
                .map {
                    it.trim().split(" ")
                }

            val valueMap = mutableMapOf<String, Float>()
            for (item in transformedList) {
                valueMap[item[0]] = item[1].toFloat()
            }
            return valueMap
        }
    }
}