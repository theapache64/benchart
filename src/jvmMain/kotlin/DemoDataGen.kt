import java.util.*

fun main() {
    startupTimeGen()
}

fun startupTimeGen() {
    val randomEng = 500..5000
    println("How many?: ")
    val count = Scanner(System.`in`).nextInt()
    repeat(count) {
        println(
            """
        timeToFullDisplayMs   min   ${randomEng.random()},   median  ${randomEng.random()},   max  ${randomEng.random()}
        timeToInitialDisplayMs   min   ${randomEng.random()},   median  ${randomEng.random()},   max  ${randomEng.random()}
    """.trimIndent()
        )
        println("-------------------------")
    }
}

fun frameTimeGen() {
    val randomEng = 0..100
    println("How many?: ")
    val count = Scanner(System.`in`).nextInt()
    repeat(count) {
        println(
            """
        frameDurationCpuMs   P50   ${randomEng.random()},   P90  ${randomEng.random()},   P95  ${randomEng.random()},   P99  ${randomEng.random()}
        frameOverrunMs   P50   ${randomEng.random()},   P90  ${randomEng.random()},   P95  ${randomEng.random()},   P99  ${randomEng.random()}
    """.trimIndent()
        )
        println("-------------------------")
    }
}