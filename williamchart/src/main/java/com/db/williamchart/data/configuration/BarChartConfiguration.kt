package com.db.williamchart.data.configuration

import com.db.williamchart.data.AxisType
import com.db.williamchart.data.Paddings
import com.db.williamchart.data.Scale

data class BarChartConfiguration(
    override val width: Int,
    override val height: Int,
    override val paddings: Paddings,
    override val axis: AxisType,
    override val labelsSize: Float,
    override val scale: Scale,
    override val labelsFormatter: (Float) -> String = { it.toString() },
    val barsBackgroundColors: ArrayList<Int>,
    val barsSpacing: Float
) : ChartConfiguration(
    width = width,
    height = height,
    paddings = paddings,
    axis = axis,
    labelsSize = labelsSize,
    scale = scale,
    labelsFormatter = labelsFormatter
)
