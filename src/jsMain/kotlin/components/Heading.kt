package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*


@Composable
fun Heading() {
    Div(attrs = {
        classes("row")
        style {
            marginBottom(30.px)
            marginTop(30.px)
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