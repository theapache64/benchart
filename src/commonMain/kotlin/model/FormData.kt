package model

data class FormData(
    val data: String,
    val isTestNameDetectionEnabled : Boolean,
    val isAutoGroupEnabled : Boolean,
    val isLoading : Boolean,
    val loadingProgress : Int = 0
)
