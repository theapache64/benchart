package page.home

import Mode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import components.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun HomePage(
    viewModel: HomeViewModel = remember { HomeViewModel() }
) {
    Div(
        attrs = {
            classes("container-fluid")
        }
    ) {

        // ui.Heading
        Heading()

        ModeSwitcher(
            currentMode = viewModel.mode,
            onModeChanged = { newMode ->
                viewModel.onModeChanged(newMode)
            }
        )

        Div(attrs = {
            classes("row")
        }) {
            Div(attrs = {
                classes("col-md-12")
            }) {
                H4(attrs = {
                    classes("text-center")
                }) {
                    if (viewModel.errorMsg.isNotBlank()) {
                        Text("❌ ${viewModel.errorMsg}")
                    }
                }
            }
        }

        Div(attrs = {
            classes("row")
            style {
                padding(40.px)
            }
        }) {
            Div(attrs = {
                classes("col-md-4")
            }) {
                when (viewModel.mode) {
                    Mode.MANUAL -> {
                        // Form
                        ManualFormUi(
                            onFormUpdated = { forms ->
                                println("Form updated : $forms")
                                viewModel.onManualFormChanged(forms)
                            }
                        )
                    }

                    Mode.AUTO -> {
                        AutoFormUi(
                            onFormChanged = { form ->
                                viewModel.onAutoFormChanged(form)
                            },
                            testNames = viewModel.testNames,
                            onTestNameChanged = { newTestName ->
                                viewModel.onTestNameChanged(newTestName)
                            }
                        )
                    }
                }
            }

            val hasOverrunMs = viewModel.charts?.frameOverrunChart?.dataSets?.isNotEmpty() ?: false

            Div(attrs = {
                classes(
                    if (hasOverrunMs) {
                        "col-md-4"
                    } else {
                        "col-md-8"
                    }
                )
                style {
                    if (viewModel.mode == Mode.MANUAL) {
                        position(Position.Sticky)
                        top(0.px)
                    }
                }
            }) {
                viewModel.charts?.let { charts ->
                    // Rendering frameDurationMs
                    charts.frameDurationChart.dataSets.isNotEmpty().let { hasData ->
                        if (hasData) {
                            ChartUi(charts.frameDurationChart)
                        }
                    }
                }
            }

            if (hasOverrunMs) {
                Div(attrs = {
                    classes(
                        "col-md-4"
                    )
                    style {
                        position(Position.Sticky)
                        top(0.px)
                    }
                }) {
                    viewModel.charts?.frameOverrunChart?.let { frameOverrunChart ->
                        ChartUi(frameOverrunChart)
                    }
                }
            }
        }


    }
}