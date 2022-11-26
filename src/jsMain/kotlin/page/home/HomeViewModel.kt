package page.home

import BenchmarkResult
import Charts
import FormData
import androidx.compose.runtime.*
import core.toChartData

@Stable
class HomeViewModel {

    // States
    var currentTestName by mutableStateOf<String?>(null)
        private set

    var testNames = mutableStateListOf<String>()
        private set

    var charts by mutableStateOf<Charts?>(null)
        private set

    var errorMsg by mutableStateOf("")
        private set


    // Normal fields
    val fullBenchmarkResults = mutableListOf<BenchmarkResult>()
    var autoFormData: FormData? = null

    fun onAutoFormChanged(form: FormData) {
        try {
            autoFormData = form
            println("Form updated : $form")
            fullBenchmarkResults.clear()
            fullBenchmarkResults.addAll(BenchmarkResult.parse(form))
            testNames.clear()
            testNames.addAll(fullBenchmarkResults.mapNotNull { it.testName }.toSet())
            if (currentTestName == null) {
                currentTestName = testNames.firstOrNull()
            }
            val filteredBenchmarkResult = if (currentTestName != null) {
                fullBenchmarkResults.filter { it.testName == currentTestName }
            } else {
                fullBenchmarkResults
            }
            charts = filteredBenchmarkResult.toChartData()
            errorMsg = ""
        } catch (e: Throwable) {
            e.printStackTrace()
            errorMsg = e.message ?: "Something went wrong!"
        }
    }

    fun onTestNameChanged(newTestName: String) {
        autoFormData?.let { form ->
            try {
                currentTestName = newTestName
                val filteredBenchmarkResult = if (currentTestName != null) {
                    fullBenchmarkResults.filter { it.testName == currentTestName }
                } else {
                    fullBenchmarkResults
                }
                charts = filteredBenchmarkResult.toChartData()
                errorMsg = ""
            } catch (e: Throwable) {
                e.printStackTrace()
                errorMsg = e.message ?: "Something went wrong!"
            }
        }
    }
}