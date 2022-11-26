package page.home

import BenchmarkResult
import Charts
import FormData
import androidx.compose.runtime.*
import core.toChartData

@Stable
class HomeViewModel {

    companion object {
        private const val ERROR_GENERIC = "Something went wrong!"
    }

    // States
    private var currentTestName: String? = null

    var testNames = mutableStateListOf<String>()
        private set

    var charts by mutableStateOf<Charts?>(null)
        private set

    var errorMsg by mutableStateOf("")
        private set

    var isEditableTitleEnabled by mutableStateOf(false)
        private set

    // Normal fields
    private val fullBenchmarkResults = mutableListOf<BenchmarkResult>()
    var formData: FormData? = null

    fun onFormChanged(newForm: FormData) {
        try {
            formData = newForm
            fullBenchmarkResults.clear()
            fullBenchmarkResults.addAll(BenchmarkResult.parse(newForm))
            testNames.clear()
            testNames.addAll(fullBenchmarkResults.mapNotNull { it.testName }.toSet())

            val hasCurrentTestName = testNames.find { it == currentTestName } != null
            if (!hasCurrentTestName) {
                currentTestName = null
            }

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
            errorMsg = e.message ?: ERROR_GENERIC
        }
    }

    fun onTestNameChanged(newTestName: String) {
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
            errorMsg = e.message ?: ERROR_GENERIC
        }
    }

    fun onTitleDoubleClicked() {
        isEditableTitleEnabled = true
    }
}