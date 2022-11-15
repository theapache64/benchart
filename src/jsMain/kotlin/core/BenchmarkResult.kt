package core

class InvalidBenchmarkDataException(message: String?) : Throwable(message)
class InvalidFrameDurationNodeException(message: String?) : Throwable(message)
class InvalidFrameOverrunNodeException(message: String?) : Throwable(message)

data class BenchmarkResult(
    val title: String,
    val frameDurationMs: Map<String, Float>,
    val frameOverrunMs: Map<String, Float>?,
) {
    companion object {
        private const val KEY_FRAME_DURATION_MS = "frameDurationCpuMs"
        private const val KEY_FRAME_OVERRUN_MS = "frameOverrunMs"
        private const val KEY_TRACES = "Traces"

        fun parse(input: String): BenchmarkResult {
            val lines = input.split("\n")
            if (lines.size != 3 && lines.size != 4) {
                throw InvalidBenchmarkDataException("Invalid benchmark input : '$input'. Expected either 3 or 4 lines, but found ${lines.size}")
            }

            val title = lines.first().trim()
            if(title.startsWith(KEY_FRAME_DURATION_MS) || title.startsWith(KEY_FRAME_OVERRUN_MS)){
                throw InvalidBenchmarkDataException("title missing. First line should be title but found '$title'")
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
                null
            }

            return BenchmarkResult(
                title = title,
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