package com.fitdback.userinterface

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.ListView
import com.fitdback.database.DataBasket
import com.fitdback.database.datamodel.ExerciseDataModel
import com.fitdback.posedetection.R
import com.fitdback.test.CustomDialog
import com.fitdback.userinterface.listviewadpater.ExDataListAdapter
import java.text.SimpleDateFormat
import java.util.*

class CommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        // Post 아이콘을 클릭하여 날짜를 선택
        val btnWritePost = findViewById<ImageView>(R.id.btnWritePost)

        btnWritePost.setOnClickListener {

            // community_posting 다이얼로그
            val postingDialog = CustomDialog(this, R.layout.dialog_community_posting, "Posting")
            val postingAlertDialog = postingDialog.showDialog()

            val btnSelectDate = postingAlertDialog?.findViewById<Button>(R.id.btnSelectDate)
            val btnSelectExData = postingAlertDialog?.findViewById<Button>(R.id.btnSelectExData)
            val btnPost = postingAlertDialog?.findViewById<Button>(R.id.btnPost)

            val ex_history: String? = null
            val user_memo: String? = null
            var selectedDate: String? = null

            // 날짜 선택
            btnSelectDate?.setOnClickListener {
                // 날짜 선택 다이얼로그
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)

                val datePickerDialog =
                    DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                        @SuppressLint("SetTextI18n", "SimpleDateFormat", "LogNotTimber")
                        override fun onDateSet(
                            view: DatePicker?,
                            year: Int,
                            month: Int,
                            dayOfMonth: Int
                        ) {
//                            selectedDate = "${year}.${month}.${dayOfMonth}"
                            val cal = GregorianCalendar(year, month, dayOfMonth) // month: 0~11
                            val simpleDateFormat = SimpleDateFormat("yyMMdd")
                            selectedDate = simpleDateFormat.format(cal.time)
                            btnSelectDate.text = selectedDate

                        }

                    }, year, month, date)
                datePickerDialog.show()
            }

            // 운동 데이터 선택
            btnSelectExData?.setOnClickListener {
                val exDataPickerDialog =
                    CustomDialog(this, R.layout.dialog_community_ex_data_picker, "")
                val exDataPickerAlert = exDataPickerDialog.showDialog()

                // 리스트뷰에 추가할 데이터 리스트
                val dataModelList = mutableListOf<ExerciseDataModel>()

                // 리스트뷰 연결
                val listView = exDataPickerAlert?.findViewById<ListView>(R.id.exDataListLV)
                val adapterList = ExDataListAdapter(dataModelList)
                listView?.adapter = adapterList

                dataModelList.clear()

                for (dataModel in DataBasket.individualExData!!.children) {
                    dataModelList.add(dataModel.getValue(ExerciseDataModel::class.java)!!)
                }

            }

            btnPost?.setOnClickListener {

            }

        }

    }
}