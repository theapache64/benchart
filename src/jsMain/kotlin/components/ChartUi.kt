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
                                "rgba(54, 162, 235, 1)",    // Blue
                                "rgba(255, 159, 64, 1)",    // Orange
                                "rgba(75, 192, 192, 1)",    // Teal
                                "rgba(153, 102, 255, 1)",   // Purple
                                "rgba(255, 205, 86, 1)",    // Yellow
                                "rgba(22, 160, 133, 1)",    // Green
                                "rgba(142, 68, 173, 1)",    // Deep Purple
                                "rgba(230, 126, 34, 1)",    // Burnt Orange
                                "rgba(52, 152, 219, 1)",    // Light Blue
                                "rgba(46, 204, 113, 1)",    // Emerald
                                "rgba(155, 89, 182, 1)",    // Amethyst
                                "rgba(241, 196, 15, 1)",    // Sun Yellow
                                "rgba(127, 140, 141, 1)",   // Asphalt
                                "rgba(26, 188, 156, 1)",    // Turquoise
                                "rgba(201, 203, 207, 1)",   // Grey
                                "rgba(211, 84, 0, 1)",      // Pumpkin
                                "rgba(41, 128, 185, 1)",    // Ocean Blue
                                "rgba(39, 174, 96, 1)"      // Nephritis
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