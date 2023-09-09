package utils

import kotlinx.serialization.json.Json

object JsonUtils {
    val json = Json {
        ignoreUnknownKeys = true
    }
}