package ui

import Mode
import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.dom.*

@Composable
fun ModeSwitcher(
    currentMode: Mode,
    onModeChanged: (mode: Mode) -> Unit
) {
    println("Current mode is $currentMode")
    Div(
        attrs = {
            classes("row")
        }
    ) {
        Div(
            attrs = {
                classes("col-md-12", "text-center")
            }
        ) {
            Form {
                Mode.values().forEach { mode ->
                    Div(
                        attrs = {
                            classes("radio-inline")
                        }
                    ) {
                        Label {
                            Input(
                                type = InputType.Radio
                            ) {
                                name("mode")
                                checked(currentMode == mode)
                                onInput { newValue ->
                                    println("input changed ${newValue.value}")
                                    if (newValue.value) {
                                        onModeChanged(mode)
                                    }
                                }
                            }

                            Text(mode.name)
                        }
                    }
                }
            }
        }
    }
}

