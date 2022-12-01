package components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import org.jetbrains.compose.web.dom.*

// P50 : After performed 25% better (-30ms)
class SummaryNode(
    val emoji: String,
    val segment: String,
    val label: String,
    val percentage: Float,
    val stateWord: String,
    val diff: Float,
    val diffSymbol: String,
    val after: Float,
    val before: Float
)

@Composable
fun SummaryContainer(
    durationSummary: List<SummaryNode>,
    overrunSummary: List<SummaryNode>
) {
    if (durationSummary.isNotEmpty()) {
        key("durationSum") {
            Summary("⏱ Duration Summary", durationSummary)
        }
    }

    if (overrunSummary.isNotEmpty()) {
        key("overrunSum") {
            Summary("🏃🏻‍♂️ Overrun Summary", overrunSummary)
        }
    }
}

@Composable
fun Summary(title: String, summary: List<SummaryNode>) {
    Div(
        attrs = {
            classes("row")
        }
    ) {
        H3 { Text(title) }
        Ul {
            summary.forEach { node ->
                Li{
                    Text("${node.emoji} ${node.segment} : ${node.label} performed ${node.percentage}% ")
                    Span(
                        attrs = {
                            classes("badge", "bg-${if (node.diff > 0) "danger" else "success"}")

                            attr("data-bs-toggle", "tooltip")
                            attr("data-bs-placement", "top")
                            attr("title", "${node.before}ms to ${node.after}ms")
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
