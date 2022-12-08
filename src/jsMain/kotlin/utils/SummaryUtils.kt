package utils

import components.SummaryNode
import core.GroupMap
import model.ChartData
import kotlin.math.absoluteValue

object SummaryUtils {

    fun prepareSummary(
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
                combinedMap[word] =
                    chartData.dataSets.filterKeys { it.startsWith(word) }.values.map { it.toFloatArray() }
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