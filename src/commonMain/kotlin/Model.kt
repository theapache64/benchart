
data class FormData(
    val data: String
)

data class ChartData(
    val label: String,
    val dataSets: Map<String, Array<Float>>
)

data class Charts(
    val frameDurationChart: ChartData,
    val frameOverrunChart: ChartData?
)