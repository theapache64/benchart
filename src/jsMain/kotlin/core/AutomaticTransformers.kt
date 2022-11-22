package core

import AutomaticFormData
import Charts

fun AutomaticFormData.toCharts(): Charts {
    val benchmarkResults = BenchmarkResult.parse(this)
    return benchmarkResults.toChartData()
}

