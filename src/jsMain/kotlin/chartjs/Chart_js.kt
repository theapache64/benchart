@file:Suppress(
    "Unused",
    "NOTHING_TO_INLINE",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
    "INLINE_EXTERNAL_DECLARATION",
    "WRONG_BODY_OF_EXTERNAL_DECLARATION",
    "NESTED_EXTERNAL_DECLARATION"
)

@file:JsModule("chart.js")
@file:JsNonModule

import Chart.*
import chartjs.Type
import org.w3c.dom.*
import org.w3c.dom.events.*
import kotlin.js.Json

external val ArcElement: dynamic = definedExternally
external val LineElement: dynamic = definedExternally
external val BarElement: dynamic = definedExternally
external val PointElement: dynamic = definedExternally
external val BarController: dynamic = definedExternally
external val BubbleController: dynamic = definedExternally
external val DoughnutController: dynamic = definedExternally
external val LineController: dynamic = definedExternally
external val PieController: dynamic = definedExternally
external val PolarAreaController: dynamic = definedExternally
external val RadarController: dynamic = definedExternally
external val ScatterController: dynamic = definedExternally
external val CategoryScale: dynamic = definedExternally
external val LinearScale: dynamic = definedExternally
external val LogarithmicScale: dynamic = definedExternally
external val RadialLinearScale: dynamic = definedExternally
external val TimeScale: dynamic = definedExternally
external val TimeSeriesScale: dynamic = definedExternally
external val Decimation: dynamic = definedExternally
external val Filler: dynamic = definedExternally
external val Legend: dynamic = definedExternally
external val Title: dynamic = definedExternally
external val Tooltip: dynamic = definedExternally
external val SubTitle: dynamic = definedExternally

