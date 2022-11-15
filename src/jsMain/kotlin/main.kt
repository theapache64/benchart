import androidx.compose.runtime.DisposableEffect
import chart.Type
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable

inline fun <T : Any> jso(): T = js("({})")

inline fun <T : Any> jso(builder: T.() -> Unit): T = jso<T>().apply(builder)

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

            Canvas(
                attrs = {
                    id("myChart")
                    style {
                        width(100.percent)
                        maxWidth(700.px)
                    }
                }
            ) {
                DisposableEffect(Unit) {
                    val chart = Chart("myChart", jso {
                        type = Type.line
                        this.data = jso {
                            labels = arrayOf("P50", "P90", "P95", "P99")
                            datasets = arrayOf(
                                jso {
                                    label = "frameDurationCpuMs 1"
                                    this.data = arrayOf(
                                        16.8,
                                        21.0,
                                        30.5,
                                        34.3,
                                    )
                                },
                                jso {
                                    label = "frameDurationCpuMs 2"
                                    this.data = arrayOf(
                                        26.8,
                                        31.0,
                                        40.5,
                                        54.3,
                                    )
                                }
                            )
                        }
                    })
                    onDispose {
                        chart.destroy()
                    }
                }
            }
        }
    }
}



