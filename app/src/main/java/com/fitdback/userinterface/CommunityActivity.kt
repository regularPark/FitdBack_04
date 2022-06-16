package com.fitdback.userinterface

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.fitdback.database.DataBasket
import com.fitdback.database.datamodel.ExerciseDataModel
import com.fitdback.database.datamodel.PostDataModel
import com.fitdback.database.datamodel.UserInfoDataModel
import com.fitdback.posedetection.R
import com.fitdback.test.CustomDialog
import com.fitdback.userinterface.listviewadpater.ExDataListAdapter
import com.fitdback.userinterface.listviewadpater.PostAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CommunityActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("LogNotTimber", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {

        firebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.database

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val btnWritePost = findViewById<ImageView>(R.id.btnWritePost)

        // post list
        val listView = findViewById<ListView>(R.id.postLV)

        val dataModelList = mutableListOf<PostDataModel>()
        val adapterList = PostAdapter(dataModelList)
        listView?.adapter = adapterList

        val dbPath = database.getReference("posts")

        // get data from Database
        dbPath.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                dataModelList.clear()
                for (dataModel in snapshot.children) {
                    dataModelList.add(dataModel.getValue(PostDataModel::class.java)!!)
                }

                adapterList.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        // Post 아이콘을 클릭하여 날짜를 선택
        btnWritePost.setOnClickListener {

            // community_posting 다이얼로그
            val postingDialog = CustomDialog(this, R.layout.dialog_community_posting, "Posting")
            val postingAlertDialog = postingDialog.showDialog()

            val memoArea = postingAlertDialog?.findViewById<EditText>(R.id.memoArea)
            val btnSelectDate = postingAlertDialog?.findViewById<Button>(R.id.btnSelectDate)
            val btnSelectExData = postingAlertDialog?.findViewById<Button>(R.id.btnSelectExData)
            val btnPost = postingAlertDialog?.findViewById<Button>(R.id.btnPost)

            var selectedDate: String? = null
            var exData: String? = null
            var post_ex_history: String? = null // selectDate + exData
            var post_memo: String? = null

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

                        // 리스트뷰의 운동 데이터를 선택하고 운동 데이터 선택 버튼의 텍스트를 해당 정보로 변경
                        // 변경 이후 다이얼로그 종료
                        listView?.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                val selectItem =
                                    parent.getItemAtPosition(position) as ExerciseDataModel
//                                Toast.makeText(this, getString(selectItem), Toast.LENGTH_SHORT).show()
                                btnSelectExData.text = getString(selectItem)
                                exData = getString(selectItem)
                                exDataPickerAlert?.dismiss()
                            }

//                        dataModelList.clear()
                    } else {
                        Toast.makeText(
                            this,
                            "${selectedDate}에 해당하는 운동 데이터가 없습니다. 먼저 운동을 해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } // else

            } // btnSelectExData?.setOnClickListener

            // 게시 버튼
            btnPost?.setOnClickListener {
                // memo 텍스트
                post_memo = memoArea?.text.toString().trim()
                Log.d("post_test", post_memo!!.isEmpty().toString())

                if (post_memo!!.isNotEmpty() && selectedDate != null && exData != null) {

                    // 현재 날짜 & 시간 계산
                    val mNow = System.currentTimeMillis()
                    val mDate = Date(mNow)
                    val currentTime = SimpleDateFormat("yyMMdd HH:mm:ss").format(mDate)
                    Log.d("post_test", currentTime)

                    // post 데이터 모델 만들고
                    val myUserInfoDataModel =
                        DataBasket.individualUserInfo?.getValue(UserInfoDataModel::class.java)
                    post_ex_history = "$selectedDate $exData"

                    val postDataModel = PostDataModel(
                        currentTime,
                        myUserInfoDataModel?.user_nickname,
                        post_ex_history,
                        post_memo
                    )

                    // DB에 추가
                    val postDBPath = database.getReference("posts")
                    postDBPath.push().setValue(postDataModel)
                        .addOnSuccessListener {
                            Toast.makeText(this, "게시글 저장 완료", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error: 게시글 저장실패", Toast.LENGTH_SHORT)
                                .show()
                        }

                    // Posting 다이얼로그 dismiss
                    postingAlertDialog.dismiss()

                } else {
                    // TODO : 예외 처리
                }

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

    private fun getString(exerciseDataModel: ExerciseDataModel): String {

        return when (exerciseDataModel.ex_type) {
            "squat" -> {
                "스쿼트 ${exerciseDataModel.ex_count}회"
            }
            "plank" -> {
                "플랭크 ${formatTime(exerciseDataModel.ex_time)}"
            }
            else -> {
                "래터럴레이즈 ${exerciseDataModel.ex_count}회"
            }
        }

    }

    private fun formatTime(time: Int): String {
        val minute = time / 60
        val sec = time % 60

        return if (minute == 0) {
            "${sec}초"
        } else {
            "${minute}분 ${sec}초"
        }
    }

} // CommunityActivity