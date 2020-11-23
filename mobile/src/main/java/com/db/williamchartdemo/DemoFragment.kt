package com.db.williamchartdemo

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.db.williamchart.ExperimentalFeature
import com.db.williamchart.slidertooltip.SliderTooltip
import kotlinx.android.synthetic.main.demo_fragment.*

class DemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.demo_fragment, container, false)

    override fun onViewCreated(view: View, saveInstanceState: Bundle?) {
        mobileConnectionBarChart.animate(
                linkedMapOf(
                        "Янв" to 1000f,
                        "Февр" to 200f,
                        "Март" to 500f,
                        "Апр" to 20f,
                        "Май" to 100f,
                        "Июнь" to 800f,
                        "Авг" to 500f,
                        "Сент" to 300f,
                        "Окт" to 400f,
                        "Ноя" to 450f,
                        "Дек" to 0f
                )
        )

        mobileConnectionBarChart.onDataPointClickListener = { index, _, _ ->
            Log.i("clicked_time", System.currentTimeMillis().toString())
            for(i in mobileConnectionBarChart.barsColors.indices) {
                mobileConnectionBarChart.barsColors[i] = ContextCompat.getColor(requireContext(),
                        R.color.colorPrimary)
            }
            mobileConnectionBarChart.barsColors[index] = ContextCompat.getColor(requireContext(),
                    R.color.colorAccent)

            mobileConnectionBarChart.updateData(linkedMapOf(
                    "Янв" to 1300f,
                    "Февр" to 800f,
                    "Март" to 100f,
                    "Апр" to 80f,
                    "Май" to 100f,
                    "Июнь" to 800f,
                    "Авг" to 500f,
                    "Сент" to 300f,
                    "Окт" to 400f,
                    "Ноя" to 450f,
                    "Дек" to 0f
            ))
            mobileConnectionBarChart.invalidate()

            Log.i("draw_time", System.currentTimeMillis().toString())
            Toast.makeText(requireContext(), "$index clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
