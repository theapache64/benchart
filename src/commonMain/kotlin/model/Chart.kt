package model

import core.GroupMap

data class ChartsBundle(
    val groupMap: GroupMap,
    val charts: List<Chart>
)

data class Chart(
    val emoji: String,
    val label: String,
    // eg format:  (before1 -> map { p50 -> 20, p90 -> 30 })
    val dataSets: Map<String, Map<String, Float>>,
)



