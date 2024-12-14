package components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import model.FormData
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea

private val ALL_LOADING_MESSAGES = listOf(
    "Loading...",
    "Loading magic... This won't take long!",
    "Almost there! Great things are worth the wait.",
    "We're putting on the final touches. Stay with us!",
    "Looks like your network is slow 🤔... Hang tight!",
    "If this takes too long, try spinning in your chair!",
    "This is taking longer than usual. In the meantime, do 3 push-ups. Remember, health is wealth!",
    "Patience level: Jedi Master... Almost there!",
)


@Composable
fun FormUi(
    form: FormData,
    shouldSelectUnsaved: Boolean,
    savedBenchmarks: List<SavedBenchmarkNode>,
    onFormChanged: (form: FormData) -> Unit,
    onSaveClicked: (form: FormData) -> Unit,
    onShareClicked: (form: FormData) -> Unit,
    onSavedBenchmarkChanged: (key: String) -> Unit,
    onLoadBenchmarkClicked: (SavedBenchmarkNode) -> Unit,
    onDeleteBenchmarkClicked: (SavedBenchmarkNode) -> Unit,
) {


    LaunchedEffect(Unit) {
        onFormChanged(form)
    }

    H3 {
        Text("⌨️ Input")
    }

    Div {
        Form {

            key("inputForm") {

                SavedBenchmarksDropDown(
                    shouldSelectUnsaved = shouldSelectUnsaved,
                    savedBenchmarks = savedBenchmarks,
                    onSavedBenchmarkChanged = onSavedBenchmarkChanged,
                    onLoadBenchmarkClicked = onLoadBenchmarkClicked,
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

                if (form.isLoading) {
                    var progress by remember { mutableStateOf(20) }
                    LaunchedEffect(Unit) {
                        while (progress < 90) {
                            delay(200)
                            progress += 4
                        }
                    }

                    var loadingMsg by remember { mutableStateOf("") }
                    LaunchedEffect(Unit) {
                        val loadingMessages = ALL_LOADING_MESSAGES.asReversed()
                            .toMutableList()
                        while (loadingMessages.isNotEmpty()) {
                            loadingMsg = loadingMessages.removeAt(loadingMessages.lastIndex)
                            delay(5000)
                        }
                    }

                    Div(
                        attrs = {
                            classes("progress")
                            style {
                                marginTop(10.px)
                            }
                        }
                    ) {
                        Div(
                            attrs = {
                                classes("progress-bar", "progress-bar-striped", "progress-bar-animated", "bg-success")
                                attr("role", "progressbar")
                                attr("aria-valuenow", "$progress")
                                attr("aria-valuemin", "0")
                                attr("aria-valuemax", "100")
                                style {
                                    width(progress.percent)
                                }
                            }
                        ) {
                            Text(loadingMsg)
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

                Button(
                    attrs = {
                        classes("btn", "btn-dark", "float-end")
                        style {
                            marginTop(10.px)
                            marginRight(10.px)
                        }
                        if (form.data.isBlank()) {
                            attr("disabled", "true")
                        }
                        onClick {
                            onShareClicked(form)
                        }
                        type(ButtonType.Button)
                    }
                ) {
                    Text("🔗 SHARE")
                }
            }
        }
    }
}
