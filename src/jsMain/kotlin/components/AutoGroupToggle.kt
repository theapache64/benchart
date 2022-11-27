package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

@Composable
fun AutoGroup(
    isAutoGroupEnabled: Boolean,
    onButtonClicked: () -> Unit
) {
    Div(
        attrs = {
            classes("form-group")
            style {
                marginLeft(10.px)
            }
        }
    ) {
        // 🖌 Color map

        Label(
            forId = "colorMap"
        ) {
            Text("Auto Group:")
        }

        Button(
            attrs = {
                id("colorMap")
                classes("btn", if (isAutoGroupEnabled) "btn-success" else "btn-default")
                onClick {
                    onButtonClicked()
                }
                type(ButtonType.Button)
            }
        ) {
            Text("🖌 + 💻")
        }
    }
}