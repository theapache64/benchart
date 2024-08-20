package page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import components.Strong
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun SharedModal(
    shareUrl : String?,
    onCopyToClipboardClicked : (shareUrl : String?) -> Unit
){
    Div(
        attrs = {
            id("sharedModal")
            classes("modal", "fade")
        }
    ) {
        Div(
            attrs = {
                classes("modal-dialog", "modal-lg")
            }
        ) {
            Div(
                attrs = {
                    classes("modal-content")
                }
            ) {
                Div(
                    attrs = {
                        classes("modal-header")
                    }
                ) {
                    H4(
                        attrs = {
                            classes("modal-title")
                        }
                    ) {
                        Text("🚀 Share URL Ready!")
                    }
                }

                Div(
                    attrs = {
                        classes("modal-body")
                    }
                ) {
                    Div(
                        attrs = {
                            classes("alert","alert-success")
                        }
                    ) {
                        Strong {
                            if(shareUrl!=null){
                                Text(shareUrl)
                            }
                        }
                    }
                }

                Div(
                    attrs = {
                        classes("modal-footer")
                    }
                ) {

                    Button(
                        attrs = {
                            classes("btn", "btn-success")
                            attr("data-bs-dismiss", "modal")
                            style {
                                marginTop(10.px)
                            }

                            onClick {
                                onCopyToClipboardClicked(shareUrl)
                            }
                            type(ButtonType.Button)
                        }
                    ) {
                        Text("Copy to clipboard")
                    }
                }
            }
        }
    }
}