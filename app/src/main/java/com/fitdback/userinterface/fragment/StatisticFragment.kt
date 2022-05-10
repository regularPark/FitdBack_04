package com.fitdback.userinterface.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fitdback.database.DataBasket
import com.fitdback.posedetection.R
import com.fitdback.test.barChartTest.BarChartGenerator2
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatisticFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_statistic, container, false)

        val barChart: BarChart = view.findViewById(R.id.barChart_frag) // barChart 생성

        // 사용할 데이터
//        val entryList = mutableListOf<BarEntry>()
//        val dailySum = DataBasket.enhancedGetDailySum(DataBasket.individualExData!!, "ex_count")
//        var xValue = 1.2f

        // 엔트리에 데이터 추가
//        for (data in dailySum) {
//            entryList.add(BarEntry(xValue, data.value.toFloat()))
//            xValue += 1
//        }

        // test sample
        val sampleEntryList = mutableListOf<BarEntry>()
        sampleEntryList.add(BarEntry(1f, 4f))
        sampleEntryList.add(BarEntry(2f, 10f))
        sampleEntryList.add(BarEntry(3f, 2f))
        sampleEntryList.add(BarEntry(4f, 15f))
        sampleEntryList.add(BarEntry(5f, 13f))
        sampleEntryList.add(BarEntry(6f, 2f))
        sampleEntryList.add(BarEntry(7f, 20f))

        /*
            Bar Data Set을 만들기 위한 Data Entry
         */

        // 날짜별 ex_count Sum
        val dailyExCountSumBarEntry = getDailySumBarEntry("ex_count")

        // 날짜별 ex_calorie Sum
        val dailyExCalorieSumBarEntry = getDailySumBarEntry("ex_calorie")

        // 실제 Bar Data Set 생성.
        // dailyExCountSumBarEntry 또는 dailyExCalorieSumBarEntry로 argument변경하여 사용
        val barDataSet = BarDataSet(dailyExCalorieSumBarEntry, "exDataList").apply {

            valueTextColor = Color.GRAY
            valueTextSize = 10f
            setColors(*ColorTemplate.COLORFUL_COLORS)

        }

        val data = BarData(barDataSet)
        barChart.data = data

        val barChartGenerator2 = BarChartGenerator2()
        BarChartGenerator2().runBarChart(barChart, barDataSet.yMax + 1.0f)



    return view
    }

    private fun getDailySumBarEntry(targetData: String): MutableList<BarEntry> {
        /*
            targetData: String 종류 : "ex_count" , "ex_calorie" // 스펠링 주의!
         */

        val dailySumBarEntry = mutableListOf<BarEntry>()
        val dailySumList    =
            DataBasket.enhancedGetDailySum(DataBasket.individualExData!!, targetData)
        var xValue = 1f

        for (keyAndValue in dailySumList) {
            dailySumBarEntry.add(BarEntry(xValue, keyAndValue.value.toFloat()))
            xValue += 1f
        }

        return dailySumBarEntry
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatisticFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatisticFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}