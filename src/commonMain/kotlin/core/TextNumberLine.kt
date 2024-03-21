package core

private val digitRegex = "\\d+(.\\d+)?".toRegex()

data class TextNumberLine(
    val text: String,
    val number: Float
) {
    companion object {
        private val AVGIZER_REGEX = "\\(input count : .+\\)\$".toRegex()
        fun parse(iLine: String): TextNumberLine {
            // Quick support for https://theapache64.github.io/avgizer/
            val match = AVGIZER_REGEX.find(iLine)
            val line = if (match != null){
                iLine.replace(match.groupValues.first(), "")
            } else {
                iLine
            }

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