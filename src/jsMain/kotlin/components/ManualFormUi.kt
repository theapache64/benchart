package components

import IS_INJECT_DUMMY
import ManualFormData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.dom.*

@Composable
fun ManualFormUi(
    onFormUpdated: (forms: List<ManualFormData>) -> Unit
) {
    // Menu
    val forms = remember {
        mutableStateMapOf<Int, ManualFormData>().apply {
            if (IS_INJECT_DUMMY) {
                put(
                    0, ManualFormData(
                        title = "before",
                        data = """
                    frameDurationCpuMs   P50   14.7,   P90   45.8,   P95   65.7,   P99  179.7
                    frameOverrunMs   P50   -5.3,   P90   65.4,   P95  109.1,   P99  200.6
                """.trimIndent()
                    )
                )

                put(
                    1, ManualFormData(
                        title = "after",
                        data = """
                    frameDurationCpuMs   P50   10.7,   P90   40.8,   P95   50.7,   P99  100.7
                    frameOverrunMs   P50   -5.3,   P90   65.4,   P95  109.1,   P99  100.6
                """.trimIndent()
                    )
                )
            } else {
                put(
                    0, ManualFormData(
                        title = "",
                        data = ""
                    )
                )
            }

            onFormUpdated(this.values.toList())
        }
    }
    MenuBar(onAddSlotClicked = {
        forms[forms.size] = ManualFormData(
            title = "",
            data = ""
        )
    })

    // Slots
    repeat(forms.size) { index ->
        FormUi(
            form = forms[index]!!,
            onFormChanged = {
                forms[index] = it
                onFormUpdated(forms.values.toList())
            }
        )
    }
}


@Composable
fun FormUi(
    form: ManualFormData,
    onFormChanged: (form: ManualFormData) -> Unit
) {

    Div(attrs = {
        classes("row")
    }) {
        // Input
        Div(attrs = {
            classes("col-md-12")
        }) {
            Div(attrs = {
                classes("row")
            }) {
                H3 {
                    Text("Input")
                }

            }


            Form {
                Div(attrs = {
                    classes("form-group")
                }) {
                    Input(
                        type = InputType.Text,
                    ) {
                        classes("form-control")
                        value(form.title)
                        placeholder(value = "Label")
                        onInput { textInput ->
                            val newForm = form.copy(title = textInput.value)
                            onFormChanged(newForm)
                        }
                    }
                }

                Div(attrs = {
                    classes("form-group")
                }) {
                    TextArea(
                        value = form.data
                    ) {
                        classes("form-control")
                        placeholder(value = "Benchmark data goes here (separate each benchmark by a blank line)")
                        rows(12)
                        onInput { textInput ->
                            val newForm = form.copy(data = textInput.value)
                            onFormChanged(newForm)
                        }
                    }
                }
            }
        }
    }
}