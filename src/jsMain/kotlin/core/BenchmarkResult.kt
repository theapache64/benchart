package core

import AutomaticFormData
import ManualFormData

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

        fun parse(form: ManualFormData): BenchmarkResult {
            val frameDurationMatches = frameDurationRegEx.findAll(form.data).toList()
            val frameDurationMsRaw = frameDurationMatches.firstOrNull()
                ?: throw InvalidBenchmarkDataException("Missing $KEY_FRAME_DURATION_MS. Given '${form.data}'")
            if(frameDurationMatches.size > 1){
                throw InvalidBenchmarkDataException("Found ${frameDurationMatches.size} instances of $KEY_FRAME_DURATION_MS. Expected only one")
            }

            val frameDurationMs = parseFrameDurationMs(frameDurationMsRaw.value)

            val frameOverrunMsMatches = frameOverrunRegEx.findAll(form.data).toList()
            if(frameOverrunMsMatches.size > 1){
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

        fun parse(form: AutomaticFormData): List<BenchmarkResult> {
            val benchmarkResults = mutableListOf<BenchmarkResult>()

            return benchmarkResults
        }

        private fun parseOverrunMs(frameOverrunMsData: String): Map<String, Float> {
            try {
                return parseBenchmarkValues(KEY_FRAME_OVERRUN_MS, frameOverrunMsData)
            } catch (e: IllegalStateException) {
                throw InvalidFrameOverrunNodeException(e.message)
            }
        }

        private fun parseFrameDurationMs(frameDurationMsData: String): Map<String, Float> {
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