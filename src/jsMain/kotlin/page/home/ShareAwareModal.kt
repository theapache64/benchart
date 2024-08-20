package page.home

import androidx.compose.runtime.Composable
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun ShareAwareModal(
    onShareClicked : () -> Unit
){
    Div(
        attrs = {
            id("shareAwareModal")
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
                        Text("Share")
                    }
                }

                Div(
                    attrs = {
                        classes("modal-body")
                    }
                ) {
                    P {
                        Text("""
                        Ahh..it looks like you're using the 'Share' feature for the first time.
                        Please be aware that the data you share will be visible to everyone.
                        Make sure your input doesn't contain any sensitive data.
                        
                        If you need private share, please vote for the feature below :)
                    """.trimIndent())
                    }
                }

                Div(
                    attrs = {
                        classes("modal-footer")
                    }
                ) {

                    Button(
                        attrs = {
                            classes("btn", "btn-dark")
                            style {
                                marginTop(10.px)
                            }

                            onClick {
                                window.open("https://forms.gle/KtPAA5LMeE8sak5h9", target = "_blank")
                            }
                            type(ButtonType.Button)
                        }
                    ) {
                        Text("Vote for Private Share")
                    }

                    Button(
                        attrs = {
                            classes("btn", "btn-danger")
                            attr("data-bs-dismiss", "modal")
                            style {
                                marginTop(10.px)
                            }
                            type(ButtonType.Button)
                        }
                    ) {
                        Text("Cancel Share")
                    }

                    Button(
                        attrs = {
                            classes("btn", "btn-success")
                            attr("data-bs-dismiss", "modal")
                            style {
                                marginTop(10.px)
                            }

                            onClick {
                                onShareClicked()
                            }
                            type(ButtonType.Button)
                        }
                    ) {
                        Text("Understood, Share!")
                    }
                }
            }
        }
    }
}