import java.util.Scanner

fun main() {
    val randomEng = 0..100
    println("How many?: ")
    val count = Scanner(System.`in`).nextInt()
    repeat(count){
        println("""
        frameDurationCpuMs   P50   ${randomEng.random()},   P90  ${randomEng.random()},   P95  ${randomEng.random()},   P99  ${randomEng.random()}
        frameOverrunMs   P50   ${randomEng.random()},   P90  ${randomEng.random()},   P95  ${randomEng.random()},   P99  ${randomEng.random()}
    """.trimIndent())
        println("-------------------------")
    }
}