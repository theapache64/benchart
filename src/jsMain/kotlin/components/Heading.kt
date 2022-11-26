package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.overflow
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*


@Composable
fun Heading() {
    Div(attrs = {
        classes("row")
        style {
            marginBottom(30.px)
        }
    }) {
        Div(attrs = {
            classes("col-lg-12")
        }) {
            H1(attrs = {
                classes("text-center")
            }) { Text("📊 benchart") }
        }
    }
}