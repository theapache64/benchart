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
        private const val KEY_IS_AUTO_GROUP_ENABLED = "is_auto_group_enabled"
    }

    override fun saveFormData(newForm: FormData) {
        window.localStorage.apply {
            setItem(KEY_AUTO_FORM_INPUT, newForm.data)
            setItem(KEY_IS_TEST_NAME_DETECTION_ENABLED, newForm.isTestNameDetectionEnabled.toString())
            setItem(KEY_IS_AUTO_GROUP_ENABLED, newForm.isAutoGroupEnabled.toString())
        }
    }

    override fun getFormData(): FormData? {
        val localStorage = window.localStorage
        val data = localStorage.getItem(KEY_AUTO_FORM_INPUT) ?: return null
        val isTestNameDetectionEnabled = localStorage.getItem(KEY_IS_TEST_NAME_DETECTION_ENABLED).toBoolean()
        val isAutoGroupEnabled = localStorage.getItem(KEY_IS_AUTO_GROUP_ENABLED).toBoolean()
        return FormData(data, isTestNameDetectionEnabled, isAutoGroupEnabled)
    }

}