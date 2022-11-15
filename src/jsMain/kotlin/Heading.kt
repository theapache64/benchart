import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text


@Composable
fun Heading() {
    Div(attrs = {
        classes("row")
    }) {
        Div(attrs = {
            classes("col-md-12")
        }) {
            H1(attrs = {
                classes("text-center")
            }) { Text("📊 benchart") }
        }
    }
}