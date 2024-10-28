package components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Tbody
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Thead
import org.jetbrains.compose.web.dom.Tr

data class SDNode(
    val name: String,
    val population: List<Float>,
    val standardDeviation: Float,
    val errorMargin: Map<String, Float>,
    val min : Float,
    val median :Float,
    val max: Float,
    val percentiles : Map<String, Float>
)


@Composable
fun StandardDeviationUi(
    groupName: String,
    sdNodes: List<SDNode>
) {
    Table(
        attrs = {
            attr("border", "1")
            classes("table", "table-bordered")
        }
    ) {
        Thead {
            Tr {
                Th(
                    attrs = {
                        attr("rowspan", "2")
                    }
                ) {
                    Text(groupName)
                }
                Th(
                    attrs = {
                        attr("rowspan", "2")
                    }
                ) {
                    Text("Std. Deviation")
                }
                Th(
                    attrs = {
                        attr("colspan", "${sdNodes.firstOrNull()?.errorMargin?.size ?: 0}")
                        style {
                            textAlign("center")
                        }
                    }
                ) {
                    Text("Error Margin")
                }
            }
            Tr {
                sdNodes.firstOrNull()?.errorMargin?.keys?.forEach { emKey ->
                    key(emKey) {
                        Th { Text(emKey) }
                    }
                }
            }
        }
        Tbody {
            for (sdNode in sdNodes) {
                key(sdNode.toString()) {
                    Tr {
                        Td { Text(sdNode.name) }
                        Td(
                            attrs = {
                                title("${sdNode.population}")
                            }
                        ) {
                            A(
                                attrs = {
                                    href(
                                        "https://www.calculator.net/standard-deviation-calculator.html?numberinputs=${
                                            sdNode.population.joinToString(
                                                separator = ","
                                            )
                                        }&ctype=p&x=Calculate"
                                    )
                                    style {
                                        color(Color.black)
                                    }
                                }
                            ) {
                                Text(sdNode.standardDeviation.toString())
                            }
                        }

                        sdNode.errorMargin.values.forEach { margin ->
                            Td { Text("$margin%") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Stats(
    groupName: String,
    sdNodes: List<SDNode>
) {
    Table(
        attrs = {
            attr("border", "1")
            classes("table", "table-bordered")
        }
    ) {
        Thead {
            Tr {
                Th(
                    attrs = {
                        attr("rowspan", "2")
                    }
                ) {
                    Text(groupName)
                }
                Th(
                    attrs = {
                        attr("rowspan", "2")
                    }
                ) {
                    Text("Min")
                }
                Th(
                    attrs = {
                        attr("rowspan", "2")
                    }
                ) {
                    Text("Median")
                }

                Th(
                    attrs = {
                        attr("rowspan", "2")
                    }
                ) {
                    Text("Max")
                }
                Th(
                    attrs = {
                        attr("colspan", "${sdNodes.firstOrNull()?.percentiles?.size ?: 0}")
                        style {
                            textAlign("center")
                        }
                    }
                ) {
                    Text("Percentiles")
                }
            }
            Tr {
                sdNodes.firstOrNull()?.percentiles?.keys?.forEach { emKey ->
                    key(emKey) {
                        Th { Text(emKey) }
                    }
                }
            }
        }
        Tbody {
            for (sdNode in sdNodes) {
                key(sdNode.toString()) {
                    Tr {
                        Td { Text(sdNode.name) }
                        Td(
                            attrs = {
                                title("${sdNode.population.sorted()}")
                            }
                        ) {
                            Text(sdNode.min.toString())
                        }

                        Td(
                            attrs = {
                                title("${sdNode.population}")
                            }
                        ) {
                            Text(sdNode.median.toString())
                        }

                        Td(
                            attrs = {
                                title("${sdNode.population.sortedDescending()}")
                            }
                        ) {
                            Text(sdNode.max.toString())
                        }


                        sdNode.percentiles.values.forEach { percentile ->
                            Td { Text("$percentile") }
                        }
                    }
                }
            }
        }
    }
}