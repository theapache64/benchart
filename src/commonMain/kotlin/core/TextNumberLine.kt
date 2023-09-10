package core

private val digitRegex = "\\d+(.\\d+)?".toRegex()

data class TextNumberLine(
    val text: String,
    val number: Float
) {
    companion object {
        fun parse(line: String): TextNumberLine {
            val number = digitRegex.findAll(line)
                .lastOrNull()
                ?.groupValues
                ?.firstOrNull()
                ?: error("$line doesn't match the regex '${digitRegex.pattern}'")
            val numberIndex = line.lastIndexOf(number)
            val newLine = line.substring(0, numberIndex)
            return TextNumberLine(newLine, number.toFloat())
        }
    }
}