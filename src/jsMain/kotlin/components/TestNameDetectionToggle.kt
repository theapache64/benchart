package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*

@Composable
fun TestNameDetectionToggle(
    isEnabled: Boolean,
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
            forId = "testNameDetection",
            attrs = {
                classes("form-label")
            }
        ) {
            Text("Test Name Detection:")
        }
        Br()
        Button(
            attrs = {
                id("testNameDetection")
                classes("btn", if (isEnabled) "btn-success" else "btn-secondary")
                onClick {
                    onButtonClicked()
                }
                type(ButtonType.Button)
            }
        ) {
            Text(if (isEnabled) "ON" else "OFF")
        }
    }
}