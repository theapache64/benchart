package page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import components.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text
import kotlin.Error

@Composable
fun HomePage(
    viewModel: HomeViewModel = remember { HomeViewModel() }
) {
    Div(
        attrs = {
            classes("container-fluid")
        }
    ) {

        // Heading
        Heading()

        // Error
        if (viewModel.errorMsg.isNotBlank()) {
            Error(viewModel.errorMsg)
        }


        // Main
        Div(attrs = {
            classes("row")
            style {
                padding(40.px)
            }
        }) {
            Div(attrs = {
                classes("col-lg-4")
            }) {
                FormUi(
                    onFormChanged = { form ->
                        viewModel.onFormChanged(form)
                    },
                )
            }

            val hasOverrunMs = viewModel.charts?.frameOverrunChart?.dataSets?.isNotEmpty() ?: false

            Div(
                attrs = {
                    classes("col-lg-8")
                }
            ) {

                if (viewModel.isEditableTitleEnabled) {
                    EditableTitle()
                } else {
                    H3(
                        attrs = {
                            onDoubleClick {
                                viewModel.onTitleDoubleClicked()
                            }
                        }
                    ) {
                        Text("🖥 Output")
                    }
                }

                // 🧪 Test Names
                Div(
                    attrs = {
                        classes("row")
                    }
                ) {
                    TestNames(
                        testNames = viewModel.testNames,
                        onTestNameChanged = { newTestName ->
                            viewModel.onTestNameChanged(newTestName)
                        }
                    )
                }


                // 📊 Charts
                Div(
                    attrs = {
                        classes("row")
                    }
                ) {
                    // 📊 duration chart
                    Div(attrs = {
                        classes(
                            if (hasOverrunMs) {
                                "col-lg-6"
                            } else {
                                "col-lg-12"
                            },
                        )
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

                    // 📊 overrun chart
                    if (hasOverrunMs) {
                        Div(attrs = {
                            classes(
                                "col-lg-6"
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
    }
}