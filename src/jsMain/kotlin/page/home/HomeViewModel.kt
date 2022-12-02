package page.home

import BenchmarkResult
import androidx.compose.runtime.*
import components.SummaryNode
import core.GroupMap
import core.toCharts
import kotlinx.browser.window
import model.*
import kotlin.math.absoluteValue

@Stable
class HomeViewModel {

    companion object {
        private const val ERROR_GENERIC = "Something went wrong!"

        // keys
        val KEY_SAVED_BENCHMARKS = "savedBenchmarks"

        private fun updateSummary(
            groupMap: GroupMap,
            chartData: ChartData,
            onSummaryReady: (summaryList: List<SummaryNode>) -> Unit,
            onSummaryFailed: () -> Unit,
        ) {
            try {
                val totalGroups = groupMap.wordColorMap.size
                if (totalGroups != 2) {
                    onSummaryFailed()
                    return
                }
                val combinedMap = mutableMapOf<String, Array<Float>>()
                val words = groupMap.wordColorMap.keys.toList()
                for (word in words) {
                    combinedMap[word] = chartData
                        .dataSets
                        .filterKeys { it.startsWith(word) }
                        .values
                        .map { it.toFloatArray() }
                        .let { arrays ->
                            // Sum
                            val newArray = arrayOf(0f, 0f, 0f, 0f)
                            for (array in arrays) {
                                for (i in newArray.indices) {
                                    newArray[i] = newArray[i] + array[i]
                                }
                            }
                            // Average
                            for (i in newArray.indices) {
                                newArray[i] = newArray[i] / arrays.size
                            }
                            newArray
                        }
                }
                for ((key, value) in combinedMap) {
                    println(key)
                    println(value)
                }
                val summaryList = mutableListOf<SummaryNode>()
                repeat(4) { index ->
                    val segment = when (index) {
                        0 -> "P50"
                        1 -> "P90"
                        2 -> "P95"
                        3 -> "P99"
                        else -> error("No segment found for index '$index'")
                    }

                    val after = combinedMap[words[1]]?.get(index) ?: 0f
                    val before = combinedMap[words[0]]?.get(index) ?: 0f
                    val diff = "${(after - before).asDynamic().toFixed(2)}".toFloat()
                    val percDiff =
                        "${(((before - after) / before) * 100).asDynamic().toFixed(2)}".toFloat().absoluteValue

                    val resultWord = if (diff > 0) "worse" else "better"
                    val symbol = if (diff > 0) "+" else ""
                    val emoji = if (diff > 0) "❌" else "✅"

                    summaryList.add(
                        SummaryNode(
                            emoji = emoji,
                            segment = segment,
                            label = words[1],
                            percentage = percDiff,
                            stateWord = resultWord,
                            diff = diff,
                            diffSymbol = symbol,
                            after = "${after.asDynamic().toFixed(2)}".toFloat(),
                            before = "${before.asDynamic().toFixed(2)}".toFloat()
                        )
                    )
                }
                onSummaryReady(summaryList)
            } catch (e: Throwable) {
                e.printStackTrace()
                onSummaryFailed()
            }
        }
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

    var isAutoGroupEnabled by mutableStateOf(false)
        private set

    var durationSummary = mutableStateListOf<SummaryNode>()
        private set

    var overrunSummary = mutableStateListOf<SummaryNode>()
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
        updateSummary(
            groupMap = charts.groupMap,
            chartData = charts.frameDurationChart,
            onSummaryReady = { summaryNodes ->
                durationSummary.clear()
                durationSummary.addAll(summaryNodes)
            },
            onSummaryFailed = {
                durationSummary.clear()
            }
        )

        val frameOverrunChart = charts.frameOverrunChart
        if (frameOverrunChart != null && frameOverrunChart.dataSets.isNotEmpty()) {
            updateSummary(
                groupMap = charts.groupMap,
                chartData = frameOverrunChart,
                onSummaryReady = { summaryNodes ->
                    overrunSummary.clear()
                    overrunSummary.addAll(summaryNodes)
                },
                onSummaryFailed = {
                    overrunSummary.clear()
                }
            )
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
        val savedBenchmark = if(savedBenchmarksString==null){
            // Creating first saved benchmark
            SavedBenchmarks(items = arrayOf())
        }else{
            JSON.parse(savedBenchmarksString)
        }

        // Appending new benchmark
        val newList = savedBenchmark.items.toMutableList().apply {
            add(
                BenchmarkNode(
                    key = bName,
                    value = formData.data
                )
            )
        }
        savedBenchmark.items = newList.toTypedArray()
        savedBenchmarksString = JSON.stringify(savedBenchmark)
        window.localStorage.setItem(KEY_SAVED_BENCHMARKS, savedBenchmarksString)
        // TODO: show some toast ?
    }

}
