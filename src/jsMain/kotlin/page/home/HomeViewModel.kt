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
import core.BenchmarkResult.Companion.FOCUS_GROUP_ALL
import core.InputType
import core.toCharts
import core.toGenericChart
import kotlinx.browser.window
import model.ChartsBundle
import model.FormData
import org.w3c.dom.events.KeyboardEvent
import repo.BenchmarkRepo
import repo.FormRepo
import repo.GoogleFormRepo
import utils.DefaultValues
import utils.RandomString
import utils.SummaryUtils

external fun setTimeout(handler: dynamic, timeout: Int): Int
external fun clearTimeout(timeoutId: Int)


@Stable
class HomeViewModel(
    private val benchmarkRepo: BenchmarkRepo,
    private val formRepo: FormRepo,
    private val googleFormRepo: GoogleFormRepo
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


    var currentFocusedGroup by mutableStateOf(FOCUS_GROUP_ALL)
        private set

    var focusGroups = mutableStateListOf<String>()
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

    var avgOfCount by mutableStateOf<Int>(-1)
        private set

    var isAutoGroupButtonVisible by mutableStateOf<Boolean>(false)
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

        // set key press listener on window
        window.addEventListener("keydown", {
            val event = it.unsafeCast<KeyboardEvent>()
            if (event.key == "Escape") {
                onFocusGroupSelected(FOCUS_GROUP_ALL)
            }
        })
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

    fun onFormChanged(unfilteredForm: FormData, shouldSelectUnsaved: Boolean = true) {
        // filtering android log
        form = unfilteredForm.copy(data = filterOutAndroidJunkLog(unfilteredForm.data))
        formRepo.storeFormData(form)

        debounce<Unit>(
            func = {

                this.shouldSelectUnsaved = shouldSelectUnsaved
                try {
                    // clearing old data
                    fullBenchmarkResults.clear()
                    testNames.clear()
                    focusGroups.clear()
                    blockNames.clear()

                    // refill
                    val (inputType, benchmarkResults, focusGroups) = BenchmarkResult.parse(form, currentFocusedGroup)
                        ?: run {
                            println("failed to parse form")
                            reset()
                            errorMsg = ""
                            return@debounce
                        }
                    this.inputType = inputType
                    fullBenchmarkResults.addAll(benchmarkResults)
                    this.focusGroups.addAll(focusGroups)


                    if (!focusGroups.contains(currentFocusedGroup)) {
                        currentFocusedGroup = FOCUS_GROUP_ALL
                    }


                    avgOfCount = benchmarkResults
                        .flatMap {
                            it.blockRows.map { blockRow ->
                                blockRow.fullData.map { fullData ->
                                    fullData.value.size
                                }
                            }
                        }.flatten().takeIf { it.isNotEmpty() }?.min() ?: -1


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

                    val autoGroupMapSize = chartsBundle?.groupMap?.autoGroupMap?.size ?: 0
                    val wordColorMapSize = chartsBundle?.groupMap?.wordColorMap?.size ?: 0
                    isAutoGroupButtonVisible = autoGroupMapSize != wordColorMapSize
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


    // timestamp eg : 2024-06-29 11:30:46.641
    val fullTimestampRegex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}".toRegex()

    // compact timestamp eg: 11:30:46.865
    val compactTimestampRegex = "\\d{2}:\\d{2}:\\d{2}\\.\\d{3}".toRegex()

    val logLevelRegex = "^(I|D|E|W|V)  ".toRegex()

    /**
     * this is a custom logic to filter out android junk logs (personal)
     */
    private fun filterOutAndroidJunkLog(data: String): String {
        return data.split("\n")
            .filterNot { line ->
                // line removal
                line.contains("PROCESS ENDED", ignoreCase = false) ||
                        line.contains("PROCESS STARTED", ignoreCase = false)
            }.joinToString(separator = "\n") {
                // line manipulation
                var line = it.replace(fullTimestampRegex, "").trimStart()
                line = line.replace(compactTimestampRegex, "").trimStart()
                if (line.startsWith("System.out ")) {
                    line = line.replace("System.out ", "").trimStart()
                }
                line = line.replace(logLevelRegex, "").trimStart()
                line = when {
                    line.contains("startup type is: cold") -> {
                        "startup type is: cold"
                    }

                    line.contains("startup type is: warm") -> {
                        "startup type is: warm"
                    }

                    line.contains("startup type is: hot") -> {
                        "startup type is: hot"
                    }

                    else -> {
                        line
                    }
                }.trimStart()
                line
            }.also {
                println("QuickTag: HomeViewModel:filterOutAndroidJunkLog: '$it'")
            }
    }

    private fun reset() {
        selectedBlockNameOne = null
        selectedBlockNameTwo = null
        blockNames.clear()
        chartsBundle = null
        summaries.clear()
        bestAggSummary = null
        worstAggSummary = null
        avgOfCount = -1
        updateSummary()
    }

    private fun calcAggSummary() {
        val isGeneric = inputType == InputType.GENERIC
        val newAggSums = mutableListOf<AggSummary>()
        for (blockNameOuter in blockNames) {
            for (blockNameInner in blockNames) {
                if (blockNameOuter == blockNameInner) {
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
        } else {
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

    fun onTestNameSelected(newTestName: String) {
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

    fun onFocusGroupSelected(focusGroup: String) {
        currentFocusedGroup = focusGroup
        onFormChanged(form)
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

    fun onShareClicked(formData: FormData) {

        // We need to split the input into chunk of 30,000 character
        val chunks = formData.data.chunked(30000)
        // since we're using the millis as Random see 10 should be enough 🤔
        val shareKey = RandomString.getRandomString(10)

        // Submit the Google form to insert the data to google sheet
        for ((index, chunk) in chunks.withIndex()) {
            try {
                googleFormRepo.insert(
                    shareKey,
                    index,
                    chunk
                )
                // TODO: RESTART POINT : WHY IT SHOWS FAILED TO EXECUTE ERROR?  https://support.hcltechsw.com/csm?id=kb_article&sysparm_article=KB0106294
            }catch (e : Throwable){
                e.printStackTrace()
                println("QuickTag: HomeViewModel:onShareClicked: I AM THE ERROR: ${e.message}")
                throw Exception("Couldn't store data. '${e.message}'")
            }
        }

        // show a success message to user that the URL has been copied to the clipboard
        println("QuickTag: HomeViewModel:onShareClicked: Huhhaaa!!! shareKey: $shareKey")
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

    fun onDotClicked(focusGroup: String) {
        if (focusGroups.contains(focusGroup)) {
            onFocusGroupSelected(focusGroup)
        }
    }

}

data class AggSummary(
    val blockOneName: String,
    val blockTwoName: String,
    val sumOfGreen: Int,
    val sumOfRed: Int
)
