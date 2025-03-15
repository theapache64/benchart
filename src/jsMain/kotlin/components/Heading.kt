package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text


private const val version = "v25.03.16.0 (16 Mar 2025)"

@Composable
fun Heading() {
    Div(attrs = {
        classes("row")
    }) {
        Div(attrs = {
            classes("col-lg-12", "text-center")
            style {
                marginBottom(30.px)
                marginTop(30.px)
            }

        }) {
            H1(
                attrs = {
                    attr("data-bs-toggle", "tooltip")
                    attr("data-bs-placement", "top")
                    attr("title", version)
                }
            ) {
                Img(
                    src = "icons/apple-touch-icon.png",
                    attrs = {
                        style {
                            width(36.px)
                            marginRight(6.px)
                            marginTop((-8).px)
                        }
                    }
                )
                Text("BenChart")
            }
        }
    }
}