package utils


import kotlin.js.Date
import kotlin.random.Random

/**
 * Created by theapache64 on 9/4/16.
 * and reused in 2024 :P
 */
object RandomString {
    private const val RANDOM_ENGINE = "0123456789AaBbCcDdEeFfGgHhIiJjKkLkMmNnOoPpQqRrSsTtUuVvWwXxYyZz"

    fun getRandomString(length: Int): String {
        val random = Random(Date().getMilliseconds())
        val apiKeyBuilder = StringBuilder()
        for (i in 0 until length) {
            apiKeyBuilder.append(RANDOM_ENGINE[random.nextInt(RANDOM_ENGINE.length)])
        }
        return apiKeyBuilder.toString()
    }
}