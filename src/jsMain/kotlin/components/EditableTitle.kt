package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

@Composable
fun EditableTitle() {
    Div(
        attrs = {
            classes("row")
        }
    ) {
        Div(
            attrs = {
                classes("form-group")
            }
        ) {
            Label(
                forId = "customTitle",
                attrs = {
                    classes("form-label")
                }
            ) {
                Text("Title :")
            }
            Input(
                type = InputType.Text,
            ) {
                id("customTitle")
                classes("form-control")
                placeholder(value = "Custom title goes here")
                style {
                    fontSize(24.px)
                }
            }
        }
    }
}