package components

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*

@Composable
fun SavedBenchmarksDropDown(
    savedBenchmarks: List<SavedBenchmarkNode>,
    onLoadBenchmarkClicked: (SavedBenchmarkNode) -> Unit,
    onDeleteBenchmarkClicked: (SavedBenchmarkNode) -> Unit
) {

    if (savedBenchmarks.isEmpty()) {
        return
    }

    var selectedBenchmark by remember(savedBenchmarks) { mutableStateOf(savedBenchmarks.first()) }


    Label(
        forId = "savedBenchmarks",
        attrs = {
            classes("form-label")
        }
    ) {
        Text("Load Benchmark :")
    }

    Div(
        attrs = {
            classes("form-group")
        }
    ) {
        Div(
            attrs = {
                classes("row")
            }
        ) {

            Div(
                attrs = {
                    classes("col")
                }
            ) {
                Select(
                    attrs = {
                        classes("form-select")
                        id("savedBenchmarks")
                        onChange {
                            it.value?.let { benchmarkKey ->
                                selectedBenchmark =
                                    savedBenchmarks.find { benchmark -> benchmark.key == benchmarkKey }!!
                            }
                        }
                    }
                ) {
                    for (savedBenchmark in savedBenchmarks) {
                        Option(
                            value = savedBenchmark.key,
                            attrs = {
                                if (savedBenchmark.key == selectedBenchmark.key) {
                                    selected()
                                }
                            }
                        ) {
                            Text(savedBenchmark.key)
                        }
                    }
                }
            }

            Div(
                attrs = {
                    classes("col")
                }
            ) {
                Button(
                    attrs = {
                        classes("btn", "btn-primary")
                        style {
                            marginRight(10.px)
                        }
                        onClick {
                            onLoadBenchmarkClicked(selectedBenchmark)
                        }
                        type(ButtonType.Button)
                    }
                ) {
                    Text("LOAD")
                }

                Button(
                    attrs = {
                        classes("btn", "btn-danger")
                        onClick {
                            onDeleteBenchmarkClicked(selectedBenchmark)
                        }
                        type(ButtonType.Button)
                    }
                ) {
                    Text("DELETE")
                }
            }
        }
    }
}