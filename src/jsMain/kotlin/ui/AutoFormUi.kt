package ui

import AutoFormData
import androidx.compose.runtime.*
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.dom.*


private val defaultAutoForm = """
                - Before 1
                # first line will be treated as title of the block
                  special chars will be stripped from the title
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.5,   P90   20.8,   P95   25.4,   P99   47.4
                frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
                Traces: Iteration 0 1 2 3 4
                
                ## Before 2
                # line breaks are used to separate the block
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.4,   P90   20.7,   P95   24.4,   P99   51.2
                frameOverrunMs   P50   -6.5,   P90    5.4,   P95   15.0,   P99   60.3
                Traces: Iteration 0 1 2 3 4
                
                After 1
                you can include whatever text you want anywhere you want
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.6,   P90   21.8,   P95   27.5,   P99   49.4
                the order doesn't matter
                frameOverrunMs   P50   -6.2,   P90    7.3,   P95   19.5,   P99   61.7
                Traces: Iteration 0 1 2 3 4
                
                > After 2
                HomeScrollBenchmark_scrollTest
                frameDurationCpuMs   P50   13.8,   P90   21.9,   P95   27.3,   P99   53.4
                see.. am some random text
                frameOverrunMs   P50   -5.7,   P90    7.4,   P95   22.4,   P99   63.2
                Traces: Iteration 0 1 2 3 4
            """.trimIndent()

private const val KEY_AUTO_FORM_INPUT = "auto_form_input"

@Composable
fun AutoFormUi(
    onFormChanged: (form: AutoFormData) -> Unit
) {
    var form by remember {
        mutableStateOf(
            AutoFormData(
                data = window.localStorage.getItem(KEY_AUTO_FORM_INPUT) ?: defaultAutoForm
            )
        )
    }

    LaunchedEffect(Unit) {
        onFormChanged(form)
    }

    Div(
        attrs = {
            classes("row")
        }
    ) {
        // Input
        Div(
            attrs = {
                classes("col-md-12")
            }
        ) {
            Div(
                attrs = {
                    classes("row")
                }
            ) {
                H3 {
                    Text("Input")
                }

            }


            Form {

                Div(attrs = {
                    classes("Autoroup")
                }) {
                    TextArea(
                        value = form.data
                    ) {
                        classes("form-control")
                        placeholder(value = "Benchmark data")
                        rows(30)
                        onInput { textInput ->
                            form = form.copy(data = textInput.value)
                            window.localStorage.setItem(KEY_AUTO_FORM_INPUT, textInput.value)
                            onFormChanged(form)
                        }
                    }
                }
            }
        }
    }
}