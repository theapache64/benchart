package page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import components.*
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import repo.BenchmarkRepoImpl
import repo.FormRepoImpl
import kotlin.Error

@Composable
fun HomePage(
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
                                    classes("col-lg-6")
                                }) {
                                    ChartUi(
                                        isColorMapEnabled = viewModel.isAutoGroupEnabled,
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
