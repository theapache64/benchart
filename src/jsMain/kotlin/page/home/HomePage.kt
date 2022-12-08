package page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import components.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
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
                paddingLeft(40.px)
                paddingRight(40.px)
                paddingBottom(40.px)
            }
        }) {
            Div(attrs = {
                classes("col-lg-4")
            }) {
                FormUi(
                    form = viewModel.form,
                    onFormChanged = viewModel::onFormChanged,
                    onSaveClicked = viewModel::onSaveClicked,
                    savedBenchmarks = viewModel.savedBenchmarks,
                    onLoadBenchmarkClicked = viewModel::onLoadBenchmarkClicked,
                    onDeleteBenchmarkClicked = viewModel::onDeleteBenchmarkClicked,
                )

                Br()

                SummaryContainer(
                    durationSummary = viewModel.durationSummary,
                    overrunSummary = viewModel.overrunSummary
                )
            }

            if (!viewModel.charts?.frameDurationChart?.dataSets.isNullOrEmpty()) {
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

                    // 🧪 ToolBar
                    Div(
                        attrs = {
                            classes("row")
                        }
                    ) {
                        Form{
                            Div(
                                attrs = {
                                    classes("row")
                                }
                            ) {
                                Div(
                                    attrs = {
                                        classes("col-md-4")
                                    }
                                ) {
                                    TestNames(
                                        testNames = viewModel.testNames,
                                        onTestNameChanged = { newTestName ->
                                            viewModel.onTestNameChanged(newTestName)
                                        }
                                    )
                                }

                                Div(
                                    attrs = {
                                        classes("col-md-2")
                                    }
                                ) {
                                    AutoGroup(
                                        isAutoGroupEnabled = viewModel.isAutoGroupEnabled,
                                        onButtonClicked = viewModel::onToggleAutoGroupClicked
                                    )
                                }

                            }
                        }
                    }

                    Br()

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
                                        ChartUi(
                                            isColorMapEnabled = viewModel.isAutoGroupEnabled,
                                            groupMap = charts.groupMap,
                                            chartData = charts.frameDurationChart
                                        )
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
                                    ChartUi(
                                        viewModel.isAutoGroupEnabled,
                                        viewModel.charts!!.groupMap,
                                        frameOverrunChart
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
