package com.fitdback.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.fitdback.posedetection.R

class ListViewAdapter(val List : MutableList<FeedbackTestDataModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return List.size
    }

    override fun getItem(position: Int): Any {
        return List[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView
        if (convertView == null) {

            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.listview_health_memo, parent, false)

        }

        // 레이아웃과 연결
        val totalCount = convertView?.findViewById<TextView>(R.id.listViewTotalCountArea)
        val successCount = convertView?.findViewById<TextView>(R.id.listViewSuccessCountArea)
        val exerciseTime = convertView?.findViewById<TextView>(R.id.listViewExerciseTimeArea)

        totalCount!!.text = List[position].totalCount.toString() // postion에 해당하는 데이터의 totalCount
        successCount!!.text = List[position].successCount.toString()
        exerciseTime!!.text = List[position].exerciseTime.toString()

        return convertView!!

    }

}