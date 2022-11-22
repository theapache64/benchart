package core

import AutoFormData
import Charts

fun AutoFormData.toCharts(): Charts {
    val benchmarkResults = BenchmarkResult.parse(this)
    return benchmarkResults.toChartData()
}

