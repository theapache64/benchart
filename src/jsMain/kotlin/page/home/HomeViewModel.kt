package page.home

import BenchmarkResult
import Charts
import FormData
import androidx.compose.runtime.*
import components.SummaryNode
import core.toCharts
import kotlin.math.roundToInt

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

    var isAutoGroupEnabled by mutableStateOf(false)
        private set

    var durationSummary by mutableStateOf(emptyList<SummaryNode>())
        private set

    var overrunSummary by mutableStateOf(emptyList<SummaryNode>())
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
            charts = filteredBenchmarkResult.toCharts().also {
                updateSummary(it)
            }

            errorMsg = ""
        } catch (e: Throwable) {
            e.printStackTrace()
            errorMsg = e.message ?: ERROR_GENERIC
        }
    }

    private fun updateSummary(charts: Charts) {
        // Calculating duration summary
        val totalGroups = charts.groupMap.wordColorMap.size
        if (totalGroups == 2) {
            val combinedMap = mutableMapOf<String, Array<Float>>()
            val words = charts.groupMap.wordColorMap.keys.toList()
            for (word in words) {
                combinedMap[word] = charts.frameDurationChart
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
            val summaryList = mutableListOf<SummaryNode>()
            for ((key, value) in combinedMap) {
                println(key)
                println(value)
            }
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
                val diff = (after - before).roundToInt()
                val percDiff = (((before - after) / before) * 100).roundToInt()
                println("diff $segment -> $diff ms")
                val resultWord = if (diff > 0) "worse" else "better"
                val symbol = if (diff > 0) "+" else ""
                // Sentence : P50 : After performed 25% better (-30ms)
                val emoji = if (diff > 0) "❌" else "✅"
                summaryList.add(
                    SummaryNode(
                        emoji = emoji,
                        segment = segment,
                        label = words[1],
                        percentage = percDiff,
                        stateWord = resultWord,
                        diff = diff,
                        diffSymbol = symbol
                    )
                )
                durationSummary = summaryList
            }
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
            errorMsg = ""
        } catch (e: Throwable) {
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
}