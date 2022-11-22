import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.dom.*

@Composable
fun AutoFormUi(
    onFormChanged : (form : AutoFormData) -> Unit
) {
    var form by remember { mutableStateOf(AutoFormData(data = "")) }

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