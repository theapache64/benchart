class InvalidBenchmarkDataException(message: String?) : Throwable(message)
class InvalidFrameDurationNodeException(message: String?) : Throwable(message)
class InvalidFrameOverrunNodeException(message: String?) : Throwable(message)

data class BenchmarkResult(
    val title: String,
    val frameDurationMs: Map<String, Float>,
    val frameOverrunMs: Map<String, Float>?,
) {
    companion object {
        const val KEY_FRAME_DURATION_MS = "frameDurationCpuMs"
        const val KEY_FRAME_OVERRUN_MS = "frameOverrunMs"
        private const val KEY_TRACES = "Traces"

        private val frameDurationRegEx = "($KEY_FRAME_DURATION_MS.+)".toRegex(RegexOption.MULTILINE)
        private val frameOverrunRegEx = "$KEY_FRAME_OVERRUN_MS.+".toRegex()

        private val machineLineRegEx = "^(frameDurationCpuMs|frameOverrunMs|Traces).+".toRegex()
        private val titleStripRegEx = "\\W+".toRegex()

        fun parse(form: ManualFormData): BenchmarkResult {
            val frameDurationMatches = frameDurationRegEx.findAll(form.data).toList()
            val frameDurationMsRaw = frameDurationMatches.firstOrNull()
                ?: throw InvalidBenchmarkDataException("Missing $KEY_FRAME_DURATION_MS. Given '${form.data}'")
            if (frameDurationMatches.size > 1) {
                throw InvalidBenchmarkDataException("Found ${frameDurationMatches.size} instances of $KEY_FRAME_DURATION_MS. Expected only one")
            }

            val frameDurationMs = parseDurationMs(frameDurationMsRaw.value)

            val frameOverrunMsMatches = frameOverrunRegEx.findAll(form.data).toList()
            if (frameOverrunMsMatches.size > 1) {
                throw InvalidBenchmarkDataException("Found ${frameOverrunMsMatches.size} instances of $KEY_FRAME_OVERRUN_MS. Expected only one")
            }
            val frameOverrunMsRaw = frameOverrunMsMatches.firstOrNull()

            // optional
            val frameOverrunMs = if (frameOverrunMsRaw != null) {
                parseOverrunMs(frameOverrunMsRaw.value)
            } else {
                null
            }

            return BenchmarkResult(
                title = form.title,
                frameDurationMs = frameDurationMs,
                frameOverrunMs = frameOverrunMs
            )
        }

        fun parse(form: AutoFormData): List<BenchmarkResult> {
            val benchmarkResults = mutableListOf<BenchmarkResult>()

            val blocks = form.data
                .split("\n").joinToString(separator = "\n") { it.trim() }
                .split("^\\s+".toRegex(RegexOption.MULTILINE)).map { it.trim() }
            for ((index, block) in blocks.withIndex()) {
                val lines = block.split("\n").map { it.trim() }
                var title: String? = null
                var durationMs: Map<String, Float>? = null
                var overrunMs: Map<String, Float>? = null
                for (line in lines) {

                    if (title == null && isHumanLine(line)) {
                        title = line
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
                    val isDuplicateTitle = benchmarkResults.find { it.title == title } != null
                    if (isDuplicateTitle) {
                        throw InvalidBenchmarkDataException("Duplicate title found. '$title' already exist")
                    }

                    benchmarkResults.add(
                        BenchmarkResult(
                            title = title,
                            frameDurationMs = durationMs,
                            frameOverrunMs = overrunMs
                        )
                    )
                }
            }

            return benchmarkResults
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