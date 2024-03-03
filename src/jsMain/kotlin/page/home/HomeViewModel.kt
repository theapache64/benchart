package page.home

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import components.KEY_UNSAVED_BENCHMARK
import components.SavedBenchmarkNode
import components.Summary
import core.BenchmarkResult
import core.InputType
import core.toCharts
import core.toGenericChart
import kotlinx.browser.window
import model.ChartsBundle
import model.FormData
import repo.BenchmarkRepo
import repo.FormRepo
import utils.DefaultValues
import utils.SummaryUtils

external fun setTimeout(handler: dynamic, timeout: Int): Int
external fun clearTimeout(timeoutId: Int)


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

    var chartsBundle by mutableStateOf<ChartsBundle?>(null)
        private set

    var errorMsg by mutableStateOf("")
        private set

    var isEditableTitleEnabled by mutableStateOf(false)
        private set

    var shouldSelectUnsaved by mutableStateOf(false)
        private set

    var selectedBlockNameOne by mutableStateOf<String?>(null)
        private set

    var selectedBlockNameTwo by mutableStateOf<String?>(null)
        private set

    var blockNames = mutableStateListOf<String>()
        private set

    var summaries = mutableStateListOf<Summary>()
        private set

    var inputType by mutableStateOf<InputType?>(null)
        private set

    var unit by mutableStateOf("")
        private set

    var bestAggSummary by mutableStateOf<AggSummary?>(null)
        private set

    var worstAggSummary by mutableStateOf<AggSummary?>(null)
        private set

    var form by mutableStateOf(
        formRepo.getFormData() ?: FormData(
            DefaultValues.form,
            isTestNameDetectionEnabled = false,
            isAutoGroupEnabled = false
        )
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


    var timeoutId: Int? = null
    fun <T> debounce(func: () -> Unit, delay: Int) {
        timeoutId?.let { clearTimeout(it) }
        timeoutId = setTimeout({
            func()
        }, delay)
    }

    fun onFormChanged(newForm: FormData, shouldSelectUnsaved: Boolean = true) {
        form = newForm
        formRepo.saveFormData(newForm)

        debounce<Unit>(
            func = {

                this.shouldSelectUnsaved = shouldSelectUnsaved
                try {
                    // clearing old data
                    fullBenchmarkResults.clear()
                    testNames.clear()
                    blockNames.clear()

                    // refill
                    val (inputType, benchmarkResults) = BenchmarkResult.parse(newForm) ?: run {
                        println("failed to parse form")
                        reset()
                        errorMsg = ""
                        return@debounce
                    }
                    this.inputType = inputType
                    fullBenchmarkResults.addAll(benchmarkResults)

                    when (inputType) {
                        InputType.GENERIC -> {
                            val newCharts = fullBenchmarkResults.toGenericChart()
                            chartsBundle = newCharts
                            onChartsBundleUpdated(newCharts)
                            unit = ""
                        }

                        InputType.NORMAL_BENCHMARK -> {

                            testNames.addAll(fullBenchmarkResults.mapNotNull { it.testName }.toSet())

                            val currentTestName = testNames.find { it == currentTestName } ?: testNames.firstOrNull()
                            val filteredBenchmarkResult = if (currentTestName != null) {
                                fullBenchmarkResults.filter { it.testName == currentTestName }
                            } else {
                                fullBenchmarkResults
                            }
                            val newCharts = filteredBenchmarkResult.toCharts()
                            chartsBundle = newCharts
                            onChartsBundleUpdated(newCharts)
                            unit = "ms"
                        }
                    }


                    errorMsg = ""
                } catch (e: Throwable) {
                    e.printStackTrace()
                    errorMsg = e.message ?: ERROR_GENERIC
                    reset()
                }
            },
            300
        )
    }

    private fun reset() {
        selectedBlockNameOne = null
        selectedBlockNameTwo = null
        blockNames.clear()
        chartsBundle = null
        summaries.clear()
        bestAggSummary = null
        worstAggSummary = null
        updateSummary()
    }

    private fun calcAggSummary() {
        val isGeneric = inputType == InputType.GENERIC
        val newAggSums = mutableListOf<AggSummary>()
        for (blockNameOuter in blockNames) {
            for (blockNameInner in blockNames) {
                if(blockNameOuter==blockNameInner){
                    continue
                }
                chartsBundle?.charts?.mapNotNull { chart ->
                    SummaryUtils.getSummaryOrThrow(
                        isGeneric = isGeneric,
                        chart = chart,
                        selectedBlockNameOne = blockNameOuter,
                        selectedBlockNameTwo = blockNameInner
                    )
                }?.let { summaries ->
                    var greenSum = 0
                    var redSum = 0
                    for (summary in summaries) {
                        for (node in summary.nodes) {
                            when {
                                node.diff > 0 -> {
                                    // bad
                                    redSum += node.diff.toInt()
                                }
                                node.diff < 0 -> {
                                    // green
                                    greenSum -= node.diff.toInt()
                                }
                            }
                        }
                    }
                    newAggSums.add(AggSummary(blockNameOuter, blockNameInner, sumOfGreen = greenSum, sumOfRed = redSum))
                }
            }
        }

        bestAggSummary = newAggSums.maxByOrNull { it.sumOfGreen }
        worstAggSummary = newAggSums.maxByOrNull { it.sumOfRed }
    }

    private fun onChartsBundleUpdated(chartsBundle: ChartsBundle) {
        blockNames.clear()
        val blockNames = chartsBundle.groupMap.wordColorMap.keys.toList()
        this.blockNames.addAll(blockNames)
        if (blockNames.size >= 2) {
            selectedBlockNameOne = blockNames[0]
            selectedBlockNameTwo = blockNames[1]
        }else {
            selectedBlockNameOne = null
            selectedBlockNameTwo = null
        }
        updateSummary()
    }

    private fun updateSummary() {
        // Calculating duration summary
        summaries.clear()

        val isGeneric = inputType == InputType.GENERIC
        val allSummaries = chartsBundle?.charts?.mapNotNull { chart ->
            SummaryUtils.getSummaryOrThrow(
                isGeneric = isGeneric,
                chart = chart,
                selectedBlockNameOne = selectedBlockNameOne,
                selectedBlockNameTwo = selectedBlockNameTwo
            )
        }
        summaries.addAll(allSummaries ?: emptyList())
        calcAggSummary()
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
            chartsBundle = newCharts
            updateSummary()
            errorMsg = ""
        } catch (e: Throwable) {
            summaries.clear()
            e.printStackTrace()
            errorMsg = e.message ?: ERROR_GENERIC
        }
    }

    fun onTitleDoubleClicked() {
        isEditableTitleEnabled = true
    }

    fun onToggleAutoGroupClicked() {
        onFormChanged(form.copy(isAutoGroupEnabled = !form.isAutoGroupEnabled))
    }

    fun onToggleTestNameDetectionClicked() {
        onFormChanged(form.copy(isTestNameDetectionEnabled = !form.isTestNameDetectionEnabled))
    }

    fun onSaveClicked(formData: FormData) {
        val bName = window.prompt("Name: ")
        if (bName.isNullOrBlank()) {
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
            val newForm = formRepo.getFormData() ?: form
            onFormChanged(newForm, shouldSelectUnsaved = false)
        }
    }

    fun onBlockNameOneChanged(newBlockName: String) {
        selectedBlockNameOne = newBlockName
        updateSummary()
    }

    fun onBlockNameTwoChanged(newBlockName: String) {
        selectedBlockNameTwo = newBlockName
        updateSummary()
    }

    fun onBestClicked() {
        selectedBlockNameOne = bestAggSummary?.blockOneName
        selectedBlockNameTwo = bestAggSummary?.blockTwoName
        updateSummary()
    }

    fun onWorstClicked() {
        selectedBlockNameOne = worstAggSummary?.blockOneName
        selectedBlockNameTwo = worstAggSummary?.blockTwoName
        updateSummary()
    }
}

data class AggSummary(
    val blockOneName: String,
    val blockTwoName: String,
    val sumOfGreen: Int,
    val sumOfRed: Int
)
