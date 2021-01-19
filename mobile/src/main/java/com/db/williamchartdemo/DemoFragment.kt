package com.db.williamchartdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.demo_fragment.*

class DemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.demo_fragment, container, false)

    override fun onViewCreated(view: View, saveInstanceState: Bundle?) {
        mobileConnectionBarChart.updateData(linkedMapOf(
                "00:00" to 1300f,
                "01:00" to 800f,
                "02:00" to 100f,
                "03:00" to 80f,
                "04:00" to 100f,
                "05:00" to 800f,
                "06:00" to 600f,
                "07:00" to 500f,
                "08:00" to 300f,
                "09:00" to 400f,
                "10:00" to 450f,
                "11:00" to 0f,
                "12:00" to 1300f,
                "13:00" to 800f,
                "14:00" to 100f,
                "15:00" to 80f,
                "16:00" to 100f,
                "17:00" to 800f,
                "18:00" to 600f,
                "19:00" to 500f,
                "20:00" to 300f,
                "21:00" to 400f,
                "22:00" to 450f,
                "23:00" to 0f
        ))

        mobileConnectionBarChart.onDataPointClickListener = { index, _, _ ->
            Log.i("clicked_time", System.currentTimeMillis().toString())
            for (i in mobileConnectionBarChart.barsColors.indices) {
                mobileConnectionBarChart.barsColors[i] = ContextCompat.getColor(requireContext(),
                        R.color.colorPrimary)
            }
            mobileConnectionBarChart.barsColors[index] = ContextCompat.getColor(requireContext(),
                    R.color.colorAccent)

            mobileConnectionBarChart.updateData(linkedMapOf(
                    "00:00" to 1300f,
                    "01:00" to 800f,
                    "02:00" to 100f,
                    "03:00" to 80f,
                    "04:00" to 100f,
                    "05:00" to 800f,
                    "06:00" to 600f,
                    "07:00" to 500f,
                    "08:00" to 300f,
                    "09:00" to 400f,
                    "10:00" to 450f,
                    "11:00" to 0f,
                    "12:00" to 1300f,
                    "13:00" to 800f,
                    "14:00" to 100f,
                    "15:00" to 80f,
                    "16:00" to 100f,
                    "17:00" to 800f,
                    "18:00" to 600f,
                    "19:00" to 500f,
                    "20:00" to 300f,
                    "21:00" to 400f,
                    "22:00" to 450f,
                    "23:00" to 0f
            ))
            mobileConnectionBarChart.invalidate()

            Log.i("draw_time", System.currentTimeMillis().toString())
            Toast.makeText(requireContext(), "$index clicked", Toast.LENGTH_SHORT).show()
        }

        mobileConnectionBarChart.scrollToBar(23)
    }
}
