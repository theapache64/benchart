package page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import components.AutoGroup
import components.ChartUi
import components.EditableTitle
import components.ErrorUi
import components.FormUi
import components.Heading
import components.SummaryContainer
import components.TestNameDetectionToggle
import components.TestNames
import core.InputType
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text
import repo.BenchmarkRepoImpl
import repo.FormRepoImpl

@Composable
fun HomePageUi(
    viewModel: HomeViewModel = remember { HomeViewModel(BenchmarkRepoImpl(), FormRepoImpl()) }
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
            ErrorUi(viewModel.errorMsg)
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
                    shouldSelectUnsaved = viewModel.shouldSelectUnsaved,
                    onFormChanged = viewModel::onFormChanged,
                    onSaveClicked = viewModel::onSaveClicked,
                    savedBenchmarks = viewModel.savedBenchmarks,
                    onSavedBenchmarkChanged = viewModel::onSavedBenchmarkChanged,
                    onLoadBenchmarkClicked = viewModel::onLoadBenchmarkClicked,
                    onDeleteBenchmarkClicked = viewModel::onDeleteBenchmarkClicked,
                )

                Br()
                Br()

                SummaryContainer(summaries = viewModel.summaries)
            }

            viewModel.chartsBundle?.charts?.takeIf { it.isNotEmpty() }?.let { fullChartsList ->
                val mainCharts = viewModel.chartsBundle ?: error("TSH")
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
                        Form {
                            Div(
                                attrs = {
                                    classes("row")
                                }
                            ) {

                                Div(
                                    attrs = {
                                        classes("col-md-2")
                                    }
                                ) {
                                    AutoGroup(
                                        isEnabled = viewModel.form.isAutoGroupEnabled,
                                        onButtonClicked = viewModel::onToggleAutoGroupClicked
                                    )
                                }

                                if (viewModel.inputType == InputType.NORMAL_BENCHMARK) {
                                    Div(
                                        attrs = {
                                            classes("col-md-2")
                                        }
                                    ) {
                                        TestNameDetectionToggle(
                                            isEnabled = viewModel.form.isTestNameDetectionEnabled,
                                            onButtonClicked = viewModel::onToggleTestNameDetectionClicked
                                        )
                                    }
                                }

                                if (viewModel.testNames.isNotEmpty()) {
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

                                }

                            }
                        }
                    }

                    Br()
                    val chunkedCharts = remember(fullChartsList) { fullChartsList.chunked(2) }


                    // 📊 Charts
                    for (charts in chunkedCharts) {
                        Div(
                            attrs = {
                                classes("row")
                            }
                        ) {
                            for (chart in charts) {
                                // 📊 duration chart
                                Div(attrs = {
                                    classes(chart.bsClass)
                                }) {
                                    ChartUi(
                                        isColorMapEnabled = viewModel.form.isAutoGroupEnabled,
                                        groupMap = mainCharts.groupMap,
                                        chart = chart
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
