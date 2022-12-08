package page.home

import androidx.compose.runtime.*
import components.KEY_UNSAVED_BENCHMARK
import components.SavedBenchmarkNode
import components.SummaryNode
import core.BenchmarkResult
import core.toCharts
import kotlinx.browser.window
import model.Charts
import model.FormData
import repo.BenchmarkRepo
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

    var shouldSelectUnsaved by mutableStateOf(false)
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
        refreshBenchmarks()
    }


    private fun refreshBenchmarks() {
        savedBenchmarks = benchmarkRepo.getSavedBenchmarks()
    }

    // Normal fields
    private val fullBenchmarkResults = mutableListOf<BenchmarkResult>()

    fun onFormChanged(newForm: FormData, shouldSelectUnsaved : Boolean = true) {
        form = newForm
        formRepo.saveFormData(newForm)
        this.shouldSelectUnsaved = shouldSelectUnsaved
        try {
            // clearing old data
            fullBenchmarkResults.clear()
            testNames.clear()

            // refill
            fullBenchmarkResults.addAll(BenchmarkResult.parse(newForm))
            testNames.addAll(fullBenchmarkResults.mapNotNull { it.testName }.toSet())

            val currentTestName = testNames.find { it == currentTestName } ?: testNames.firstOrNull()
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

    fun onTitleDoubleClicked() {
        isEditableTitleEnabled = true
    }

    fun onToggleAutoGroupClicked() {
        isAutoGroupEnabled = !isAutoGroupEnabled
    }

    fun onSaveClicked(formData: FormData) {
        val bName = window.prompt("Name: ")
        if (bName.isNullOrBlank()) {
            window.alert("Benchmark name can't be empty! 😕")
            return
        }

        val isExist = savedBenchmarks.find { it.key == bName } != null
        if (isExist) {
            window.alert("Bruhh.. $bName exists! Try something else")
            return
        }

        // Appending new benchmark
        val newList = savedBenchmarks.toMutableList().apply {
            add(
                index = 0,
                element = SavedBenchmarkNode(
                    key = bName, value = formData.data
                )
            )
        }
        benchmarkRepo.saveBenchmarks(newList)
        shouldSelectUnsaved = false
        refreshBenchmarks()
    }

    fun onLoadBenchmarkClicked(savedBenchmarkNode: SavedBenchmarkNode) {
        val newForm = form.copy(data = savedBenchmarkNode.value)
        onFormChanged(newForm, shouldSelectUnsaved = false)
    }

    fun onDeleteBenchmarkClicked(deletedBenchmarkNode: SavedBenchmarkNode) {
        val isYes = window.confirm(
            "Do you want to delete `${deletedBenchmarkNode.key}` ?"
        )

        if (isYes) {
            benchmarkRepo.delete(deletedBenchmarkNode)
            shouldSelectUnsaved = true
            refreshBenchmarks()
        }
    }

    fun onSavedBenchmarkChanged(key: String) {
        shouldSelectUnsaved = key == KEY_UNSAVED_BENCHMARK
        if (shouldSelectUnsaved) {
            val newForm = formRepo.getFormData() ?: FormData("")
            onFormChanged(newForm, shouldSelectUnsaved = false)
        }
    }

}
