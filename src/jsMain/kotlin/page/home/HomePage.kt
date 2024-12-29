package page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import components.AutoGroup
import components.ChartUi
import components.EditableTitle
import components.ErrorUi
import components.FocusGroups
import components.FormUi
import components.Heading
import components.StandardDeviationUi
import components.Stats
import components.SummaryContainer
import components.SummarySelector
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
import repo.GoogleFormRepoImpl
import repo.GoogleSheetRepoImpl
import repo.UserRepoImpl

@Composable
fun HomePageUi(
    viewModel: HomeViewModel = remember {
        HomeViewModel(
            BenchmarkRepoImpl(),
            FormRepoImpl(),
            GoogleFormRepoImpl(),
            GoogleSheetRepoImpl(),
            UserRepoImpl()
        )
    }
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
                    onShareClicked = viewModel::onShareClicked
                )

                Br()
                Br()

                SummaryContainer(
                    selector = {
                        println("block size ${viewModel.blockNames.size}")
                        if (viewModel.blockNames.size > 2) {
                            SummarySelector(
                                bestButtonLabel = "BEST (-${viewModel.bestAggSummary?.sumOfGreen}${viewModel.unit})",
                                worstButtonLabel = "WORST (+${viewModel.worstAggSummary?.sumOfRed}${viewModel.unit})",
                                onBestClicked = viewModel::onBestClicked,
                                onWorstClicked = viewModel::onWorstClicked,
                                blockNames = viewModel.blockNames,
                                selectedBlockNameOne = viewModel.selectedBlockNameOne,
                                selectedBlockNameTwo = viewModel.selectedBlockNameTwo,
                                onBlockOneSelected = viewModel::onBlockNameOneChanged,
                                onBlockTwoSelected = viewModel::onBlockNameTwoChanged
                            )
                        }
                    },
                    newSummaries = viewModel.summaries,
                    oldSummaries = viewModel.oldSummaries,
                    newAvgOfCount = viewModel.avgOfCount,
                    oldAvgOfCount = viewModel.oldAvgOfCount,
                    currentFocusedGroup = viewModel.currentFocusedGroup
                )
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

                                if (viewModel.isAutoGroupButtonVisible) {
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
                                }

                                if (viewModel.focusGroups.size > 1) {
                                    Div(
                                        attrs = {
                                            classes("col-md-4")
                                        }
                                    ) {
                                        FocusGroups(
                                            focusGroups = viewModel.focusGroups,
                                            currentFocusGroup = viewModel.currentFocusedGroup,
                                            onFocusGroupSelected = { focusGroup ->
                                                viewModel.onFocusGroupSelected(focusGroup)
                                            }
                                        )
                                    }
                                }

                                if (viewModel.inputType == InputType.MACRO_BENCHMARK) {
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
                                            onTestNameSelected = { newTestName ->
                                                viewModel.onTestNameSelected(newTestName)
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
                                        chartModel = chart,
                                        onDotClicked = viewModel::onDotClicked,
                                    )
                                }
                            }
                        }
                    }


                    Br()

                    // Summary
                    if(viewModel.sdNodes.isNotEmpty()){
                        Div(
                            attrs = {
                                classes("row")
                            }
                        ) {
                            Div(
                                attrs = {
                                    classes("col-md-6")
                                }
                            ) {
                                H3 {
                                    Text("📈 Standard Deviation: ")
                                }

                                StandardDeviationUi(viewModel.currentFocusedGroup, viewModel.sdNodes)
                            }

                            Div(
                                attrs = {
                                    classes("col-md-6")
                                }
                            ) {
                                H3 {
                                    Text("📈 Statistical Summary: ")
                                }

                                Stats(viewModel.currentFocusedGroup, viewModel.sdNodes)
                            }
                        }


                    }
                }
            }


        }
    }

    ShareAwareModal(
        onShareClicked = {
            viewModel.onAwarePublicShare()
        }
    )

    SharedModal(
        shareUrl = viewModel.sharedUrl,
        onCopyToClipboardClicked = { sharedUrl ->
            viewModel.onCopyToClipboardClicked(sharedUrl)
        }
    )
}
