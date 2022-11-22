package core

import Charts
import ManualFormData


fun List<ManualFormData>.toCharts(
    onInvalidData: (InvalidBenchmarkDataException?) -> Unit
): Charts {
    val benchmarkResults = this.toBenchmarkResults(onInvalidData)
    return benchmarkResults.toChartData()
}

private fun List<ManualFormData>.toBenchmarkResults(
    onInvalidData: (InvalidBenchmarkDataException?) -> Unit
): List<BenchmarkResult> {
    val benchmarkResults = mutableListOf<BenchmarkResult>()
    var hasError = false
    for (item in this) {
        try {
            benchmarkResults.add(BenchmarkResult.parse(item))
        } catch (e: InvalidBenchmarkDataException) {
            e.printStackTrace()
            hasError = true
            onInvalidData(e)
        }
    }
    if(!hasError){
        onInvalidData(null)
    }
    return benchmarkResults
}
