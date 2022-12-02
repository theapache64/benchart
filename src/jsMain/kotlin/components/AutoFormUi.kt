package components

import androidx.compose.runtime.*
import kotlinx.browser.window
import model.FormData
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*


private val defaultAutoForm = """
    - Before 1
    # first line will be treated as title of the block
      special chars will be stripped from the title
    HomeScrollBenchmark_scrollTest
    frameDurationCpuMs   P50   40.5,   P90   45.8,   P95   60.4,   P99   80.4
    frameOverrunMs   P50   -5.9,   P90    7.0,   P95   20.1,   P99   64.4
    Traces: Iteration 0 1 2 3 4

    ## Before 2
    # line breaks are used to separate the block
    HomeScrollBenchmark_scrollTest
    frameDurationCpuMs   P50   45.5,   P90   43.8,   P95   58.4,   P99   78.4
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
fun FormUi(
    onFormChanged: (form: FormData) -> Unit,
    onSaveClicked: (form: FormData) -> Unit
) {
    var form by remember {
        mutableStateOf(
            FormData(
                data = window.localStorage.getItem(KEY_AUTO_FORM_INPUT) ?: defaultAutoForm
            )
        )
    }

    LaunchedEffect(Unit) {
        onFormChanged(form)
    }

    H3 {
        Text("⌨️ Input")
    }

    Form {

        key("inputForm") {
            Div(
                attrs = {
                    classes("form-group")
                }
            ) {

                Label(
                    forId = "benchmark",
                    attrs = {
                        classes("form-label")
                    }
                ) {
                    Text("Benchmark :")
                }

                TextArea(
                    value = form.data
                ) {
                    id("benchmark")
                    classes("form-control")
                    placeholder(value = "Benchmark data")
                    rows(20)
                    onInput { textInput ->
                        form = form.copy(data = textInput.value)
                        window.localStorage.setItem(KEY_AUTO_FORM_INPUT, textInput.value)
                        onFormChanged(form)
                    }
                }

                Button(
                    attrs = {
                        classes("btn", "btn-dark", "float-end")
                        style {
                            marginTop(10.px)
                        }
                        if (form.data.isBlank()) {
                            attr("disabled", "true")
                        }
                        onClick {
                            onSaveClicked(form)
                        }
                        type(ButtonType.Button)
                    }
                ) {
                    Text("💾 SAVE")
                }
            }
        }
    }
}