import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import core.toCharts
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import ui.*

enum class Mode {
    AUTO,
    MANUAL
}

val IS_INJECT_DUMMY = true

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
                classes("container-fluid")
            }
        ) {

            // ui.Heading
            Heading()

            var mode by remember { mutableStateOf(Mode.AUTO) }
            ModeSwitcher(
                currentMode = mode,
                onModeChanged = { newMode ->
                    mode = newMode
                }
            )

            var charts by remember {
                mutableStateOf<Charts?>(null)
            }

            var errorMsg by remember {
                mutableStateOf("")
            }


            Div(attrs = {
                classes("row")
            }) {
                Div(attrs = {
                    classes("col-md-12")
                }) {
                    H4(attrs = {
                        classes("text-center")
                    }) {
                        if (errorMsg.isNotBlank()) {
                            Text("❌ $errorMsg")
                        }
                    }
                }
            }

            Div(attrs = {
                classes("row")
                style {
                    padding(40.px)
                }
            }) {
                Div(attrs = {
                    classes("col-md-4")
                }) {
                    when (mode) {
                        Mode.MANUAL -> {
                            // Form
                            ManualFormUi(
                                onFormUpdated = { forms ->
                                    println("Form updated : $forms")
                                    charts = forms.toCharts(
                                        onInvalidData = { exception ->
                                            errorMsg = exception?.message ?: ""
                                        }
                                    )
                                }
                            )
                        }

                        Mode.AUTO -> {
                            AutoFormUi(
                                onFormChanged = { form ->
                                    try {
                                        println("Form updated : $form")
                                        charts = form.toCharts()
                                        errorMsg = ""
                                    } catch (e: Throwable) {
                                        e.printStackTrace()
                                        errorMsg = e.message ?: "Something went wrong!"
                                    }
                                }
                            )
                        }
                    }
                }

                val hasOverrunMs = charts?.frameOverrunChart?.dataSets?.isNotEmpty() ?: false

                Div(attrs = {
                    classes(
                        if (hasOverrunMs) {
                            "col-md-4"
                        } else {
                            "col-md-8"
                        }
                    )
                    style {
                        if (mode == Mode.MANUAL) {
                            position(Position.Sticky)
                            top(0.px)
                        }
                    }
                }) {
                    charts?.let { charts ->
                        // Rendering frameDurationMs
                        charts.frameDurationChart.dataSets.isNotEmpty().let { hasData ->
                            if (hasData) {
                                ChartUi(charts.frameDurationChart)
                            }
                        }
                    }
                }

                if (hasOverrunMs) {
                    Div(attrs = {
                        classes(
                            "col-md-4"
                        )
                        style {
                            position(Position.Sticky)
                            top(0.px)
                        }
                    }) {
                        charts?.frameOverrunChart?.let { frameOverrunChart ->
                            ChartUi(frameOverrunChart)
                        }
                    }
                }
            }


        }
    }
}
