package core

import ChartData
import Charts
import ManualFormData


fun List<ManualFormData>.toCharts(): Charts {
    val benchmarkResults = this.toBenchmarkResults()
    return benchmarkResults.toChartData()
}

private fun List<ManualFormData>.toBenchmarkResults(): List<BenchmarkResult> {
    val benchmarkResults = mutableListOf<BenchmarkResult>()
    for (item in this) {
        benchmarkResults.add(BenchmarkResult.parse(item))
    }
    return benchmarkResults
}
