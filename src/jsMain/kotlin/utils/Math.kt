package utils

import page.home.ConfidenceIntervals
import kotlin.math.sqrt



fun Collection<Float>.calculateErrorMargins(): ConfidenceIntervals {
    if (this.isEmpty()) {
        return ConfidenceIntervals(
            mean = 0f,
            marginOf68p3 = 0f,
            marginOf90 = 0f,
            marginOf95 = 0f,
            marginOf99 = 0f,
            percentageMarginOf68p3 = 0f,
            percentageMarginOf90 = 0f,
            percentageMarginOf95 = 0f,
            percentageMarginOf99 = 0f,
            sampleSize = 0,
            standardDeviation = 0f
        )
    }

    val mean = this.average().toFloat()
    val sampleSize = this.size
    val stdDev = this.populationStandardDeviation()

    val standardError = stdDev / sqrt(sampleSize.toFloat())

    // Calculate absolute margins of error for different confidence levels
    val margin68p3 = standardError   // 68.3% confidence
    val margin90 = standardError * 1.645f  // 90% confidence
    val margin95 = standardError * 1.96f   // 95% confidence
    val margin99 = standardError * 2.576f  // 99% confidence

    // Calculate percentage margins relative to mean
    // Avoid division by zero if mean is 0
    val percentMargin68p3 = if (mean != 0f) (margin68p3 / mean) * 100f else 0f
    val percentMargin90 = if (mean != 0f) (margin90 / mean) * 100f else 0f
    val percentMargin95 = if (mean != 0f) (margin95 / mean) * 100f else 0f
    val percentMargin99 = if (mean != 0f) (margin99 / mean) * 100f else 0f

    return ConfidenceIntervals(
        mean = mean,
        marginOf68p3 = margin68p3,
        marginOf90 = margin90,
        marginOf95 = margin95,
        marginOf99 = margin99,
        percentageMarginOf68p3 = percentMargin68p3,
        percentageMarginOf90 = percentMargin90,
        percentageMarginOf95 = percentMargin95,
        percentageMarginOf99 = percentMargin99,
        sampleSize = sampleSize,
        standardDeviation = stdDev
    )
}

private fun Collection<Float>.populationStandardDeviation(): Float {
    if (this.isEmpty()) return 0f

    val mean = this.average()
    val sumSquaredDiffs = this.sumOf {
        val diff = it - mean
        (diff * diff).toDouble()
    }
    val variance = sumSquaredDiffs / this.size
    return sqrt(variance).toFloat()
}