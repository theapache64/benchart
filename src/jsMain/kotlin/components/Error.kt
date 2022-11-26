package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun Error(message: String) {
    Div(attrs = {
        classes("row")
    }) {
        Div(attrs = {
            classes("col-lg-12")
        }) {
            H4(attrs = {
                classes("text-center")
            }) {
                Text("❌ $message")
            }
        }
    }
}