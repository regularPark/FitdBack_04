package com.fitdback.test.friendTest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.BaseAdapter
import android.widget.TextView
import com.fitdback.database.datamodel.FriendDataModel
import com.fitdback.posedetection.R

class FriendListAdapter(val list: MutableList<FriendDataModel>) : BaseAdapter() {
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

        // listvew_friend_list 레이아웃과 연결 & 데이터 삽입
        if (myConvertView == null) {
            myConvertView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.listview_friend_list, parent, false)
        }

        val friendNicknameArea = myConvertView?.findViewById<TextView>(R.id.list_friendNicknameArea)
        val friendCodeArea = myConvertView?.findViewById<TextView>(R.id.list_friendCodeArea)

        friendNicknameArea!!.text = list[position].friend_nickname
        friendCodeArea!!.text = list[position].friend_uid

        return myConvertView!!
    }

}