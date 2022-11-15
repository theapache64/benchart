package core

import FormData

class InvalidBenchmarkDataException(message: String?) : Throwable(message)
class InvalidFrameDurationNodeException(message: String?) : Throwable(message)
class InvalidFrameOverrunNodeException(message: String?) : Throwable(message)

data class BenchmarkResult(
    val title: String,
    val fileAndTestName : String,
    val frameDurationMs: Map<String, Float>,
    val frameOverrunMs: Map<String, Float>,
) {
    companion object {
        const val KEY_FRAME_DURATION_MS = "frameDurationCpuMs"
        const val KEY_FRAME_OVERRUN_MS = "frameOverrunMs"
        private const val KEY_TRACES = "Traces"

        fun parse(form: FormData): BenchmarkResult {
            val input = form.data
            val lines = input.split("\n")
            if (lines.size != 3 && lines.size != 4) {
                throw InvalidBenchmarkDataException("Invalid benchmark input : '$input'. Expected either 3 or 4 lines, but found ${lines.size}")
            }

            val fileAndTestName = lines.first().trim()
            if(fileAndTestName.startsWith(KEY_FRAME_DURATION_MS) || fileAndTestName.startsWith(KEY_FRAME_OVERRUN_MS)){
                throw InvalidBenchmarkDataException("fileAndTestName missing. First line should be fileAndTestName but found '$fileAndTestName'")
            }

            val lastLine = lines.last().trim()
            if(!lastLine.startsWith(KEY_TRACES)){
                throw InvalidBenchmarkDataException("last line should contain traces cound, but found '$lastLine'")
            }


            val frameDurationMs = parseFrameDurationMs(lines[1].trim())

            val thirdLine = lines[2].trim()
            // optional
            val frameOverrunMs = if (thirdLine.startsWith(KEY_FRAME_OVERRUN_MS)) {
                parseOverrunMs(thirdLine)
            } else {
                emptyMap()
            }

            return BenchmarkResult(
                title = form.title,
                fileAndTestName = fileAndTestName,
                frameDurationMs = frameDurationMs,
                frameOverrunMs = frameOverrunMs
            )
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