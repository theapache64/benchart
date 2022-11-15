import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        Div(
            attrs = {
                classes("container")
            }
        ) {

            // Heading
            Heading()

            /*// Menu
            var slotCount by remember { mutableStateOf(1) }
            MenuBar(
                onAddSlotClicked = {
                    slotCount++
                }
            )

            // Slots
            repeat(slotCount) {
                Slot()
            }*/
        }
    }
}



