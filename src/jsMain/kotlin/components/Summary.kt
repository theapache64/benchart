package components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.HTMLSpanElement
import utils.DefaultValues.form

// P50 : After performed 25% better (-30ms)
class SummaryNode(
    val isGeneric : Boolean,
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

data class Summary(
    val title: String,
    val nodes: List<SummaryNode>
)

@Composable
fun SummaryContainer(
    summaries: List<Summary>,
) {
    SummarySelector()

    Br()

    for (summary in summaries) {
        key(summary.title) {
            SummaryUi(summary.title, summary.nodes)
            Br()
        }
    }
}

@Composable
fun SummarySelector(){

    Div (
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

                    }
                    type(ButtonType.Button)
                }
            ) {
                Text("BEST")
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

                    }
                    type(ButtonType.Button)
                }
            ) {
                Text("WORST")
            }
        }

    }

    Div(
        attrs = {
            classes("row")
        }
    ) {
        Div(
            attrs = {
                classes("col")
            }
        ) {
            Select(
                attrs = {
                    classes("form-select")
                    onInput {
                        // TODO
                    }
                }
            ) {
                for (testName in arrayOf("A", "B", "C")) {
                    Option(
                        value = testName,
                        attrs = {
                            /*if (testName == currentTestName) {
                                selected()
                            }*/
                        }
                    ) {
                        Text(testName)
                    }
                }
            }
        }

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


        Div(
            attrs = {
                classes("col")
            }
        ) {
            Select(
                attrs = {
                    classes("form-select")
                    onInput {
                        // TODO
                    }
                }
            ) {
                for (testName in arrayOf("A", "B", "C")) {
                    Option(
                        value = testName,
                        attrs = {
                            /*if (testName == currentTestName) {
                                selected()
                            }*/
                        }
                    ) {
                        Text(testName)
                    }
                }
            }
        }
    }


}

private open class ElementBuilderImplementation<TElement : Element>(private val tagName: String) : ElementBuilder<TElement> {
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
fun SummaryUi(title: String, summary: List<SummaryNode>) {
    Div(
        attrs = {
            classes("row")
        }
    ) {
        H3 { Text(title) }
        Ul {
            summary.forEach { node ->
                Li {
                    Text("${node.emoji} ")
                    // ${node.segment} : ${node.label} performed ${node.percentage}%
                    BoldText(
                        text = node.segment,
                        style = {
                            classes("text-capitalize")
                        }
                    )
                    Text(" : ")
                    BoldText(node.label)
                    Text(if(node.isGeneric) " looks " else " performed " )
                    BoldText("${node.percentage}% ")
                    val postfix = if(node.isGeneric) "" else "ms"
                    Span(
                        attrs = {
                            val badgeClass = when {
                                node.diff == 0f -> "secondary"
                                node.diff > 0 -> "danger"
                                else -> "success"
                            }
                            classes("badge", "bg-$badgeClass")

                            attr("data-bs-toggle", "tooltip")
                            attr("data-bs-placement", "top")

                            attr("title", "${node.before}$postfix to ${node.after}$postfix")
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
