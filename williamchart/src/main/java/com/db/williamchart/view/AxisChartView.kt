package com.db.williamchart.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.doOnPreDraw
import com.db.williamchart.*
import com.db.williamchart.animation.ChartAnimation
import com.db.williamchart.animation.DefaultAnimation
import com.db.williamchart.data.AxisType
import com.db.williamchart.data.DataPoint
import com.db.williamchart.data.Frame
import com.db.williamchart.data.Scale
import com.db.williamchart.data.configuration.ChartConfiguration
import com.db.williamchart.extensions.obtainStyledAttributes
import com.db.williamchart.plugin.AxisGrid
import com.db.williamchart.plugin.AxisLabels
import com.db.williamchart.renderer.RendererConstants.Companion.notInitialized
import kotlin.math.max

@OptIn(ExperimentalFeature::class)
abstract class AxisChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var widthOfView = 0

    var dataCount = 0

    var labelsSize: Float = defaultLabelsSize

    var labelsColors = arrayListOf(-0x1000000)

    var labelsFont: Typeface? = null

    var axis: AxisType = AxisType.XY

    var scale: Scale = Scale(notInitialized, notInitialized)

    var labelsFormatter: (Float) -> String = { it.toString() }

    var animation: ChartAnimation<DataPoint> = DefaultAnimation()

    val labels: Labels = AxisLabels()

    var tooltip: Tooltip = object : Tooltip {
        override fun onCreateTooltip(parentView: ViewGroup) {}
        override fun onDataPointTouch(x: Float, y: Float) {}
        override fun onDataPointClick(x: Float, y: Float) {}
    }

    var grid: Grid = object : Grid {
        override fun draw(
                canvas: Canvas,
                innerFrame: Frame,
                xLabelsPositions: List<Float>,
                yLabelsPositions: List<Float>
        ) {
        }
    }

    @ExperimentalFeature
    var onDataPointClickListener: (index: Int, x: Float, y: Float) -> Unit = { _, _, _ -> }

    @ExperimentalFeature
    var onDataPointTouchListener: (index: Int, x: Float, y: Float) -> Unit = { _, _, _ -> }

    protected lateinit var canvas: Canvas

    protected val painter: Painter = Painter(labelsFont = labelsFont)

    /**
     * Initialized in init function by chart views extending [AxisChartView] (e.g. [LineChartView])
     */
    protected lateinit var renderer: ChartContract.Renderer

    private val gestureDetector: GestureDetectorCompat =
        GestureDetectorCompat(
                this.context,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDown(e: MotionEvent?): Boolean = true
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        val (index, x, y) = renderer.processClick(e?.x, e?.y)
                        return if (index != -1) {
                            onDataPointClickListener(index, x, y)
                            tooltip.onDataPointClick(x, y)
                            true
                        } else false
                    }

                    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                        Log.i("onScroll", "before: distanceX = $distanceX")
                        if(scrollX + distanceX >= 0 && scrollX + distanceX + canvas.width <= BarChartView.latestRightBound) {
                            scrollBy(distanceX.toInt(), 0)
                        }
                        Log.i("onScroll", "after: current ScrollX = $scrollX")
                        Log.i("onScroll", "right bound = ${BarChartView.latestRightBound}")
                        Log.i("onScroll", "canvas width = ${canvas.width}")
                        return true
                    }
                }
        )

    init {
        handleAttributes(obtainStyledAttributes(attrs, R.styleable.ChartAttrs))
        doOnPreDraw { tooltip.onCreateTooltip(this) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.setWillNotDraw(false)
        // style.init()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // style.clean()
    }

    override fun computeHorizontalScrollOffset(): Int {
        return max(0, scrollX)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        widthOfView = if (widthMode == MeasureSpec.AT_MOST) defaultFrameWidth else widthMeasureSpec
        setMeasuredDimension(
                widthOfView,
                if (heightMode == MeasureSpec.AT_MOST) defaultFrameHeight else heightMeasureSpec
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        renderer.draw()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val (index, x, y) = renderer.processTouch(event?.x, event?.y)
        if (index != -1) {
            onDataPointTouchListener(index, x, y)
            tooltip.onDataPointTouch(x, y)
        }
        return if (gestureDetector.onTouchEvent(event)) true
        else super.onTouchEvent(event)
    }

    abstract val chartConfiguration: ChartConfiguration

    fun resetScrollX() {
        scrollBy(-scrollX, 0)
    }

    fun show(entries: LinkedHashMap<String, Float>) {
        dataCount = entries.count()
        doOnPreDraw { renderer.preDraw(chartConfiguration, true) }
        renderer.render(entries)
    }

    fun updateData(entries: LinkedHashMap<String, Float>) {
        dataCount = entries.count()
        doOnPreDraw { renderer.preDraw(chartConfiguration, false) }
        renderer.render(entries)
    }

    fun animate(entries: LinkedHashMap<String, Float>) {
        dataCount = entries.count()
        doOnPreDraw { renderer.preDraw(chartConfiguration, true) }
        renderer.anim(entries, animation)
    }

    private fun handleAttributes(typedArray: TypedArray) {
        typedArray.apply {

            // Customize Axis
            axis = when (getString(R.styleable.ChartAttrs_chart_axis)) {
                "0" -> AxisType.NONE
                "1" -> AxisType.X
                "2" -> AxisType.Y
                else -> AxisType.XY
            }

            // Customize Labels
            labelsSize = getDimension(R.styleable.ChartAttrs_chart_labelsSize, labelsSize)

            if(hasValue(R.styleable.ChartAttrs_chart_labelsColors)) {
                val labelsColorsId = typedArray.getResourceId(R.styleable.ChartAttrs_chart_labelsColors, -1)
                labelsColors = typedArray.resources.getIntArray(labelsColorsId).toCollection(ArrayList())
            }

            if (hasValue(R.styleable.ChartAttrs_chart_labelsFont) && !isInEditMode) {
                labelsFont =
                    ResourcesCompat.getFont(
                            context,
                            getResourceId(R.styleable.ChartAttrs_chart_labelsFont, -1)
                    )
                painter.labelsFont = labelsFont
            }

            // Customize Grid
            val shouldDisplayGrid = getBoolean(R.styleable.ChartAttrs_chart_grid, false)
            if (shouldDisplayGrid) {
                grid = AxisGrid().apply {
                    color = getColor(R.styleable.ChartAttrs_chart_gridColor, color)
                    strokeWidth =
                        getDimension(R.styleable.ChartAttrs_chart_gridStrokeWidth, strokeWidth)
                }
            }

            recycle()
        }
    }

    protected fun handleEditMode() {
        if (isInEditMode) {
            show(editModeSampleData)
        }
    }

    companion object {
        private const val defaultFrameWidth = 200
        private const val defaultFrameHeight = 100
        private const val defaultLabelsSize = 60F
        private val editModeSampleData =
            linkedMapOf(
                    "Label1" to 1f,
                    "Label2" to 7.5f,
                    "Label3" to 4.7f,
                    "Label4" to 3.5f
            )
    }
}
