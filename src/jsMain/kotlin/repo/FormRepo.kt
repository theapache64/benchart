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
        private const val KEY_IS_TEST_NAME_DETECTION_ENABLED = "is_test_name_detection_enabled"
    }

    override fun saveFormData(newForm: FormData) {
        window.localStorage.apply {
            setItem(KEY_AUTO_FORM_INPUT, newForm.data)
            setItem(KEY_IS_TEST_NAME_DETECTION_ENABLED, newForm.isTestNameDetectionEnabled.toString())
        }
    }

    override fun getFormData(): FormData? {
        val data = window.localStorage.getItem(KEY_AUTO_FORM_INPUT) ?: return null
        val isTestNameDetectionEnabled = window.localStorage.getItem(KEY_IS_TEST_NAME_DETECTION_ENABLED).toBoolean()
        return FormData(data, isTestNameDetectionEnabled)
    }

}