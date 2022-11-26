package core

import AutoFormData
import BenchmarkResult
import Charts

fun AutoFormData.toCharts(benchmarkResults : List<BenchmarkResult>): Charts {
    return benchmarkResults.toChartData()
}

