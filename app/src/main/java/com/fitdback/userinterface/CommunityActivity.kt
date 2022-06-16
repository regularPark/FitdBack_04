package com.fitdback.userinterface

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.fitdback.database.DataBasket
import com.fitdback.database.datamodel.ExerciseDataModel
import com.fitdback.posedetection.R
import com.fitdback.test.CustomDialog
import com.fitdback.userinterface.listviewadpater.ExDataListAdapter
import java.text.SimpleDateFormat
import java.util.*

class CommunityActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
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

                if (selectedDate == null) { // 날짜를 먼저 선택했는지 널 체크

                    Toast.makeText(this, "먼저 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()

                } else {

                    // 리스트뷰에 추가할 Array
                    val exDataArray = mutableListOf<ExerciseDataModel>()
                    for (i in 0..2) {
                        exDataArray.add(i, ExerciseDataModel())
                    }
                    exDataArray[0].ex_type = "squat"
                    exDataArray[1].ex_type = "plank"
                    exDataArray[2].ex_type = "sideLateralRaise"

                    Log.d("post_test", exDataArray.toString())

                    // 날짜별 & 운동 타입별 운동 데이터 업데이트
                    for (dataModel in DataBasket.individualExData!!.children) {

                        val tempDataModel = dataModel.getValue(ExerciseDataModel::class.java)

                        if (tempDataModel != null && tempDataModel.ex_date.equals(selectedDate)) { // 날짜 필터링

                            when (tempDataModel.ex_type) {
                                "squat" -> exDataArray[0].ex_count += tempDataModel.ex_count
                                "plank" -> exDataArray[1].ex_time += tempDataModel.ex_time
                                "sideLateralRaise" -> exDataArray[2].ex_count += tempDataModel.ex_count
                            }
                        }
                    }

                    // 리스트뷰에 추가할 데이터 리스트
                    val dataModelList = mutableListOf<ExerciseDataModel>()

                    // 스쿼트나 사이드래터럴레이즈가 0회, 플랭크가 0초인 경우는 데이터 리스트에 추가하지 않는다.
                    var dataCount = 0
                    for (dataModel in exDataArray) {

                        if (!checkIfZeroDataExists(dataModel)) { // zero data가 없을 때 리스트에 add
                            dataModelList.add(dataModel)
                            dataCount++
                            Log.d("post_test", dataModel.toString())
                        }

                    }

                    // 선택한 날짜의 운동 데이터가 있으면 다이얼로그를 띄운다.
                    if (dataCount != 0) {
                        val exDataPickerDialog =
                            CustomDialog(
                                this,
                                R.layout.dialog_community_ex_data_picker,
                                "$selectedDate  운동 기록"
                            )
                        val exDataPickerAlert = exDataPickerDialog.showDialog()

                        // 리스트뷰 연결
                        val listView = exDataPickerAlert?.findViewById<ListView>(R.id.exDataListLV)
                        val adapterList = ExDataListAdapter(dataModelList)
                        listView?.adapter = adapterList

//                        dataModelList.clear()
                    } else {
                        Toast.makeText(
                            this,
                            "${selectedDate}에 해당하는 운동 데이터가 없습니다. 먼저 운동을 해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } // else

            }

            btnPost?.setOnClickListener {

            }

        }

    } // fun onCreate()

    private fun checkIfZeroDataExists(dataModel: ExerciseDataModel): Boolean {

        val targetData = if (dataModel.ex_type == "plank") {
            dataModel.ex_time
        } else {
            dataModel.ex_count
        }

        return targetData == 0 // if targetData == 0: return true (zero data exists)

    }

} // CommunityActivity