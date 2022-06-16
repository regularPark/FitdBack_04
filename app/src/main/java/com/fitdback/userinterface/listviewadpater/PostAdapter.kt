package com.fitdback.userinterface.listviewadpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.fitdback.database.datamodel.PostDataModel
import com.fitdback.posedetection.R

class PostAdapter(val list: MutableList<PostDataModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var myConvertView = convertView

        // listview_community_ex_data_list 레이아웃과 연결 & 데이터 삽입
        if (myConvertView == null) {
            myConvertView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.listvew_community_post_list, parent, false)
        }

        // listview_community_post_list.xml 에 정의된 위젯 참조
        val post_ex_history_area = myConvertView?.findViewById<TextView>(R.id.post_ex_history_area)
        val post_memo_area  = myConvertView?.findViewById<TextView>(R.id.post_memo_area)
        val post_writer_and_datetime_area  = myConvertView?.findViewById<TextView>(R.id.post_writer_and_datetime_area)

        post_ex_history_area?.text = list[position].post_ex_history
        post_memo_area?.text = list[position].post_memo
        post_writer_and_datetime_area?.text = "posted by ${list[position].post_writer} on ${list[position].post_datetime}"

        return myConvertView!!
    }

}