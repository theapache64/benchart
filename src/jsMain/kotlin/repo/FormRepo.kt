package repo

import kotlinx.browser.window
import model.FormData

interface FormRepo {
    fun saveFormData(newForm: FormData)
    fun getFormData(): FormData?
}

class FormRepoImpl : FormRepo {
    companion object {
        private const val KEY_AUTO_FORM_INPUT = "auto_form_input"
    }

    override fun saveFormData(newForm: FormData) {
        window.localStorage.setItem(KEY_AUTO_FORM_INPUT, newForm.data)
    }

    override fun getFormData(): FormData? {
        val data = window.localStorage.getItem(KEY_AUTO_FORM_INPUT) ?: return null
        return FormData(data)
    }

}