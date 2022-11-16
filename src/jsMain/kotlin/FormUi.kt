import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.dom.*

@Composable
fun FormsUi(
    onFormUpdated: (forms: List<FormData>) -> Unit
) {
    // Menu
    val forms = remember { mutableMapOf<Int, FormData>() }
    var formCount by remember { mutableStateOf(1) }
    println("Form count is $formCount")
    MenuBar(
        onAddSlotClicked = {
            formCount++
        }
    )

    // Slots
    repeat(formCount) { index ->
        FormUi(
            onFormChanged = {
                forms[index] = it

                onFormUpdated(forms.values.toList())
            }
        )
    }
}


@Composable
fun FormUi(
    onFormChanged: (form: FormData) -> Unit
) {
    var form by remember { mutableStateOf(FormData(title = "", data = "")) }

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
                    classes("form-group")
                }) {
                    Input(
                        type = InputType.Text,
                    ) {
                        classes("form-control")
                        value(form.title)
                        placeholder(value = "Label")
                        onInput { textInput ->
                            form = form.copy(title = textInput.value)
                            onFormChanged(form)
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
                        placeholder(value = "Benchmark data")
                        rows(12)
                        onInput { textInput ->
                            form = form.copy(data = textInput.value)
                            onFormChanged(form)
                        }
                    }
                }
            }
        }
    }
}