package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text


@Composable
fun MenuBar(onAddSlotClicked: () -> Unit) {
    Div(
        attrs = {
            classes("row")
        }
    ) {
        Div(
            attrs = {
                classes("col-md-12", "text-right")
            }
        ) {
            Button(
                attrs = {
                    onClick {
                        onAddSlotClicked()
                    }
                    classes("btn", "btn-primary")
                }
            ) {
                Text("Add slot")
            }
        }
    }
}

