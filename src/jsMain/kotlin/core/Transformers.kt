package core

import ChartData
import Charts
import FormData


fun List<FormData>.toCharts(): Charts {
    val benchmarkResults = this.toBenchmarkResults()
    return benchmarkResults.toChartData()
}

private fun List<FormData>.toBenchmarkResults(): List<BenchmarkResult> {
    val benchmarkResults = mutableListOf<BenchmarkResult>()
    for (item in this) {
        benchmarkResults.add(BenchmarkResult.parse(item))
    }
    return benchmarkResults
}


private fun List<BenchmarkResult>.toChartData(): Charts {
    val frameDurationMap = mutableMapOf<String, Array<Float>>()
    val frameOverrunMap = mutableMapOf<String, Array<Float>>()
    for (item in this) {
        frameDurationMap[item.title] = item.frameDurationMs.values.toTypedArray()
        frameOverrunMap[item.title] = item.frameOverrunMs.values.toTypedArray()
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

