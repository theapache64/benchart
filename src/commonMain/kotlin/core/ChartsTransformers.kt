package core

import model.Chart
import model.ChartsBundle


fun List<BenchmarkResult>.toCharts(): ChartsBundle {
    val chartNames = this
        .map { result ->
            result.blockRows.map { dataPoint ->
                dataPoint.title
            }
        }
        .flatten()
        .toSet()

    val charts = mutableListOf<Chart>()
    for (chartName in chartNames) {
        // before1 -> {P50=40.5, P90=45.8, P95=60.4, P99=80.4}
        val dataSets = mutableMapOf<String, Map<String, Float>>()
        for (item in this) {
            dataSets[item.title] = item.blockRows.find { it.title == chartName }?.avgData ?: emptyMap()
        }

        charts.add(
            Chart(
                emoji = SupportedMetrics.values().find { it.key == chartName }?.emoji ?: "📊",
                label = chartName, // frameDurationCpuMs, frameOverrunMs, etc
                dataSets = dataSets
            )
        )
    }

    val groupMap = parseGroupMap(this, isGeneric = false)
    return ChartsBundle(
        groupMap = groupMap,
        charts = charts
    )
}


fun List<BenchmarkResult>.toGenericChart(): ChartsBundle {
    // Generic chart will be always 1
    val result = this.first()

    val chart = Chart(
        emoji = "📊",
        label = result.title,
        dataSets = mutableMapOf<String, Map<String, Float>>().apply {
            for(blockRow in result.blockRows){
                put(blockRow.title, blockRow.avgData)
            }
        },
        bsClass = "col-lg-12"
    )

    return ChartsBundle(
        groupMap = parseGroupMap(this, isGeneric = true),
        charts = listOf(
            chart
        )
    )
}


data class GroupMap(
    val autoGroupMap: Map<String, String>,
    val wordColorMap: Map<String, String>
)

fun parseGroupMap(
    benchmarkResults: List<BenchmarkResult>,
    isGeneric : Boolean
): GroupMap {
    val autoGroupMap = mutableMapOf<String, String>()
    val titles = if(isGeneric){
        benchmarkResults.flatMap { it.blockRows.map { blockRow -> blockRow.title } }
    }else {
        benchmarkResults.map { it.title }
    }
    println("titles: $titles -> ${benchmarkResults.map { it.blockRows }}")
    val wordColorMap = mutableMapOf<String, String>()
    // TODO: Add more colors
    val lineColors = mutableListOf(
        "rgba(255, 99, 132, 1)",
        "rgba(54, 162, 235, 1)",
        "rgba(255, 206, 86, 1)",
        "rgba(75, 192, 192, 1)",
        "rgba(153, 102, 255, 1)",
        "rgba(255, 159, 64, 1)",
    )
    for (title in titles) {
        val firstWord = title.split(" ")[0]
        val color = wordColorMap.getOrPut(firstWord) {

            if (lineColors.isEmpty()) {
                lineColors.add("rgba(${randomRgb()}, ${randomRgb()}, ${randomRgb()}, 1)")
            }

            val newColor = lineColors.first()
            lineColors.remove(newColor)
            newColor
        }
        autoGroupMap[title] = color
    }
    return GroupMap(
        autoGroupMap = autoGroupMap,
        wordColorMap = wordColorMap
    ).also {
        println("groupMap: $it")
    }
}

private fun randomRgb() = (0..255).random()
