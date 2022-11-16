import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import chartjs.Type
import core.jso
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun ChartUi(chartData: ChartData) {
    H3 { Text(chartData.label) }

    // Charts
    Canvas(
        attrs = {
            style {
                width(100.percent)
                maxWidth(700.px)
            }
        }
    ) {
        DisposableEffect(chartData) {
            val dataSets = mutableListOf<Chart.ChartDataSets>()
            for ((key, value) in chartData.dataSets) {
                dataSets.add(
                    jso {
                        label = key
                        data = value
                        borderColor = arrayOf(
                            "rgba(255, 99, 132, 1)",
                            "rgba(54, 162, 235, 1)",
                            "rgba(255, 206, 86, 1)",
                            "rgba(75, 192, 192, 1)",
                            "rgba(153, 102, 255, 1)",
                            "rgba(255, 159, 64, 1)"
                        )
                        borderWidth = 3
                    }
                )
            }
            val chart = Chart(scopeElement, jso {
                type = Type.line
                this.data = jso {
                    labels = arrayOf("P50", "P90", "P95", "P99")
                    datasets = dataSets.toTypedArray()

                }
                this.options = jso {
                    scales = jso {
                        y = jso {
                            beginAtZero = true
                        }
                    }
                    plugins = jso {
                        title = jso {
                            display = true
                            text = "HELLLOOOO"
                        }
                    }
                }
            })
            onDispose {
                chart.destroy()
            }
        }
    }

}