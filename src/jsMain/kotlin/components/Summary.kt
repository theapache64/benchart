package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.*



@Composable
fun Summary(title: String, summary: List<String>) {
    Div {
        H3 { Text(title) }
        Ul {
            for (node in summary) {
                Li {
                    Text(node)
                }
            }
        }
    }
}
