package components

import androidx.compose.runtime.*
import kotlinx.browser.window
import model.FormData
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*




@Composable
fun FormUi(
    form: FormData,
    shouldSelectUnsaved : Boolean,
    savedBenchmarks: List<SavedBenchmarkNode>,
    onFormChanged: (form: FormData) -> Unit,
    onSaveClicked: (form: FormData) -> Unit,
    onSavedBenchmarkChanged: (key :String) -> Unit,
    onLoadBenchmarkClicked: (SavedBenchmarkNode) -> Unit,
    onDeleteBenchmarkClicked: (SavedBenchmarkNode) -> Unit,
) {


    LaunchedEffect(Unit) {
        onFormChanged(form)
    }

    H3 {
        Text("⌨️ Input")
    }

    Form {

        key("inputForm") {

            SavedBenchmarksDropDown(
                shouldSelectUnsaved = shouldSelectUnsaved,
                savedBenchmarks = savedBenchmarks,
                onSavedBenchmarkChanged =onSavedBenchmarkChanged,
                onLoadBenchmarkClicked =  onLoadBenchmarkClicked,
                onDeleteBenchmarkClicked = onDeleteBenchmarkClicked
            )

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
                        onFormChanged(form.copy(data = textInput.value))
                    }
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
