package utils

import components.Summary
import components.SummaryNode
import core.GroupMap
import core.SupportedMetrics
import model.Chart
import kotlin.math.absoluteValue

object SummaryUtils {

    fun prepareSummary(
        groupMap: GroupMap,
        chart: Chart,
        onSummaryReady: (summary: Summary) -> Unit,
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
                    chart.dataSets.filterKeys { it.startsWith(word) }.values.map { it.values.toFloatArray() }
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

            val summaryNodes = mutableListOf<SummaryNode>()
            val segments = chart.dataSets.values.first().keys.toList()
            repeat(segments.size) { index ->
                val segment = segments[index]
                val after = combinedMap[words[1]]?.get(index) ?: 0f
                val before = combinedMap[words[0]]?.get(index) ?: 0f
                val diff = "${(after - before).asDynamic().toFixed(2)}".toFloat()
                val percDiff =
                    "${(((before - after) / before) * 100).asDynamic().toFixed(2)}".toFloat().absoluteValue

                val resultWord = if (diff > 0) "worse" else "better"
                val symbol = if (diff > 0) "+" else ""
                val emoji = if (diff > 0) "❌" else "✅"

                summaryNodes.add(
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
            val metricConfig = SupportedMetrics.values().find { it.key == chart.label }
                ?: error("Unsupported metric name `${chart.label}`")
            val title = "${metricConfig.emoji} ${metricConfig.title}"
            onSummaryReady(Summary(title = title, summaryNodes))
        } catch (e: Throwable) {
            e.printStackTrace()
            onSummaryFailed()
        }
    }
}