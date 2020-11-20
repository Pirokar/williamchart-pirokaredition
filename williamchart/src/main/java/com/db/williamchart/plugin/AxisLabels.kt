package com.db.williamchart.plugin

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.db.williamchart.Labels
import com.db.williamchart.data.Label
import com.db.williamchart.view.BarChartView

class AxisLabels : Labels {
    override fun draw(canvas: Canvas, paint: Paint, colors: List<Int>, xLabels: List<Label>) {
        for (i in xLabels.indices) {
            paint.color = when {
                colors.size > i -> colors[i]
                colors.isEmpty() -> Color.BLACK
                else -> colors[colors.size - 1]
            }
            canvas.drawText(
                    xLabels[i].label,
                    xLabels[i].screenPositionX,
                    xLabels[i].screenPositionY,
                    paint
            )
        }
    }
}
