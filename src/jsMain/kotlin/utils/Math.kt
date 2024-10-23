package utils

import page.home.ConfidenceIntervals
import kotlin.math.sqrt


fun Collection<Float>.calculateErrorMargins(): ConfidenceIntervals {
    if (this.isEmpty()) {
        return ConfidenceIntervals(0f, 0f, 0f, 0f, 0f, 0, 0f)
    }

    val mean = this.average().toFloat()
    val sampleSize = this.size
    val stdDev = this.populationStandardDeviation()

    val standardError = stdDev / sqrt(sampleSize.toFloat())

    // Calculate margins of error for different confidence levels
    val margin68p3 = standardError   // 68.3% confidence
    val margin90 = standardError * 1.645f  // 90% confidence
    val margin95 = standardError * 1.96f   // 95% confidence
    val margin99 = standardError * 2.576f  // 99% confidence

    return ConfidenceIntervals(
        mean = mean,
        marginOf68p3 = margin68p3,
        marginOf90 = margin90,
        marginOf95 = margin95,
        marginOf99 = margin99,
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