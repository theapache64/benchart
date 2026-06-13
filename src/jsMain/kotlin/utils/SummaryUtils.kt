package utils

import components.Summary
import components.SummaryNode
import core.BenchmarkResult.Companion.FOCUS_GROUP_ALL
import core.MetricUnit
import core.getMetricEmoji
import model.Chart
import kotlin.math.absoluteValue

object SummaryUtils {

    private fun getMetricTitle(label: String): String {
        return label
    }

    private val highIsGoodMetricRegex = arrayOf(
        "frameCount",
        "gfxFrameTotalCount",
        "batteryDiffMah",
        "batteryEndMah",
        "batteryStartMah",
    ).joinToString(separator = "|", prefix = "(", postfix = ")").toRegex()

    fun getSummaryOrThrow(
        currentFocusedGroup: String,
        isGeneric: Boolean,
        chart: Chart,
        selectedBlockNameOne: String?,
        selectedBlockNameTwo: String?,
    ): Summary? {
        if (selectedBlockNameOne == null || selectedBlockNameTwo == null) {
            println("blank block name detected. skipping summary")
            return null
        }

        val combinedMap = mutableMapOf<String, List<Float>>()
        val words = listOf(selectedBlockNameOne, selectedBlockNameTwo)
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

        val title = if (isGeneric) {
            if (currentFocusedGroup == FOCUS_GROUP_ALL) {
                "📊 $selectedBlockNameOne vs $selectedBlockNameTwo"
            } else {
                "📊 ${chart.label}"
            }
        } else {
            "${getMetricEmoji(chart.label)} ${getMetricTitle(chart.label)}"
        }

        repeat(segments.size) { index ->
            val segment = segments[index]
            val after = combinedMap[words[1]]?.get(index) ?: 0f
            val before = combinedMap[words[0]]?.get(index) ?: 0f
            println("before : '$before' -> after: '$after'")
            val diff = "${(after - before).asDynamic().toFixed(2)}".toFloat()
            var percDiff = if (before == 0f) {
                after * 100
            } else {
                (((before - after) / before) * 100)
            }
            percDiff = "${percDiff.asDynamic().toFixed(2)}".toFloat().absoluteValue as Float

            val isHighGoodMetric =
                highIsGoodMetricRegex.containsMatchIn(title) || title.endsWith("%Count") // TODO: Make this come from a user preference

            val diffThreshold = 16
            val absDiff = diff.absoluteValue
            val resultWord = if (diff == 0f) {
                "equally"
            } else if (isHighGoodMetric == (diff > 0)) {
                if (absDiff >= diffThreshold) {
                    "better"
                } else {
                    "nominal"
                }
            } else {
                if (absDiff <= diffThreshold) {
                    "degraded"
                } else {
                    "worse"
                }
            }
            val symbol = if (diff > 0) "+" else ""
            val emoji = if (diff > 0 == isHighGoodMetric) {
                "🟢"
            } else {
                if (absDiff <= diffThreshold) {
                    "🟠"
                } else {
                    "🔴"
                }
            }
            val badgeClass = when {
                diff == 0f -> "secondary"
                diff > 0 != isHighGoodMetric -> {
                    if (absDiff <= diffThreshold) {
                        "warning"
                    } else {
                        "danger"
                    }
                }

                else -> {
                    if(absDiff >= diffThreshold){
                        "success"
                    }else{
                        "info"
                    }
                }
            }
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
                    before = "${before.asDynamic().toFixed(2)}".toFloat(),
                    unit = getMetricUnit(title),
                    badgeClass = badgeClass,
                )
            )
        }


        return Summary(title = title, summaryNodes)
    }

    private fun getMetricUnit(title: String): MetricUnit? {
        return when {
            title.endsWith("Ms") -> MetricUnit.Ms
            title.endsWith("Mah") -> MetricUnit.Mah
            title.endsWith("Kb") -> MetricUnit.Kb
            title.endsWith("ViewCount") -> MetricUnit.View
            title.endsWith("Percent") -> MetricUnit.Percentage
            title.contains("frame", ignoreCase = true) && title.contains("count", ignoreCase = true) -> MetricUnit.Frame
            else -> null
        }
    }
}