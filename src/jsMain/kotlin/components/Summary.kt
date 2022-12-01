package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.*

// P50 : After performed 25% better (-30ms)
data class SummaryNode(
    val emoji :String,
    val segment: String,
    val label: String,
    val percentage: Int,
    val stateWord: String,
    val diff: Int,
    val diffSymbol : String,
)

@Composable
fun Summary(title: String, summary: List<SummaryNode>) {
    Div {
        H3 { Text(title) }
        Ul {
            for (node in summary) {
                Li {
                    Text("${node.emoji} ${node.segment} : ${node.label} performed ${node.percentage}% ")
                    Span(
                        attrs = {
                            classes("label", "label-${if (node.diff > 0) "danger" else "success"}")
                        }
                    ) {
                        Text(node.stateWord)
                    }
                    Text(" (${node.diffSymbol}${node.diff}ms)")
                }
            }
        }
    }
}
