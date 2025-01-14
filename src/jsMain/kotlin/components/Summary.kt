package components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import core.BenchmarkResult.Companion.FOCUS_GROUP_ALL
import core.MetricUnit
import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Select
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement
import kotlin.math.absoluteValue

// P50 : After performed 25% better (-30ms)
class SummaryNode(
    val isGeneric: Boolean,
    val emoji: String,
    val segment: String,
    val label: String,
    val percentage: Float,
    val stateWord: String,
    val diff: Float,
    val diffSymbol: String,
    val after: Float,
    val before: Float,
    val badgeClass: String,
    val unit: MetricUnit?,
)

data class Summary(
    val title: String,
    val nodes: List<SummaryNode>
)

@Composable
fun SummaryContainer(
    selector: @Composable () -> Unit,
    oldSummaries: List<Summary>,
    newSummaries: List<Summary>,
    oldAvgOfCount: Int,
    newAvgOfCount: Int,
    currentFocusedGroup: String
) {

    selector()
    for ((index, summaries) in listOf(oldSummaries to oldAvgOfCount, newSummaries to newAvgOfCount).withIndex()) {
        key("summaries-$index") {
            if (summaries.first.isNotEmpty()) {
                Br()

                for (summary in summaries.first) {
                    key(summary.title + index) {
                        SummaryUi(summary.title, summaries.second, summary.nodes, currentFocusedGroup)
                        Br()
                    }
                }
            }
        }
    }
}

@Composable
fun SummarySelector(
    bestButtonLabel: String,
    worstButtonLabel: String,
    onBestClicked: () -> Unit,
    onWorstClicked: () -> Unit,
    blockNames: List<String>,
    selectedBlockNameOne: String?,
    selectedBlockNameTwo: String?,
    onBlockOneSelected: (String) -> Unit,
    onBlockTwoSelected: (String) -> Unit,
) {

    Div(
        attrs = {
            classes("row", "mb-3")
        }
    ) {

        Div(
            attrs = {
                classes("col-auto")
            }
        ) {
            // Best
            Button(
                attrs = {
                    classes("btn", "btn-outline-dark", "btn-sm")
                    onClick {
                        onBestClicked()
                    }
                    type(ButtonType.Button)
                }
            ) {
                Text(bestButtonLabel)
            }

        }
        Div(
            attrs = {
                classes("col-auto")
            }
        ) {
            // Best
            Button(
                attrs = {
                    classes("btn", "btn-outline-dark", "btn-sm")
                    onClick {
                        onWorstClicked()
                    }
                    type(ButtonType.Button)
                }
            ) {
                Text(worstButtonLabel)
            }
        }

    }

    Div(
        attrs = {
            classes("row")
        }
    ) {
        repeat(2) { index ->
            key("block-selector-$index") {
                Div(
                    attrs = {
                        classes("col")
                    }
                ) {
                    Select(
                        attrs = {
                            classes("form-select")
                            onInput {
                                it.value?.let { newBlockName ->
                                    if (index == 0) {
                                        // first block name
                                        onBlockOneSelected(newBlockName)
                                    } else {
                                        // second block name
                                        onBlockTwoSelected(newBlockName)
                                    }
                                }
                            }
                        }
                    ) {
                        for (blockName in blockNames) {
                            Option(
                                value = blockName,
                                attrs = {
                                    val selectedBlockName =
                                        if (index == 0) selectedBlockNameOne else selectedBlockNameTwo
                                    if (blockName == selectedBlockName) {
                                        selected()
                                    }
                                }
                            ) {
                                Text(blockName)
                            }
                        }
                    }
                }

                if (index == 0) {
                    Div(
                        attrs = {
                            classes("col-auto")
                        }
                    ) {
                        P {
                            Strong {
                                Text("vs")
                            }
                        }
                    }

                }
            }
        }

    }


}

private open class ElementBuilderImplementation<TElement : Element>(private val tagName: String) :
    ElementBuilder<TElement> {
    private val el: Element by lazy { document.createElement(tagName) }

    @Suppress("UNCHECKED_CAST")
    override fun create(): TElement = el.cloneNode() as TElement
}

private val Strong: ElementBuilder<HTMLElement> = ElementBuilderImplementation("strong")

@Composable
fun Strong(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null
) = TagElement(elementBuilder = Strong, applyAttrs = attrs, content = content)

@Composable
fun SummaryUi(title: String, avgOfCount: Int, summary: List<SummaryNode>, currentFocusGroup: String) {
    Div(
        attrs = {
            classes("row")
        }
    ) {
        H3 {
            Text(title)
            if (avgOfCount >= 1) {
                Small(
                    attrs = {
                        classes("text-muted")
                        style {
                            fontSize(18.px)
                        }
                    }
                ) {
                    if (avgOfCount == 1) {
                        if (currentFocusGroup != FOCUS_GROUP_ALL) {
                            Text(" (focused on '$currentFocusGroup')")
                        }
                    } else {
                        Text(" (average of $avgOfCount)")
                    }
                }
            }
        }
        Ul {
            summary.forEach { node ->
                Li {
                    Text("${node.emoji} ")
                    BoldText(
                        text = node.segment,
                        style = {
                            classes("text-capitalize")
                        }
                    )
                    Text(" : ")
                    BoldText(node.label)
                    Text(if (node.isGeneric) " looks " else " performed ")
                    if (node.diff != 0f) {
                        BoldText("${node.percentage}% ")
                    }
                    val postfix = node.getPostfix(node.diff)
                    val beforePostfix = node.getPostfix(node.before)
                    val afterPostfix = node.getPostfix(node.after)

                    Span(
                        attrs = {
                            classes("badge", "bg-${node.badgeClass}")

                            attr("data-bs-toggle", "tooltip")
                            attr("data-bs-placement", "top")

                            attr("title", if(node.diff ==0f) "both ${node.before}$beforePostfix" else "${node.before}$beforePostfix to ${node.after}$afterPostfix")
                        }
                    ) {
                        Text(node.stateWord)
                    }
                    Text(" (${node.diffSymbol}${node.diff}$postfix)")
                }
            }
        }
    }
}

fun SummaryNode.getPostfix(num: Float): String {
    return unit?.let { unit ->
        if (num.absoluteValue > 1) unit.plural else unit.singular
    } ?: ""
}
fun String.predictTitle(): String {
    var name = this
    if (name.endsWith("Mah", ignoreCase = false)) {
        name = name.replace("Mah", "")
    }
    if (name.endsWith("Ms", ignoreCase = false)) {
        name = name.replace("Ms", "")
    }
    return name.map {
        if (it.isUpperCase()) {
            " $it"
        } else {
            it.toString()
        }
    }.joinToString(separator = "").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

@Composable
private fun BoldText(
    text: String,
    style: (AttrsScope<HTMLSpanElement>.() -> Unit)? = null
) {
    Span(
        attrs = {
            style?.invoke(this)
            style {
                fontWeight("bold")
            }
        }
    ) {
        Text(text)
    }
}
