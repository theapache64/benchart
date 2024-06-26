package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.dom.*


@Composable
fun FocusGroups(
    focusGroups: List<String>,
    currentFocusGroup: String?,
    onFocusGroupSelected: (focusGroup: String) -> Unit
){
    if(focusGroups.isNotEmpty()){
        Div(
            attrs = {
                classes("form-group")
            }
        ) {
            Label(
                forId = "focusGroups",
                attrs = {
                    classes("form-label")
                }
            ) {
                Text("Focus Group :")
            }
            Select(
                attrs = {
                    classes("form-select")
                    id("focusGroups")
                    onInput {
                        it.value?.let { focusGroup ->
                            onFocusGroupSelected(focusGroup)
                        }
                    }
                }
            ) {
                for (focusGroup in focusGroups) {
                    Option(
                        value = focusGroup,
                        attrs = {
                            if (focusGroup == currentFocusGroup) {
                                selected()
                            }
                        }
                    ) {
                        Text(focusGroup)
                    }
                }
            }
        }
    }
}