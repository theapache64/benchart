package core

import BenchmarkResult
import ChartData
import Charts

fun List<BenchmarkResult>.toChartData(): Charts {
    val frameDurationMap = mutableMapOf<String, Array<Float>>()
    val frameOverrunMap = mutableMapOf<String, Array<Float>>()
    for (item in this) {
        frameDurationMap[item.title] = item.frameDurationMs.values.toTypedArray()
        val frameOverrunMs = item.frameOverrunMs
        if (frameOverrunMs != null) {
            frameOverrunMap[item.title] = frameOverrunMs.values.toTypedArray()
        }
    }

    val colorMap = parseColorMap(this)

    return Charts(
        frameDurationChart = ChartData(
            label = BenchmarkResult.KEY_FRAME_DURATION_MS,
            dataSets = frameDurationMap,
            colorMap = colorMap
        ),
        frameOverrunChart = if (frameOverrunMap.isNotEmpty()) {
            ChartData(
                label = BenchmarkResult.KEY_FRAME_OVERRUN_MS,
                dataSets = frameOverrunMap,
                colorMap = colorMap
            )
        } else {
            null
        },
    )
}


fun parseColorMap(benchmarkResults: List<BenchmarkResult>): Map<String, String> {
    val colorMap = mutableMapOf<String, String>()
    val titles = benchmarkResults.map { it.title }
    val wordColorMap = mutableMapOf<String, String>()
    // TODO: Add more colors
    val lineColors = mutableListOf(
        "rgba(255, 99, 132, 1)",
        "rgba(54, 162, 235, 1)",
        "rgba(255, 206, 86, 1)",
        "rgba(75, 192, 192, 1)",
        "rgba(153, 102, 255, 1)",
        "rgba(255, 159, 64, 1)"
    )
    for (title in titles) {
        val firstWord = title.split(" ")[0]
        val color = wordColorMap.getOrPut(firstWord) {

            if (lineColors.isEmpty()) {
                throw IllegalStateException("lineColors exhausted")
            }

            val newColor = lineColors.random()
            lineColors.remove(newColor)
            newColor
        }
        colorMap[title] = color
    }
    return colorMap
}
