package page.home

import AutoFormData
import BenchmarkResult
import Charts
import ManualFormData
import Mode
import androidx.compose.runtime.*
import core.toCharts

@Stable
class HomeViewModel {

    // States
    var mode by mutableStateOf(Mode.AUTO)
        private set

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
    var autoFormData: AutoFormData? = null


    fun onModeChanged(newMode: Mode) {
        this.mode = newMode
    }

    fun onAutoFormChanged(form: AutoFormData) {
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
            charts = form.toCharts(filteredBenchmarkResult)
            errorMsg = ""
        } catch (e: Throwable) {
            e.printStackTrace()
            errorMsg = e.message ?: "Something went wrong!"
        }
    }

    fun onManualFormChanged(forms: List<ManualFormData>) {
        charts = forms.toCharts(
            onInvalidData = { exception ->
                errorMsg = exception?.message ?: ""
            }
        )
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
                charts = form.toCharts(filteredBenchmarkResult)
                errorMsg = ""
            } catch (e: Throwable) {
                e.printStackTrace()
                errorMsg = e.message ?: "Something went wrong!"
            }
        }
    }
}