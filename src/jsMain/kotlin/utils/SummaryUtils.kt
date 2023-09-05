package utils

import components.Summary
import components.SummaryNode
import core.GroupMap
import core.SupportedMetrics
import model.Chart
import kotlin.math.absoluteValue

object SummaryUtils {

    fun prepareSummary(
        isGeneric: Boolean,
        groupMap: GroupMap,
        chart: Chart,
        onSummaryReady: (summary: Summary) -> Unit,
        onSummaryFailed: (reason: String) -> Unit,
    ) {
        try {
            val totalGroups = groupMap.wordColorMap.size
            if (totalGroups != 2) {
                println("impossible to summarize. expected group size = 2, but found $totalGroups -> ${groupMap.wordColorMap}")
                return
            }
            val combinedMap = mutableMapOf<String, List<Float>>()
            val words = groupMap.wordColorMap.keys.toList()
            println("words : $words")
            for (word in words) {
                combinedMap[word] =
                    chart.dataSets.filterKeys { it.startsWith(word) }.values.map { it.values.toFloatArray() }
                        .let { arrays ->
                            // Sum
                            val newArray = mutableListOf<Float>().apply {
                                repeat(chart.dataSets.values.first().size) {
                                    add(0f)
                                }
                            }
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
            println("combinedMap : ${combinedMap.map { it.value.toList() }}")

            val summaryNodes = mutableListOf<SummaryNode>()
            val segments = chart.dataSets.values.first().keys.toList()
            println("segments: $segments")
            repeat(segments.size) { index ->
                val segment = segments[index]
                val after = combinedMap[words[1]]?.get(index) ?: 0f
                val before = combinedMap[words[0]]?.get(index) ?: 0f
                println("before : '$before' -> after: '$after'")
                val diff = "${(after - before).asDynamic().toFixed(2)}".toFloat()
                val percDiff =
                    "${(((before - after) / before) * 100).asDynamic().toFixed(2)}".toFloat().absoluteValue

                val resultWord = if (diff > 0) "worse" else "better"
                val symbol = if (diff > 0) "+" else ""
                val emoji = if (diff > 0) "❌" else "✅"

                summaryNodes.add(
                    SummaryNode(
                        isGeneric = isGeneric,
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
            val title = if (isGeneric) {
                "📊Summary"
            } else {
                val metricConfig = SupportedMetrics.values().find { it.key == chart.label }
                    ?: error("Unsupported metric name `${chart.label}`")
                "${metricConfig.emoji} ${metricConfig.title}"
            }
            onSummaryReady(Summary(title = title, summaryNodes))
        } catch (e: Throwable) {
            e.printStackTrace()
            onSummaryFailed(e.message ?: "unknown")
        }
    }
}