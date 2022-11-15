import androidx.compose.runtime.*
import chartjs.Type
import core.BenchmarkResult
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

inline fun <T : Any> jso(): T = js("({})")

inline fun <T : Any> jso(builder: T.() -> Unit): T = jso<T>().apply(builder)

data class FormData(
    val title: String,
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

@Composable
fun FormsUi(
    onFormUpdated: (forms: List<FormData>) -> Unit
) {
    // Menu
    var formCount by remember { mutableStateOf(1) }
    MenuBar(
        onAddSlotClicked = {
            formCount++
        }
    )

    // Slots
    repeat(formCount) {
        FormUi(
            onFormChanged = {
                onFormUpdated(listOf(it))
            }
        )
    }
}

@Composable
fun MenuBar(onAddSlotClicked: () -> Int) {
    Div(
        attrs = {
            classes("row")
        }
    ) {
        Div(
            attrs = {
                classes("col-md-12", "text-right")
            }
        ) {
            Button(
                attrs = {
                    onClick {
                        onAddSlotClicked()
                    }
                }
            ) {
                Text("Add slot")
            }
        }
    }
}

@Composable
fun FormUi(
    onFormChanged : (form : FormData) -> Unit
){
    var form by remember { mutableStateOf(FormData(title = "", data = "")) }

    Div(
        attrs = {
            classes("row")
        }
    ) {
        // Input
        Div(
            attrs = {
                classes("col-md-12")
            }
        ) {
            Div(
                attrs = {
                    classes("row")
                }
            ) {
                H3 {
                    Text("Input")
                }

            }


            Form {
                Div(attrs = {
                    classes("form-group")
                }) {
                    Input(
                        type = InputType.Text,
                    ) {
                        classes("form-control")
                        value(form.title)
                        placeholder(value = "Title")
                        onInput { textInput ->
                            form = form.copy(title = textInput.value)
                            onFormChanged(form)
                        }
                    }
                }

                Div(attrs = {
                    classes("form-group")
                }) {
                    TextArea(
                        value = form.data
                    ) {
                        classes("form-control")
                        placeholder(value = "Benchmark data")
                        rows(12)
                        onInput { textInput ->
                            form = form.copy(data = textInput.value)
                            onFormChanged(form)
                        }
                    }
                }
            }
        }
    }
}

fun main() {

    Chart.register(
        ArcElement,
        LineElement,
        BarElement,
        PointElement,
        BarController,
        BubbleController,
        DoughnutController,
        LineController,
        PieController,
        PolarAreaController,
        RadarController,
        ScatterController,
        CategoryScale,
        LinearScale,
        LogarithmicScale,
        RadialLinearScale,
        TimeScale,
        TimeSeriesScale,
        Decimation,
        Filler,
        Legend,
        Title,
        Tooltip,
        SubTitle
    )
    renderComposable(rootElementId = "root") {
        Div(
            attrs = {
                classes("container")
            }
        ) {

            // Heading
            Heading()

            var charts by remember {
                mutableStateOf<Charts?>(null)
            }

            Div(attrs = {
                classes("row")
            }) {
                Div(attrs = {
                    classes("col-md-6")
                }) {
                    // Forms
                    FormsUi(
                        onFormUpdated = { forms ->
                            charts = forms.toCharts()
                        }
                    )
                }

                Div(attrs = {
                    classes("col-md-6")
                }) {
                    charts?.let { charts ->
                        // Rendering frameDurationMs
                        ChartUi(charts.frameDurationChart)

                        charts.frameOverrunChart?.let { frameOverrunChart ->
                            ChartUi(frameOverrunChart)
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun ChartUi(chartData: ChartData) {
    // Charts
    Canvas {
        DisposableEffect(Unit) {
            val dataSets = mutableListOf<Chart.ChartDataSets>()
            for ((key, value) in chartData.dataSets) {
                dataSets.add(
                    jso {
                        label = key
                        data = value
                    }
                )
            }
            val chart = Chart(scopeElement, jso {
                type = Type.line
                this.data = jso {
                    labels = arrayOf("P50", "P90", "P95", "P99")
                    datasets = dataSets.toTypedArray()
                }
            })
            onDispose {
                chart.destroy()
            }
        }
    }
}

private fun List<FormData>.toCharts(): Charts {
    val benchmarkResults = this.toBenchmarkResults()
    return benchmarkResults.toChartData()
}

private fun List<FormData>.toBenchmarkResults(): List<BenchmarkResult> {
    val benchmarkResults = mutableListOf<BenchmarkResult>()
    for (item in this) {
        benchmarkResults.add(BenchmarkResult.parse(item))
    }
    return benchmarkResults
}


private fun List<BenchmarkResult>.toChartData(): Charts {
    val frameDurationMap = mutableMapOf<String, Array<Float>>()
    val frameOverrunMap = mutableMapOf<String, Array<Float>>()
    for (item in this) {
        frameDurationMap[item.title] = item.frameDurationMs.values.toTypedArray()
        frameOverrunMap[item.title] = item.frameOverrunMs.values.toTypedArray()
    }

    return Charts(
        frameDurationChart = ChartData(
            label = BenchmarkResult.KEY_FRAME_DURATION_MS,
            dataSets = frameDurationMap
        ),
        frameOverrunChart = if (frameOverrunMap.isNotEmpty()) {
            ChartData(
                label = BenchmarkResult.KEY_FRAME_OVERRUN_MS,
                dataSets = frameOverrunMap
            )
        } else {
            null
        }
    )
}



