package components

import Chart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import chartjs.Type
import core.GroupMap
import jso
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun ChartUi(
    isColorMapEnabled: Boolean,
    groupMap: GroupMap,
    chart: model.Chart,
) {
    H3 { Text("${chart.emoji} ${chart.label}") }

    // Charts
    Canvas(
        attrs = {
            style {
                width(100.percent)
                maxWidth(100.percent)

                height(700.px)
                maxHeight(700.px)
            }
        }
    ) {
        DisposableEffect(chart, isColorMapEnabled) {
            val dataSets = mutableListOf<Chart.ChartDataSets>()
            for ((legend, values) in chart.dataSets) {

                dataSets.add(
                    jso {
                        label = legend
                        data = values.values.toTypedArray()
                        borderColor = if (isColorMapEnabled) {
                            groupMap.autoGroupMap[label]
                        } else {
                            arrayOf(
                                "rgba(255, 99, 132, 1)",
                                "rgba(54, 162, 235, 1)",
                                "rgba(255, 206, 86, 1)",
                                "rgba(75, 192, 192, 1)",
                                "rgba(153, 102, 255, 1)",
                                "rgba(255, 159, 64, 1)"
                            )
                        }
                        borderWidth = 3
                    }
                )
            }
            val chart = Chart(scopeElement, jso {
                type = Type.line
                this.data = jso {
                    labels = chart.dataSets.values.flatMap { it.keys }.toSet().toTypedArray().also {
                        println("labels: ${it.toList()}")
                    }
                    datasets = dataSets.toTypedArray()
                }
                this.options = jso {
                    plugins = jso {
                        title = jso {
                            display = true
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