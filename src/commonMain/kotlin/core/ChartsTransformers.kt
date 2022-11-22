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

    return Charts(
        frameDurationChart = ChartData(
            label = BenchmarkResult.KEY_FRAME_DURATION_MS,
            dataSets = frameDurationMap
        ),
        frameOverrunChart = if (frameOverrunMap.isNotEmpty()) {
            ChartData(
                label = BenchmarkResult.KEY_FRAME_OVERRUN_MS,
                dataSets = frameOverrunMap
            )
        } else {
            null
        }
    )
}
