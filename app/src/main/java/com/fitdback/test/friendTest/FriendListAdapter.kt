package com.fitdback.test.friendTest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.fitdback.database.datamodel.FriendDataModel
import com.fitdback.posedetection.R
import com.fitdback.test.DevModeActivity

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

        // listvew_friend_list.xml 에 정의된 위젯 참조
        val friendNicknameArea = myConvertView?.findViewById<TextView>(R.id.list_friendNicknameArea)
        val friendCodeArea = myConvertView?.findViewById<TextView>(R.id.list_friendCodeArea)
//        val btnShowFriendStatistics = myConvertView?.findViewById<Button>(R.id.btnShowFriendStatistics)

        friendNicknameArea!!.text = list[position].friend_nickname
        friendCodeArea!!.text = list[position].friend_uid

        // 친구 통계 보기
//        btnShowFriendStatistics?.setOnClickListener {
//
//            Toast.makeText(DevModeActivity.context, "", Toast.LENGTH_SHORT).show()
//
//        }


        return myConvertView!!
    }

}