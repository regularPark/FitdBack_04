package com.fitdback.userinterface.listviewadpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.fitdback.database.datamodel.ExerciseDataModel
import com.fitdback.posedetection.R

class ExDataListAdapter(val list: MutableList<ExerciseDataModel>) : BaseAdapter() {
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
                .inflate(R.layout.listview_community_ex_data_list, parent, false)
        }

        // listview_community_ex_data_list.xml 에 정의된 위젯 참조
        val list_exDataArea = myConvertView?.findViewById<TextView>(R.id.list_exDataArea)

        list_exDataArea!!.text = list[position].ex_type
//        friendNicknameArea!!.text = list[position].friend_nickname
//        friendCodeArea!!.text = list[position].friend_uid


        return myConvertView!!
    }

}