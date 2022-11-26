import org.jetbrains.compose.web.renderComposable
import page.home.HomePage

enum class Mode {
    AUTO,
    MANUAL
}

const val IS_INJECT_DUMMY = true

fun main() {

    initChartSettings()
    renderComposable(rootElementId = "root") {
        HomePage()
    }
}

private fun initChartSettings() {
    Chart.register(
        ArcElement,
        LineElement,
        BarElement,
        PointElement,
        BarController,
        BubbleController,
        DoughnutController,
        LineController,
        PieController,
        PolarAreaController,
        RadarController,
        ScatterController,
        CategoryScale,
        LinearScale,
        LogarithmicScale,
        RadialLinearScale,
        TimeScale,
        TimeSeriesScale,
        Decimation,
        Filler,
        Legend,
        Title,
        Tooltip,
        SubTitle
    )
}
