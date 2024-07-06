package components

import Chart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import chartjs.Type
import core.GroupMap
import jso
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.maxHeight
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun ChartUi(
    isColorMapEnabled: Boolean,
    groupMap: GroupMap,
    chartModel: model.Chart,
    onDotClicked : (focusGroup : String) -> Unit
) {
    H3 { Text("${chartModel.emoji} ${chartModel.label}") }

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
        DisposableEffect(chartModel, isColorMapEnabled) {
            val dataSets = mutableListOf<Chart.ChartDataSets>()
            for ((legend, values) in chartModel.dataSets) {

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
                val chartLabels = chartModel.dataSets.values.flatMap { it.keys }.toSet().toTypedArray()
                this.data = jso {
                    labels = chartLabels
                    datasets = dataSets.toTypedArray()

                }
                this.options = jso {
                    plugins = jso {
                        title = jso {
                            display = true
                        }
                    }
                    scales = jso {
                        y = jso {
                            beginAtZero = true
                        }
                    }
                    onClick = { event: dynamic, elements: Array<dynamic> ->
                        if (elements.isNotEmpty()) {
                            val element = elements[0]
                            val datasetIndex = element.datasetIndex
                            val index = element.index
                            val focusGroup = chartLabels[index as Int]
                            onDotClicked(focusGroup)
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