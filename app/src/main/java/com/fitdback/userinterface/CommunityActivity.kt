package com.fitdback.userinterface

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import com.fitdback.posedetection.R
import com.fitdback.test.CustomDialog
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
            val btnPost = postingAlertDialog?.findViewById<Button>(R.id.btnPost)

            val ex_history: String? = null
            val user_memo: String? = null
            val isDateSelected: Boolean = false

            // 날짜 선택
            btnSelectDate?.setOnClickListener {
                if (!isDateSelected) { // 날짜 선택한 적이 없는 경우

                    // 날짜 선택 다이얼로그
                    val today = GregorianCalendar()
                    val year: Int = today.get(Calendar.YEAR)
                    val month: Int = today.get(Calendar.MONTH)
                    val date: Int = today.get(Calendar.DATE)

                    val datePickerDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                        @SuppressLint("SetTextI18n", "SimpleDateFormat", "LogNotTimber")
                        override fun onDateSet(
                            view: DatePicker?,
                            year: Int,
                            month: Int,
                            dayOfMonth: Int
                        ) {
                            var selectedDate = "${year},${month},${dayOfMonth}"

                            // selectedDate -> yyMMdd 형태로 변환
//                            val cal = GregorianCalendar(year, month, dayOfMonth) // month: 0~11
//                            val simpleDateFormat = SimpleDateFormat("yyMMdd")
//                            selectedDate = simpleDateFormat.format(cal.time)
                            Log.d("memo", selectedDate)

                            // 선택한 날짜의 운동 데이터를 보여주는 다이얼로그
                        }

                    }, year, month, date)

                    datePickerDialog.show()

                }
            }

            btnPost?.setOnClickListener {

            }

        }

    }
}