import core.GroupMap

data class FormData(
    val data: String
)

data class ChartData(
    val emoji :String,
    val label: String,
    val dataSets: Map<String, Array<Float>>,
)

data class Charts(
    val groupMap: GroupMap,
    val frameDurationChart: ChartData,
    val frameOverrunChart: ChartData?
)