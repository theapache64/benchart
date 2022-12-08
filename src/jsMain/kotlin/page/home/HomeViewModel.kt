package page.home

import androidx.compose.runtime.*
import components.SavedBenchmarkNode
import components.SavedBenchmarks
import components.SummaryNode
import core.BenchmarkResult
import core.toCharts
import kotlinx.browser.window
import model.Charts
import model.FormData
import repo.BenchmarkRepo
import repo.BenchmarkRepoImpl.Companion.KEY_SAVED_BENCHMARKS
import repo.FormRepo
import utils.DefaultValues
import utils.SummaryUtils

@Stable
class HomeViewModel(
    private val benchmarkRepo: BenchmarkRepo,
    private val formRepo: FormRepo
) {

    companion object {
        private const val ERROR_GENERIC = "Something went wrong!"

        // keys
    }

    var savedBenchmarks by mutableStateOf<List<SavedBenchmarkNode>>(emptyList())
        private set

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

    var isAutoGroupEnabled by mutableStateOf(false)
        private set

    var durationSummary = mutableStateListOf<SummaryNode>()
        private set

    var overrunSummary = mutableStateListOf<SummaryNode>()
        private set

    var form by mutableStateOf(
        formRepo.getFormData() ?: FormData(DefaultValues.form)
    )
        private set

    init {
        refreshedBenchmarks()
    }


    private fun refreshedBenchmarks() {
        savedBenchmarks = benchmarkRepo.getSavedBenchmarks()
    }

    // Normal fields
    private val fullBenchmarkResults = mutableListOf<BenchmarkResult>()

    fun onFormChanged(newForm: FormData) {
        form = newForm
        formRepo.saveFormData(newForm)
        try {
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
            val newCharts = filteredBenchmarkResult.toCharts()
            charts = newCharts
            updateSummary(newCharts)

            errorMsg = ""
        } catch (e: Throwable) {
            durationSummary.clear()
            overrunSummary.clear()
            e.printStackTrace()
            errorMsg = e.message ?: ERROR_GENERIC
        }
    }

    private fun updateSummary(charts: Charts) {
        // Calculating duration summary
        SummaryUtils.prepareSummary(groupMap = charts.groupMap,
            chartData = charts.frameDurationChart,
            onSummaryReady = { summaryNodes ->
                durationSummary.clear()
                durationSummary.addAll(summaryNodes)
            },
            onSummaryFailed = {
                durationSummary.clear()
            })

        val frameOverrunChart = charts.frameOverrunChart
        if (frameOverrunChart != null && frameOverrunChart.dataSets.isNotEmpty()) {
            SummaryUtils.prepareSummary(
                groupMap = charts.groupMap,
                chartData = frameOverrunChart,
                onSummaryReady = { summaryNodes ->
                    overrunSummary.clear()
                    overrunSummary.addAll(summaryNodes)
                },
                onSummaryFailed = {
                    overrunSummary.clear()
                })
        } else {
            overrunSummary.clear()
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
            charts = filteredBenchmarkResult.toCharts()
            updateSummary(charts!!)
            errorMsg = ""
        } catch (e: Throwable) {
            durationSummary.clear()
            overrunSummary.clear()
            e.printStackTrace()
            errorMsg = e.message ?: ERROR_GENERIC
        }
    }

    fun onTitleDoubleClicked() {
        isEditableTitleEnabled = true
    }

    fun onToggleAutoGroupClicked() {
        isAutoGroupEnabled = !isAutoGroupEnabled
    }

    fun onSaveClicked(formData: FormData) {
        val bName = window.prompt("Name:")
        if (bName.isNullOrBlank()) {
            window.alert("Benchmark name can't be empty! 😕")
            return
        }

        var savedBenchmarksString = window.localStorage.getItem(KEY_SAVED_BENCHMARKS)
        val savedBenchmark = if (savedBenchmarksString == null) {
            // Creating first saved benchmark
            SavedBenchmarks(items = arrayOf())
        } else {
            JSON.parse(savedBenchmarksString)
        }

        // Appending new benchmark
        val newList = savedBenchmark.items.toMutableList().apply {
            add(
                SavedBenchmarkNode(
                    key = bName, value = formData.data
                )
            )
        }
        savedBenchmark.items = newList.toTypedArray()
        savedBenchmarksString = JSON.stringify(savedBenchmark)
        window.localStorage.setItem(KEY_SAVED_BENCHMARKS, savedBenchmarksString)
        this.savedBenchmarks = newList
    }

    fun onLoadBenchmarkClicked(savedBenchmarkNode: SavedBenchmarkNode) {
        form = form.copy(data = savedBenchmarkNode.value)
    }

    fun onDeleteBenchmarkClicked(deletedBenchmarkNode: SavedBenchmarkNode) {
        var savedBenchmarksString = window.localStorage.getItem(KEY_SAVED_BENCHMARKS)
        val savedBenchmark = if (savedBenchmarksString == null) {
            // Creating first saved benchmark
            SavedBenchmarks(items = arrayOf())
        } else {
            JSON.parse(savedBenchmarksString)
        }

        // Appending new benchmark
        val newList = savedBenchmark.items.toMutableList().apply {
            removeAll { it.key == deletedBenchmarkNode.key }
        }
        savedBenchmark.items = newList.toTypedArray()
        savedBenchmarksString = JSON.stringify(savedBenchmark)
        window.localStorage.setItem(KEY_SAVED_BENCHMARKS, savedBenchmarksString)
        this.savedBenchmarks = newList
        println("final benchmark : $newList")
    }

}