external class Chart {
    companion object {
        @Suppress("MemberVisibilityCanBePrivate")
        fun register(vararg elements: dynamic)
    }



    constructor(context: String, options: ChartConfiguration)
    constructor(context: CanvasRenderingContext2D, options: ChartConfiguration)
    constructor(context: HTMLCanvasElement, options: ChartConfiguration)

    var config: ChartConfiguration?
        get() = definedExternally
        set(value) = definedExternally
    var data: ChartData?
        get() = definedExternally
        set(value) = definedExternally
    fun destroy()
    var update: ((__0: ChartUpdateProps) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var render: ((__0: ChartRenderProps) -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var stop: (() -> Chart)?
        get() = definedExternally
        set(value) = definedExternally
    var resize: (() -> Chart)?
        get() = definedExternally
        set(value) = definedExternally
    var clear: (() -> Chart)?
        get() = definedExternally
        set(value) = definedExternally
    var toBase64Image: (() -> String)?
        get() = definedExternally
        set(value) = definedExternally
    var generateLegend: (() -> Any)?
        get() = definedExternally
        set(value) = definedExternally
    var getElementAtEvent: ((e: Any) -> dynamic)?
        get() = definedExternally
        set(value) = definedExternally
    var getElementsAtEvent: ((e: Any) -> Array<Any>)?
        get() = definedExternally
        set(value) = definedExternally
    var getElementsAtXAxis: ((e: Any) -> Array<Any>)?
        get() = definedExternally
        set(value) = definedExternally
    var getDatasetAtEvent: ((e: Any) -> Array<Any>)?
        get() = definedExternally
        set(value) = definedExternally
    var getDatasetMeta: ((index: Number) -> Meta)?
        get() = definedExternally
        set(value) = definedExternally
    var getVisibleDatasetCount: (() -> Number)?
        get() = definedExternally
        set(value) = definedExternally
    var isDatasetVisible: ((datasetIndex: Number) -> Boolean)?
        get() = definedExternally
        set(value) = definedExternally
    var setDatasetVisibility: ((datasetIndex: Number, visible: Boolean) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
    var ctx: CanvasRenderingContext2D?
        get() = definedExternally
        set(value) = definedExternally
    var canvas: HTMLCanvasElement?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var height: Number?
        get() = definedExternally
        set(value) = definedExternally

    var aspectRatio: Number?
        get() = definedExternally
        set(value) = definedExternally
    var options: ChartOptions?
        get() = definedExternally
        set(value) = definedExternally
    var chartArea: ChartArea?
        get() = definedExternally
        set(value) = definedExternally

    interface InteractionModeRegistry {
        @nativeGetter
        operator fun get(key: String): String? /* "x-axis" */

        @nativeSetter
        operator fun set(key: String, value: String /* "x-axis" */)
        var point: String /* "point" */
        var nearest: String /* "nearest" */
        var single: String /* "single" */
        var label: String /* "label" */
        var index: String /* "index" */
        var dataset: String /* "dataset" */
        var x: String /* "x" */
        var y: String /* "y" */
    }

    interface ChartArea {
        var top: Number
        var right: Number
        var bottom: Number
        var left: Number
    }

    interface ChartLegendItem {
        var text: String?
            get() = definedExternally
            set(value) = definedExternally
        var fillStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var hidden: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var index: Number?
            get() = definedExternally
            set(value) = definedExternally
        var lineCap: String? /* "butt" | "round" | "square" */
            get() = definedExternally
            set(value) = definedExternally
        var lineDash: Array<Number>?
            get() = definedExternally
            set(value) = definedExternally
        var lineDashOffset: Number?
            get() = definedExternally
            set(value) = definedExternally
        var lineJoin: String? /* "bevel" | "round" | "miter" */
            get() = definedExternally
            set(value) = definedExternally
        var lineWidth: Number?
            get() = definedExternally
            set(value) = definedExternally
        var strokeStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var pointStyle: String? /* "circle" | "cross" | "crossRot" | "dash" | "line" | "rect" | "rectRounded" | "rectRot" | "star" | "triangle" */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartLegendLabelItem : ChartLegendItem {
        var datasetIndex: Number?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartTooltipItem {
        var label: String?
            get() = definedExternally
            set(value) = definedExternally
        var value: String?
            get() = definedExternally
            set(value) = definedExternally
        var xLabel: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var yLabel: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var datasetIndex: Number?
            get() = definedExternally
            set(value) = definedExternally
        var index: Number?
            get() = definedExternally
            set(value) = definedExternally
        var x: Number?
            get() = definedExternally
            set(value) = definedExternally
        var y: Number?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartTooltipLabelColor {
        var borderColor: dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */
            get() = definedExternally
            set(value) = definedExternally
        var backgroundColor: dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartTooltipCallback {
        val beforeTitle: ((item: Array<ChartTooltipItem>, data: ChartData) -> dynamic)?
        val title: ((item: Array<ChartTooltipItem>, data: ChartData) -> dynamic)?
        val afterTitle: ((item: Array<ChartTooltipItem>, data: ChartData) -> dynamic)?
        val beforeBody: ((item: Array<ChartTooltipItem>, data: ChartData) -> dynamic)?
        val beforeLabel: ((tooltipItem: ChartTooltipItem, data: ChartData) -> dynamic)?
        val label: ((tooltipItem: ChartTooltipItem, data: ChartData) -> dynamic)?
        val labelColor: ((tooltipItem: ChartTooltipItem, chart: Chart) -> ChartTooltipLabelColor)?
        val labelTextColor: ((tooltipItem: ChartTooltipItem, chart: Chart) -> String)?
        val afterLabel: ((tooltipItem: ChartTooltipItem, data: ChartData) -> dynamic)?
        val afterBody: ((item: Array<ChartTooltipItem>, data: ChartData) -> dynamic)?
        val beforeFooter: ((item: Array<ChartTooltipItem>, data: ChartData) -> dynamic)?
        val footer: ((item: Array<ChartTooltipItem>, data: ChartData) -> dynamic)?
        val afterFooter: ((item: Array<ChartTooltipItem>, data: ChartData) -> dynamic)?
    }

    interface ChartAnimationParameter {
        var chartInstance: Any?
            get() = definedExternally
            set(value) = definedExternally
        var animationObject: Any?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartPoint {
        var x: dynamic /* Number? | String? | Date? | Moment? */
            get() = definedExternally
            set(value) = definedExternally
        var y: dynamic /* Number? | String? | Date? | Moment? */
            get() = definedExternally
            set(value) = definedExternally
        var r: Number?
            get() = definedExternally
            set(value) = definedExternally
        var t: dynamic /* Number? | String? | Date? | Moment? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartConfiguration {
        var type: Type?
            get() = definedExternally
            set(value) = definedExternally
        var data: ChartData?
            get() = definedExternally
            set(value) = definedExternally
        var options: ChartOptions?
            get() = definedExternally
            set(value) = definedExternally
        var plugins: Array<PluginServiceRegistrationOptions>?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartData {
        var labels: Array<dynamic /* String | Array<String> | Number | Array<Number> | Date | Array<Date> | Moment | Array<Moment> */>?
            get() = definedExternally
            set(value) = definedExternally
        var datasets: Array<ChartDataSets>?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface RadialChartOptions : ChartOptions {
        override var scale: RadialLinearScale?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartSize {
        var height: Number
        var width: Number
    }

    interface ChartOptions {
        var responsive: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var responsiveAnimationDuration: Number?
            get() = definedExternally
            set(value) = definedExternally
        var aspectRatio: Number?
            get() = definedExternally
            set(value) = definedExternally
        var maintainAspectRatio: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var events: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        val legendCallback: ((chart: Chart) -> String)?
        val onHover: ((event: MouseEvent, activeElements: Array<Any>) -> Any)?
        val onClick: ((event: MouseEvent, activeElements: Array<Any>) -> Any)?
        val onResize: ((newSize: ChartSize) -> Unit)?
        var title: ChartTitleOptions?
            get() = definedExternally
            set(value) = definedExternally
        var legend: ChartLegendOptions?
            get() = definedExternally
            set(value) = definedExternally
        var tooltips: ChartTooltipOptions?
            get() = definedExternally
            set(value) = definedExternally
        var hover: ChartHoverOptions?
            get() = definedExternally
            set(value) = definedExternally
        var animation: ChartAnimationOptions?
            get() = definedExternally
            set(value) = definedExternally
        var elements: ChartElementsOptions?
            get() = definedExternally
            set(value) = definedExternally
        var layout: ChartLayoutOptions?
            get() = definedExternally
            set(value) = definedExternally
        var scale: RadialLinearScale?
            get() = definedExternally
            set(value) = definedExternally
        var scales: dynamic /* ChartScales? | LinearScale? | LogarithmicScale? | TimeScale? */
            get() = definedExternally
            set(value) = definedExternally
        var showLines: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var spanGaps: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var cutoutPercentage: Number?
            get() = definedExternally
            set(value) = definedExternally
        var circumference: Number?
            get() = definedExternally
            set(value) = definedExternally
        var rotation: Number?
            get() = definedExternally
            set(value) = definedExternally
        var devicePixelRatio: Number?
            get() = definedExternally
            set(value) = definedExternally
        var plugins: ((String) -> Any?)?
            get() = definedExternally
            set(value) = definedExternally
        var defaultColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartFontOptions {
        var defaultFontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var defaultFontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var defaultFontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var defaultFontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartTitleOptions {
        var display: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var position: String? /* "left" | "right" | "top" | "bottom" | "chartArea" */
            get() = definedExternally
            set(value) = definedExternally
        var fullWidth: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var fontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var fontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var fontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var fontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var padding: Number?
            get() = definedExternally
            set(value) = definedExternally
        var lineHeight: dynamic /* Number? | String? */
            get() = definedExternally
            set(value) = definedExternally
        var text: dynamic /* String? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartLegendOptions {
        var align: String? /* "center" | "end" | "start" */
            get() = definedExternally
            set(value) = definedExternally
        var display: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var position: String? /* "left" | "right" | "top" | "bottom" | "chartArea" */
            get() = definedExternally
            set(value) = definedExternally
        var fullWidth: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        val onClick: ((event: MouseEvent, legendItem: ChartLegendLabelItem) -> Unit)?
        val onHover: ((event: MouseEvent, legendItem: ChartLegendLabelItem) -> Unit)?
        val onLeave: ((event: MouseEvent, legendItem: ChartLegendLabelItem) -> Unit)?
        var labels: ChartLegendLabelOptions?
            get() = definedExternally
            set(value) = definedExternally
        var reverse: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var rtl: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var textDirection: String?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartLegendLabelOptions {
        var boxWidth: Number?
            get() = definedExternally
            set(value) = definedExternally
        var fontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var fontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var fontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var fontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var padding: Number?
            get() = definedExternally
            set(value) = definedExternally
        val generateLabels: ((chart: Chart) -> Array<ChartLegendLabelItem>)?
        val filter: ((legendItem: ChartLegendLabelItem, data: ChartData) -> Any)?
        var usePointStyle: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartTooltipOptions {
        var axis: String? /* "x" | "y" | "xy" */
            get() = definedExternally
            set(value) = definedExternally
        var enabled: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var custom: ((tooltipModel: ChartTooltipModel) -> Unit)?
            get() = definedExternally
            set(value) = definedExternally
        var mode: String? /* "point" | "nearest" | "single" | "label" | "index" | "x-axis" | "dataset" | "x" | "y" */
            get() = definedExternally
            set(value) = definedExternally
        var intersect: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var backgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var titleAlign: String? /* "left" | "center" | "right" */
            get() = definedExternally
            set(value) = definedExternally
        var titleFontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var titleFontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var titleFontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var titleFontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var titleSpacing: Number?
            get() = definedExternally
            set(value) = definedExternally
        var titleMarginBottom: Number?
            get() = definedExternally
            set(value) = definedExternally
        var bodyAlign: String? /* "left" | "center" | "right" */
            get() = definedExternally
            set(value) = definedExternally
        var bodyFontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var bodyFontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var bodyFontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var bodyFontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var bodySpacing: Number?
            get() = definedExternally
            set(value) = definedExternally
        var footerAlign: String? /* "left" | "center" | "right" */
            get() = definedExternally
            set(value) = definedExternally
        var footerFontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var footerFontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var footerFontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var footerFontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var footerSpacing: Number?
            get() = definedExternally
            set(value) = definedExternally
        var footerMarginTop: Number?
            get() = definedExternally
            set(value) = definedExternally
        var xPadding: Number?
            get() = definedExternally
            set(value) = definedExternally
        var yPadding: Number?
            get() = definedExternally
            set(value) = definedExternally
        var caretSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var cornerRadius: Number?
            get() = definedExternally
            set(value) = definedExternally
        var multiKeyBackground: String?
            get() = definedExternally
            set(value) = definedExternally
        var callbacks: ChartTooltipCallback?
            get() = definedExternally
            set(value) = definedExternally
        val filter: ((item: ChartTooltipItem, data: ChartData) -> Boolean)?
        val itemSort: ((itemA: ChartTooltipItem, itemB: ChartTooltipItem, data: ChartData) -> Number)?
        var position: String?
            get() = definedExternally
            set(value) = definedExternally
        var caretPadding: Number?
            get() = definedExternally
            set(value) = definedExternally
        var displayColors: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var borderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderWidth: Number?
            get() = definedExternally
            set(value) = definedExternally
        var rtl: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var textDirection: String?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartTooltipModel {
        var afterBody: Array<String>
        var backgroundColor: String
        var beforeBody: Array<String>
        var body: Array<ChartTooltipModelBody>
        var bodyFontColor: String
        var bodyFontSize: Number
        var bodySpacing: Number
        var borderColor: String
        var borderWidth: Number
        var caretPadding: Number
        var caretSize: Number
        var caretX: Number
        var caretY: Number
        var cornerRadius: Number
        var dataPoints: Array<ChartTooltipItem>
        var displayColors: Boolean
        var footer: Array<String>
        var footerFontColor: String
        var footerFontSize: Number
        var footerMarginTop: Number
        var footerSpacing: Number
        var height: Number
        var labelColors: Array<ChartTooltipLabelColor>
        var labelTextColors: Array<String>
        var legendColorBackground: String
        var opacity: Number
        var title: Array<String>
        var titleFontColor: String
        var titleFontSize: Number
        var titleMarginBottom: Number
        var titleSpacing: Number
        var width: Number
        var x: Number
        var xAlign: String
        var xPadding: Number
        var y: Number
        var yAlign: String
        var yPadding: Number
        var _bodyAlign: String
        var _bodyFontFamily: String
        var _bodyFontStyle: String
        var _footerAlign: String
        var _footerFontFamily: String
        var _footerFontStyle: String
        var _titleAlign: String
        var _titleFontFamily: String
        var _titleFontStyle: String
    }

    interface ChartTooltipModelBody {
        var before: Array<String>
        var lines: Array<String>
        var after: Array<String>
    }

    interface ChartHoverOptions {
        var mode: String? /* "point" | "nearest" | "single" | "label" | "index" | "x-axis" | "dataset" | "x" | "y" */
            get() = definedExternally
            set(value) = definedExternally
        var animationDuration: Number?
            get() = definedExternally
            set(value) = definedExternally
        var intersect: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var axis: String? /* "x" | "y" | "xy" */
            get() = definedExternally
            set(value) = definedExternally
        val onHover: ((event: MouseEvent, activeElements: Array<Any>) -> Any)?
    }

    interface ChartAnimationObject {
        var currentStep: Number?
            get() = definedExternally
            set(value) = definedExternally
        var numSteps: Number?
            get() = definedExternally
            set(value) = definedExternally
        var easing: String? /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */
            get() = definedExternally
            set(value) = definedExternally
        val render: ((arg: Any) -> Unit)?
        val onAnimationProgress: ((arg: Any) -> Unit)?
        val onAnimationComplete: ((arg: Any) -> Unit)?
    }

    interface ChartAnimationOptions {
        var duration: Number?
            get() = definedExternally
            set(value) = definedExternally
        var easing: String? /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */
            get() = definedExternally
            set(value) = definedExternally
        val onProgress: ((chart: Any) -> Unit)?
        val onComplete: ((chart: Any) -> Unit)?
        var animateRotate: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var animateScale: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartElementsOptions {
        var point: ChartPointOptions?
            get() = definedExternally
            set(value) = definedExternally
        var line: ChartLineOptions?
            get() = definedExternally
            set(value) = definedExternally
        var arc: ChartArcOptions?
            get() = definedExternally
            set(value) = definedExternally
        var rectangle: ChartRectangleOptions?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartArcOptions {
        var angle: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var backgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderAlign: dynamic /* "center" | "inner" | Scriptable<String /* "center" | "inner" */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderWidth: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartLineOptions {
        var cubicInterpolationMode: dynamic /* "default" | "monotone" | Scriptable<String /* "default" | "monotone" */>? */
            get() = definedExternally
            set(value) = definedExternally
        var tension: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var backgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderWidth: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderCapStyle: dynamic /* String? | Scriptable<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderDash: dynamic /* Array<Any>? | Scriptable<Array<Any>>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderDashOffset: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderJoinStyle: dynamic /* String? | Scriptable<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var capBezierPoints: dynamic /* Boolean? | Scriptable<Boolean>? */
            get() = definedExternally
            set(value) = definedExternally
        var fill: dynamic /* "zero" | "top" | "bottom" | Boolean? | Scriptable<dynamic /* "zero" | "top" | "bottom" | Boolean */>? */
            get() = definedExternally
            set(value) = definedExternally
        var stepped: dynamic /* Boolean? | Scriptable<Boolean>? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartPointOptions {
        var radius: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointStyle: dynamic /* "circle" | "cross" | "crossRot" | "dash" | "line" | "rect" | "rectRounded" | "rectRot" | "star" | "triangle" | Scriptable<String /* "circle" | "cross" | "crossRot" | "dash" | "line" | "rect" | "rectRounded" | "rectRot" | "star" | "triangle" */>? */
            get() = definedExternally
            set(value) = definedExternally
        var rotation: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var backgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderWidth: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var hitRadius: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var hoverRadius: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var hoverBorderWidth: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartRectangleOptions {
        var backgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderWidth: dynamic /* Number? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderSkipped: dynamic /* String? | Scriptable<String>? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartLayoutOptions {
        var padding: dynamic /* ChartLayoutPaddingObject? | Number? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartLayoutPaddingObject {
        var top: Number?
            get() = definedExternally
            set(value) = definedExternally
        var right: Number?
            get() = definedExternally
            set(value) = definedExternally
        var bottom: Number?
            get() = definedExternally
            set(value) = definedExternally
        var left: Number?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface GridLineOptions {
        var display: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var circular: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var color: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderDash: Array<Number>?
            get() = definedExternally
            set(value) = definedExternally
        var borderDashOffset: Number?
            get() = definedExternally
            set(value) = definedExternally
        var lineWidth: dynamic /* Number? | Array<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var drawBorder: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var drawOnChartArea: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var drawTicks: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var tickMarkLength: Number?
            get() = definedExternally
            set(value) = definedExternally
        var zeroLineWidth: Number?
            get() = definedExternally
            set(value) = definedExternally
        var zeroLineColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var zeroLineBorderDash: Array<Number>?
            get() = definedExternally
            set(value) = definedExternally
        var zeroLineBorderDashOffset: Number?
            get() = definedExternally
            set(value) = definedExternally
        var offsetGridLines: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var z: Number?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ScaleTitleOptions {
        var display: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var labelString: String?
            get() = definedExternally
            set(value) = definedExternally
        var lineHeight: dynamic /* Number? | String? */
            get() = definedExternally
            set(value) = definedExternally
        var fontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var fontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var fontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var fontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var padding: dynamic /* ChartLayoutPaddingObject? | Number? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface TickOptions : NestedTickOptions {
        var minor: dynamic /* NestedTickOptions? | Boolean? */
            get() = definedExternally
            set(value) = definedExternally
        var major: dynamic /* MajorTickOptions? | Boolean? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface `L$0` {
        @nativeInvoke
        operator fun invoke(value: Number, index: Number, values: Array<Number>): dynamic /* String? | Number? */

        @nativeInvoke
        operator fun invoke(value: Number, index: Number, values: Array<String>): dynamic /* String? | Number? */

        @nativeInvoke
        operator fun invoke(value: String, index: Number, values: Array<Number>): dynamic /* String? | Number? */

        @nativeInvoke
        operator fun invoke(value: String, index: Number, values: Array<String>): dynamic /* String? | Number? */
    }

    interface NestedTickOptions {
        var autoSkip: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var autoSkipPadding: Number?
            get() = definedExternally
            set(value) = definedExternally
        var backdropColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var backdropPaddingX: Number?
            get() = definedExternally
            set(value) = definedExternally
        var backdropPaddingY: Number?
            get() = definedExternally
            set(value) = definedExternally
        var beginAtZero: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        val callback: `L$0`?
            get() = definedExternally
        var display: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var fontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var fontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var fontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var fontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var labelOffset: Number?
            get() = definedExternally
            set(value) = definedExternally
        var lineHeight: Number?
            get() = definedExternally
            set(value) = definedExternally
        var max: Any?
            get() = definedExternally
            set(value) = definedExternally
        var maxRotation: Number?
            get() = definedExternally
            set(value) = definedExternally
        var maxTicksLimit: Number?
            get() = definedExternally
            set(value) = definedExternally
        var min: Any?
            get() = definedExternally
            set(value) = definedExternally
        var minRotation: Number?
            get() = definedExternally
            set(value) = definedExternally
        var mirror: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var padding: Number?
            get() = definedExternally
            set(value) = definedExternally
        var precision: Number?
            get() = definedExternally
            set(value) = definedExternally
        var reverse: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var sampleSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var showLabelBackdrop: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var source: String? /* "auto" | "data" | "labels" */
            get() = definedExternally
            set(value) = definedExternally
        var stepSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var suggestedMax: Number?
            get() = definedExternally
            set(value) = definedExternally
        var suggestedMin: Number?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface MajorTickOptions : NestedTickOptions {
        var enabled: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface AngleLineOptions {
        var display: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var color: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var lineWidth: Number?
            get() = definedExternally
            set(value) = definedExternally
        var borderDash: Array<Number>?
            get() = definedExternally
            set(value) = definedExternally
        var borderDashOffset: Number?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface PointLabelOptions {
        val callback: ((arg: Any) -> Any)?
        var fontColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? */
            get() = definedExternally
            set(value) = definedExternally
        var fontFamily: String?
            get() = definedExternally
            set(value) = definedExternally
        var fontSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var fontStyle: String?
            get() = definedExternally
            set(value) = definedExternally
        var lineHeight: dynamic /* Number? | String? */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface LinearTickOptions : TickOptions {
        override var maxTicksLimit: Number?
            get() = definedExternally
            set(value) = definedExternally
        override var stepSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        override var precision: Number?
            get() = definedExternally
            set(value) = definedExternally
        override var suggestedMin: Number?
            get() = definedExternally
            set(value) = definedExternally
        override var suggestedMax: Number?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface LogarithmicTickOptions : TickOptions

    interface ChartDataSets {
        var cubicInterpolationMode: dynamic /* "default" | "monotone" | Scriptable<String /* "default" | "monotone" */>? */
            get() = definedExternally
            set(value) = definedExternally
        var backgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var barPercentage: Number?
            get() = definedExternally
            set(value) = definedExternally
        var barThickness: dynamic /* Number? | "flex" */
            get() = definedExternally
            set(value) = definedExternally
        var borderAlign: dynamic /* "center" | "inner" | Array<String /* "center" | "inner" */>? | Scriptable<String /* "center" | "inner" */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderWidth: dynamic /* Number? | Any? | Array<dynamic /* Number | Any */>? | Scriptable<dynamic /* Number | Any */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var borderCapStyle: String? /* "butt" | "round" | "square" */
            get() = definedExternally
            set(value) = definedExternally
        var borderDash: Array<Number>?
            get() = definedExternally
            set(value) = definedExternally
        var borderDashOffset: Number?
            get() = definedExternally
            set(value) = definedExternally
        var borderJoinStyle: String? /* "bevel" | "round" | "miter" */
            get() = definedExternally
            set(value) = definedExternally
        var borderSkipped: dynamic /* "left" | "right" | "top" | "bottom" | "chartArea" | Array<String /* "left" | "right" | "top" | "bottom" | "chartArea" */>? | Scriptable<String /* "left" | "right" | "top" | "bottom" | "chartArea" */>? */
            get() = definedExternally
            set(value) = definedExternally
        var categoryPercentage: Number?
            get() = definedExternally
            set(value) = definedExternally
        var data: dynamic /* Array<dynamic /* Number? | Array<Number>? */>? | Array<ChartPoint>? */
            get() = definedExternally
            set(value) = definedExternally
        var fill: dynamic /* Boolean? | Number? | String? */
            get() = definedExternally
            set(value) = definedExternally
        var hitRadius: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var hoverBackgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var hoverBorderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var hoverBorderWidth: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var hoverRadius: Number?
            get() = definedExternally
            set(value) = definedExternally
        var label: String?
            get() = definedExternally
            set(value) = definedExternally
        var lineTension: Number?
            get() = definedExternally
            set(value) = definedExternally
        var maxBarThickness: Number?
            get() = definedExternally
            set(value) = definedExternally
        var minBarLength: Number?
            get() = definedExternally
            set(value) = definedExternally
        var steppedLine: dynamic /* "before" | "after" | "middle" | Boolean? */
            get() = definedExternally
            set(value) = definedExternally
        var order: Number?
            get() = definedExternally
            set(value) = definedExternally
        var pointBorderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointBackgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointBorderWidth: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointRadius: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointRotation: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointHoverRadius: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointHitRadius: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointHoverBackgroundColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointHoverBorderColor: dynamic /* String? | CanvasGradient? | CanvasPattern? | Array<String>? | Array<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? | Scriptable<dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointHoverBorderWidth: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var pointStyle: dynamic /* "circle" | "cross" | "crossRot" | "dash" | "line" | "rect" | "rectRounded" | "rectRot" | "star" | "triangle" | HTMLImageElement? | HTMLCanvasElement? | Array<dynamic /* "circle" | "cross" | "crossRot" | "dash" | "line" | "rect" | "rectRounded" | "rectRot" | "star" | "triangle" | HTMLImageElement | HTMLCanvasElement */>? | Scriptable<dynamic /* "circle" | "cross" | "crossRot" | "dash" | "line" | "rect" | "rectRounded" | "rectRot" | "star" | "triangle" | HTMLImageElement | HTMLCanvasElement */>? */
            get() = definedExternally
            set(value) = definedExternally
        var radius: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var rotation: dynamic /* Number? | Array<Number>? | Scriptable<Number>? */
            get() = definedExternally
            set(value) = definedExternally
        var xAxisID: String?
            get() = definedExternally
            set(value) = definedExternally
        var yAxisID: String?
            get() = definedExternally
            set(value) = definedExternally
        var type: String? /* "line" | "bar" | "horizontalBar" | "radar" | "doughnut" | "polarArea" | "bubble" | "pie" | "scatter" | String? */
            get() = definedExternally
            set(value) = definedExternally
        var hidden: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var hideInLegendAndTooltip: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var showLine: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var stack: String?
            get() = definedExternally
            set(value) = definedExternally
        var spanGaps: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var weight: Number?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartScales {
        var type: String? /* "category" | "linear" | "logarithmic" | "time" | "radialLinear" | String? */
            get() = definedExternally
            set(value) = definedExternally
        var display: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var position: String? /* "left" | "right" | "top" | "bottom" | "chartArea" | String? */
            get() = definedExternally
            set(value) = definedExternally
        var gridLines: GridLineOptions?
            get() = definedExternally
            set(value) = definedExternally
        var scaleLabel: ScaleTitleOptions?
            get() = definedExternally
            set(value) = definedExternally
        var ticks: TickOptions?
            get() = definedExternally
            set(value) = definedExternally
        var x: ChartXAxe?
            get() = definedExternally
            set(value) = definedExternally
        var y: ChartYAxe?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface CommonAxe {
        var beginAtZero: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var bounds: String?
            get() = definedExternally
            set(value) = definedExternally
        var type: String? /* "category" | "linear" | "logarithmic" | "time" | "radialLinear" | String? */
            get() = definedExternally
            set(value) = definedExternally
        var display: dynamic /* Boolean? | String? */
            get() = definedExternally
            set(value) = definedExternally
        var id: String?
            get() = definedExternally
            set(value) = definedExternally
        var labels: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var stacked: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var position: String?
            get() = definedExternally
            set(value) = definedExternally
        var ticks: TickOptions?
            get() = definedExternally
            set(value) = definedExternally
        var gridLines: GridLineOptions?
            get() = definedExternally
            set(value) = definedExternally
        var scaleLabel: ScaleTitleOptions?
            get() = definedExternally
            set(value) = definedExternally
        var time: TimeScale?
            get() = definedExternally
            set(value) = definedExternally
        var offset: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        val beforeUpdate: ((scale: Any) -> Unit)?
        val beforeSetDimension: ((scale: Any) -> Unit)?
        val beforeDataLimits: ((scale: Any) -> Unit)?
        val beforeBuildTicks: ((scale: Any) -> Unit)?
        val beforeTickToLabelConversion: ((scale: Any) -> Unit)?
        val beforeCalculateTickRotation: ((scale: Any) -> Unit)?
        val beforeFit: ((scale: Any) -> Unit)?
        val afterUpdate: ((scale: Any) -> Unit)?
        val afterSetDimension: ((scale: Any) -> Unit)?
        val afterDataLimits: ((scale: Any) -> Unit)?
        val afterBuildTicks: ((scale: Any, ticks: Array<Number>) -> Array<Number>)?
        val afterTickToLabelConversion: ((scale: Any) -> Unit)?
        val afterCalculateTickRotation: ((scale: Any) -> Unit)?
        val afterFit: ((scale: Any) -> Unit)?
    }

    interface ChartXAxe : CommonAxe {
        var distribution: String? /* "linear" | "series" */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartYAxe : CommonAxe
    interface LinearScale : ChartScales {
        override var ticks: TickOptions?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface LogarithmicScale : ChartScales {
        override var ticks: TickOptions?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface TimeDisplayFormat {
        var millisecond: String?
            get() = definedExternally
            set(value) = definedExternally
        var second: String?
            get() = definedExternally
            set(value) = definedExternally
        var minute: String?
            get() = definedExternally
            set(value) = definedExternally
        var hour: String?
            get() = definedExternally
            set(value) = definedExternally
        var day: String?
            get() = definedExternally
            set(value) = definedExternally
        var week: String?
            get() = definedExternally
            set(value) = definedExternally
        var month: String?
            get() = definedExternally
            set(value) = definedExternally
        var quarter: String?
            get() = definedExternally
            set(value) = definedExternally
        var year: String?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface DateAdapterOptions {
        var date: Any?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface TimeScale : ChartScales {
        var adapters: DateAdapterOptions?
            get() = definedExternally
            set(value) = definedExternally
        var displayFormats: TimeDisplayFormat?
            get() = definedExternally
            set(value) = definedExternally
        var isoWeekday: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var max: String?
            get() = definedExternally
            set(value) = definedExternally
        var min: String?
            get() = definedExternally
            set(value) = definedExternally
        var parser: dynamic /* String? | ((arg: Any) -> Any)? */
            get() = definedExternally
            set(value) = definedExternally
        var round: String? /* "millisecond" | "second" | "minute" | "hour" | "day" | "week" | "month" | "quarter" | "year" */
            get() = definedExternally
            set(value) = definedExternally
        var tooltipFormat: String?
            get() = definedExternally
            set(value) = definedExternally
        var unit: String? /* "millisecond" | "second" | "minute" | "hour" | "day" | "week" | "month" | "quarter" | "year" */
            get() = definedExternally
            set(value) = definedExternally
        var unitStepSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var stepSize: Number?
            get() = definedExternally
            set(value) = definedExternally
        var minUnit: String? /* "millisecond" | "second" | "minute" | "hour" | "day" | "week" | "month" | "quarter" | "year" */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface RadialLinearScale {
        var animate: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var position: String? /* "left" | "right" | "top" | "bottom" | "chartArea" */
            get() = definedExternally
            set(value) = definedExternally
        var angleLines: AngleLineOptions?
            get() = definedExternally
            set(value) = definedExternally
        var pointLabels: PointLabelOptions?
            get() = definedExternally
            set(value) = definedExternally
        var ticks: LinearTickOptions?
            get() = definedExternally
            set(value) = definedExternally
        var display: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var gridLines: GridLineOptions?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface Point {
        var x: Number
        var y: Number
    }

    interface PluginServiceGlobalRegistration {
        var id: String?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface PluginServiceRegistrationOptions {
        val beforeInit: ((chartInstance: Chart, options: Any) -> Unit)?
        val afterInit: ((chartInstance: Chart, options: Any) -> Unit)?
        val beforeUpdate: ((chartInstance: Chart, options: Any) -> Unit)?
        val afterUpdate: ((chartInstance: Chart, options: Any) -> Unit)?
        val beforeLayout: ((chartInstance: Chart, options: Any) -> Unit)?
        val afterLayout: ((chartInstance: Chart, options: Any) -> Unit)?
        val beforeDatasetsUpdate: ((chartInstance: Chart, options: Any) -> Unit)?
        val afterDatasetsUpdate: ((chartInstance: Chart, options: Any) -> Unit)?
        val beforeDatasetUpdate: ((chartInstance: Chart, options: Any) -> Unit)?
        val afterDatasetUpdate: ((chartInstance: Chart, options: Any) -> Unit)?
        val beforeRender: ((chartInstance: Chart, options: Any) -> Unit)?
        val afterRender: ((chartInstance: Chart, options: Any) -> Unit)?
        val beforeDraw: ((chartInstance: Chart, easing: String /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */, options: Any) -> Unit)?
        val afterDraw: ((chartInstance: Chart, easing: String /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */, options: Any) -> Unit)?
        val beforeDatasetsDraw: ((chartInstance: Chart, easing: String /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */, options: Any) -> Unit)?
        val afterDatasetsDraw: ((chartInstance: Chart, easing: String /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */, options: Any) -> Unit)?
        val beforeDatasetDraw: ((chartInstance: Chart, easing: String /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */, options: Any) -> Unit)?
        val afterDatasetDraw: ((chartInstance: Chart, easing: String /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */, options: Any) -> Unit)?
        val beforeTooltipDraw: ((chartInstance: Chart, tooltipData: Any, options: Any) -> Unit)?
        val afterTooltipDraw: ((chartInstance: Chart, tooltipData: Any, options: Any) -> Unit)?
        val beforeEvent: ((chartInstance: Chart, event: Event, options: Any) -> Unit)?
        val afterEvent: ((chartInstance: Chart, event: Event, options: Any) -> Unit)?
        val resize: ((chartInstance: Chart, newChartSize: ChartSize, options: Any) -> Unit)?
        val destroy: ((chartInstance: Chart) -> Unit)?
        val afterScaleUpdate: ((chartInstance: Chart, options: Any) -> Unit)?
    }

    interface ChartUpdateProps {
        var duration: Number?
            get() = definedExternally
            set(value) = definedExternally
        var lazy: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var easing: String? /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface ChartRenderProps {
        var duration: Number?
            get() = definedExternally
            set(value) = definedExternally
        var lazy: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var easing: String? /* "linear" | "easeInQuad" | "easeOutQuad" | "easeInOutQuad" | "easeInCubic" | "easeOutCubic" | "easeInOutCubic" | "easeInQuart" | "easeOutQuart" | "easeInOutQuart" | "easeInQuint" | "easeOutQuint" | "easeInOutQuint" | "easeInSine" | "easeOutSine" | "easeInOutSine" | "easeInExpo" | "easeOutExpo" | "easeInOutExpo" | "easeInCirc" | "easeOutCirc" | "easeInOutCirc" | "easeInElastic" | "easeOutElastic" | "easeInOutElastic" | "easeInBack" | "easeOutBack" | "easeInOutBack" | "easeInBounce" | "easeOutBounce" | "easeInOutBounce" */
            get() = definedExternally
            set(value) = definedExternally
    }

    interface DoughnutModel {
        var backgroundColor: dynamic /* String | CanvasGradient | CanvasPattern | Array<String> */
            get() = definedExternally
            set(value) = definedExternally
        var borderAlign: String /* "center" | "inner" */
        var borderColor: String
        var borderWidth: Number
        var circumference: Number
        var endAngle: Number
        var innerRadius: Number
        var outerRadius: Number
        var startAngle: Number
        var x: Number
        var y: Number
    }
}

external interface PluginDescriptor {
    var plugin: PluginServiceGlobalRegistration /* Chart.PluginServiceGlobalRegistration & Chart.PluginServiceRegistrationOptions */
}

open external class PluginServiceStatic {
    open fun register(plugin: PluginServiceGlobalRegistration /* Chart.PluginServiceGlobalRegistration & Chart.PluginServiceRegistrationOptions */)
    open fun unregister(plugin: PluginServiceGlobalRegistration /* Chart.PluginServiceGlobalRegistration & Chart.PluginServiceRegistrationOptions */)
    open fun clear()
    open fun count(): Number
    open fun getAll(): Array<PluginServiceGlobalRegistration /* Chart.PluginServiceGlobalRegistration & Chart.PluginServiceRegistrationOptions */>
    open fun notify(
        chart: Chart,
        hook: String /* "beforeInit" | "afterInit" | "beforeUpdate" | "afterUpdate" | "beforeLayout" | "afterLayout" | "beforeDatasetsUpdate" | "afterDatasetsUpdate" | "beforeDatasetUpdate" | "afterDatasetUpdate" | "beforeRender" | "afterRender" | "beforeDraw" | "afterDraw" | "beforeDatasetsDraw" | "afterDatasetsDraw" | "beforeDatasetDraw" | "afterDatasetDraw" | "beforeTooltipDraw" | "afterTooltipDraw" | "beforeEvent" | "afterEvent" | "resize" | "destroy" | "afterScaleUpdate" */,
        args: Any
    ): Boolean

    open fun descriptors(chart: Chart): Array<PluginDescriptor>
}

external interface Meta {
    var type: String /* "line" | "bar" | "horizontalBar" | "radar" | "doughnut" | "polarArea" | "bubble" | "pie" | "scatter" */
    var data: Array<MetaData>
    var dataset: ChartDataSets?
        get() = definedExternally
        set(value) = definedExternally
    var controller: Json
    var hidden: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var total: String?
        get() = definedExternally
        set(value) = definedExternally
    var xAxisID: String?
        get() = definedExternally
        set(value) = definedExternally
    var yAxisID: String?
        get() = definedExternally
        set(value) = definedExternally
    var `$filler`: Json?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MetaData {
    var _chart: Chart
    var _datasetIndex: Number
    var _index: Number
    var _model: Model
    var _start: Any?
        get() = definedExternally
        set(value) = definedExternally
    var _view: Model
    var _xScale: ChartScales
    var _yScale: ChartScales
    var hidden: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Model {
    var backgroundColor: String
    var borderAlign: String? /* "center" | "inner" */
        get() = definedExternally
        set(value) = definedExternally
    var borderColor: String
    var borderWidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var circumference: Number?
        get() = definedExternally
        set(value) = definedExternally
    var controlPointNextX: Number
    var controlPointNextY: Number
    var controlPointPreviousX: Number
    var controlPointPreviousY: Number
    var endAngle: Number?
        get() = definedExternally
        set(value) = definedExternally
    var hitRadius: Number
    var innerRadius: Number?
        get() = definedExternally
        set(value) = definedExternally
    var outerRadius: Number?
        get() = definedExternally
        set(value) = definedExternally
    var pointStyle: String
    var radius: String
    var skip: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var startAngle: Number?
        get() = definedExternally
        set(value) = definedExternally
    var steppedLine: Any?
        get() = definedExternally
        set(value) = definedExternally
    var tension: Number
    var x: Number
    var y: Number
    var base: Number
    var head: Number
}