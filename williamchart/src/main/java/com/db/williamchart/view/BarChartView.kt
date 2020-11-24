package com.db.williamchart.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.db.williamchart.ChartContract
import com.db.williamchart.R
import com.db.williamchart.animation.NoAnimation
import com.db.williamchart.data.configuration.BarChartConfiguration
import com.db.williamchart.data.configuration.ChartConfiguration
import com.db.williamchart.data.Frame
import com.db.williamchart.data.Label
import com.db.williamchart.data.Paddings
import com.db.williamchart.data.toRect
import com.db.williamchart.data.toRectF
import com.db.williamchart.extensions.drawChartBar
import com.db.williamchart.extensions.obtainStyledAttributes
import com.db.williamchart.renderer.BarChartRenderer

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AxisChartView(context, attrs, defStyleAttr), ChartContract.BarView {

    @Suppress("MemberVisibilityCanBePrivate")
    var spacing = defaultSpacing

    @Suppress("MemberVisibilityCanBePrivate")
    var barsColors = arrayListOf(defaultBarsColor)

    @Suppress("MemberVisibilityCanBePrivate")
    var barRadius: Float = defaultBarsRadius

    @Suppress("MemberVisibilityCanBePrivate")
    var barsBackgroundColors = ArrayList<Int>()

    override val chartConfiguration: ChartConfiguration
        get() =
            BarChartConfiguration(
                width = 1500,
                height = measuredHeight,
                paddings = Paddings(
                    paddingLeft.toFloat(),
                    paddingTop.toFloat(),
                    paddingRight.toFloat(),
                    paddingBottom.toFloat()
                ),
                axis = axis,
                labelsSize = labelsSize,
                scale = scale,
                barsBackgroundColors = barsBackgroundColors,
                barsSpacing = spacing,
                labelsFormatter = labelsFormatter
            )

    init {
        renderer = BarChartRenderer(this, painter, NoAnimation())
        handleAttributes(obtainStyledAttributes(attrs, R.styleable.BarChartAttrs))
        handleEditMode()
    }

    override fun drawBars(frames: List<Frame>) {
        painter.prepare(style = Paint.Style.FILL)
        for(i in frames.indices) {
            painter.paint.color = when {
                barsColors.size > i -> barsColors[i]
                barsColors.isEmpty() -> defaultBarsColor
                else -> barsColors[barsColors.size - 1]
            }
            canvas.drawChartBar(
                frames[i].toRectF(),
                barRadius,
                painter.paint
            )
            latestRightBound = frames[i].right
        }
    }

    override fun drawBarsBackground(frames: List<Frame>) {
        painter.prepare(style = Paint.Style.FILL)
        for(i in frames.indices) {
            painter.paint.color = when {
                barsBackgroundColors.size > i -> barsBackgroundColors[i]
                barsBackgroundColors.isEmpty() -> defaultBarsColor
                else -> barsBackgroundColors[barsBackgroundColors.size - 1]
            }
            canvas.drawChartBar(
                frames[i].toRectF(),
                barRadius,
                painter.paint
            )
        }
    }

    override fun drawLabels(xLabels: List<Label>) {
        painter.prepare(textSize = labelsSize, font = labelsFont)
        labels.draw(canvas, painter.paint, labelsColors, xLabels)
    }

    override fun drawGrid(
        innerFrame: Frame,
        xLabelsPositions: List<Float>,
        yLabelsPositions: List<Float>
    ) {
        grid.draw(canvas, innerFrame, xLabelsPositions, yLabelsPositions)
    }

    override fun drawDebugFrame(frames: List<Frame>) {
        painter.prepare(color = -0x1000000, style = Paint.Style.STROKE)
        frames.forEach { canvas.drawRect(it.toRect(), painter.paint) }
    }

    private fun handleAttributes(typedArray: TypedArray) {
        typedArray.apply {
            spacing = getDimension(R.styleable.BarChartAttrs_chart_spacing, spacing)

            if(hasValue(R.styleable.BarChartAttrs_chart_barsColors)) {
                val barsColorsId = typedArray.getResourceId(R.styleable.BarChartAttrs_chart_barsColors,
                        0)
                barsColors = typedArray.resources.getIntArray(barsColorsId).toCollection(ArrayList())
            }

            if(hasValue(R.styleable.BarChartAttrs_chart_barsBackgroundColors)) {
                val barsBackgroundColorsId = typedArray.getResourceId(R.styleable.BarChartAttrs_chart_barsBackgroundColors, 0)
                barsBackgroundColors = typedArray.resources.getIntArray(barsBackgroundColorsId).toCollection(ArrayList())
            }

            barRadius = getDimension(R.styleable.BarChartAttrs_chart_barsRadius, barRadius)
            recycle()
        }
    }

    companion object {
        private const val defaultSpacing = 10f
        private const val defaultBarsColor = Color.BLUE
        private const val defaultBarsRadius = 0F
        var latestRightBound = 0f
    }
}
