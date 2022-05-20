package com.fitdback.test

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fitdback.database.DataBasket
import com.fitdback.database.datamodel.ExerciseDataModel
import com.fitdback.database.datamodel.FriendDataModel
import com.fitdback.database.datamodel.UserInfoDataModel
import com.fitdback.posedetection.R
import com.fitdback.test.barChartTest.BarChartTestActivity
import com.fitdback.test.feedbackTest.FeedbackTestActivity
import com.fitdback.userinterface.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


/*
22.04.26 일종의 개발자 옵션
- 기능 테스트 용도
 */

@SuppressLint("LogNotTimber")
class DevModeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_mode)

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.database
        val myRef = database.getReference("userExerciseInfo")

        // 레이아웃 연결
        val userIDArea = findViewById<TextView>(R.id.userIDArea)
        val btnDBTest = findViewById<Button>(R.id.btnDBTest)
        val btnFeedbackTest = findViewById<Button>(R.id.btnFeedbackTest)
        val btnDataReadTest = findViewById<Button>(R.id.btnDataReadTest)
        val btnBarChartTest = findViewById<Button>(R.id.btnBarChartTest)
        val btnCreateDummyData = findViewById<Button>(R.id.btnCreateDummyData)
        val btnRunFriendMode = findViewById<Button>(R.id.btnRunFriendMode)

        // Intent
        val toHealthMemoTestActivity = Intent(this, HealthMemoTestActivity::class.java)
        val toFeedbackTestActivity = Intent(this, FeedbackTestActivity::class.java)

        // 로그인에 성공해서 현재 액티비티로 전환되면, user의 ID를 화면에 표시
        userIDArea.text = firebaseAuth.currentUser?.uid


        /*
            버튼 관련 동작
        */

        // btnDBTest
        btnDBTest.setOnClickListener {

            startActivity(toHealthMemoTestActivity)

        }

        // Data Write Test
        btnFeedbackTest.setOnClickListener {

            val dialog = CustomDialog(this, R.layout.dialog_data_read_write_test, "testing")
            val mAlertDialog = dialog.showDialog()

            val btnToFeedbackTestActivity =
                mAlertDialog?.findViewById<Button>(R.id.btnToFeedbackTestActivity) // mAlertDialog에서 찾아야함!!

            btnToFeedbackTestActivity?.setOnClickListener { // nullable type(?을 붙여줌) 붙여줘야 'pipe:qemud:wififorward' service 에러가 안남

//                Toast.makeText(this, "버튼 클릭됨", Toast.LENGTH_SHORT).show()

                // <EditText>에서 텍스트 가져오기
                val ex_date: String =
                    mAlertDialog.findViewById<EditText>(R.id.ex_date_area)?.text.toString()

                val ex_type: String =
                    mAlertDialog.findViewById<EditText>(R.id.ex_type_area)?.text.toString()

                val ex_time: Int =
                    mAlertDialog.findViewById<EditText>(R.id.ex_time_area)?.text.toString().toInt()

                val ex_count: Int =
                    mAlertDialog.findViewById<EditText>(R.id.ex_count_area)?.text.toString().toInt()

                val ex_success_count: Int =
                    mAlertDialog.findViewById<EditText>(R.id.ex_success_count_area)?.text.toString()
                        .toInt()

                val ex_calorie: Int =
                    mAlertDialog.findViewById<EditText>(R.id.ex_calorie_area)?.text.toString()
                        .toInt()

                // DB에 저장할 모델 생성
                val ex_datamodel = ExerciseDataModel(
                    ex_date,
                    ex_type,
                    ex_time,
                    ex_count,
                    ex_success_count,
                    ex_calorie
                )

                Log.d("datamodel", ex_datamodel.toString())

                // 데이터 저장 path 지정
                val dbPath =
                    DataBasket.getDBPath("users", "ex_data", true)

                dbPath?.push()?.setValue(ex_datamodel)

                mAlertDialog.dismiss()

            }

        }

        // barChart manipulation Test
        btnBarChartTest.setOnClickListener {

            val dbPath = DataBasket.getDBPath("users", "ex_data", true)
            DataBasket.getDataFromFB(dbPath!!, "individualExData")

            Handler().postDelayed({
                startActivity(Intent(this, BarChartTestActivity::class.java))
            }, 500)

        }

        /*
           Create Dummy Data

           다이얼로그가 떠서 -> 날짜 선택
            -> 해당하는 날짜 길이만큼 날짜 List 로드.
            -> 랜덤을 이용하여 ExerciseDataModel을 날짜 개수 만큼 만듦(리스트에 저장)
            -> 해당 리스트를 하나씩 DB에 write

         */
        btnCreateDummyData.setOnClickListener {

            val dialog = CustomDialog(this, R.layout.dialog_create_dummy_data, "testing")
            val mAlertDialog = dialog.showDialog()
            val btnSelectDate = mAlertDialog!!.findViewById<Button>(R.id.btnSelectFirstDate)
//            val btnSelectSecondDate = mAlertDialog.findViewById<Button>(R.id.btnSelectSecondDate)
            val btnDBDummyDataWrite = mAlertDialog.findViewById<Button>(R.id.btnDBDummyDataWrite)
            val btnSetExTypeToSquat = mAlertDialog.findViewById<Button>(R.id.btnSetExTypeToSquat)
            val btnSetExTypeToPlank = mAlertDialog.findViewById<Button>(R.id.btnSetExTypeToPlank)
            val btnSetExTypeToSideLateralRaise =
                mAlertDialog.findViewById<Button>(R.id.btnSetExTypeToSideLateralRaise)
//            var dateText: String = ""

            var selectedExType: String? = null

            btnSelectDate?.setOnClickListener {

                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)

                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        btnSelectDate.text = "${year},${month + 1},${dayOfMonth}"
//                        dateText = "${year}${month}${dayOfMonth}"
                    }

                }, year, month, date)

                dlg.show()

            }

            btnSetExTypeToSquat?.setOnClickListener {

                selectedExType = "squat"
                btnDBDummyDataWrite!!.setText("일주일 전부터 위 날짜까지 $selectedExType 데이터 생성")

            }

            btnSetExTypeToPlank?.setOnClickListener {

                selectedExType = "plank"
                btnDBDummyDataWrite?.text = "일주일 전부터 위 날짜까지 $selectedExType 데이터 생성"

            }

            btnSetExTypeToSideLateralRaise?.setOnClickListener {

                selectedExType = "sideLateralRaise"
                btnDBDummyDataWrite?.text = "6일전부터 위 날짜까지 $selectedExType 데이터 생성"

            }

            btnDBDummyDataWrite?.setOnClickListener {

                val dateTextArray = btnSelectDate?.text!!.split(",").toMutableList()

                val year = dateTextArray[0].toInt()
                val month = dateTextArray[1].toInt() - 1
                val date = dateTextArray[2].toInt()

                val dateList = DataBasket.getOneWeekListFromDate(year, month, date, "Before")

//                Log.d("select_date", "getOneWeekFromDate() : $dateList")
                val exerciseDataModelList = createDummyData(dateList, selectedExType!!)
//                Log.d("dummy", "exerciseDataModelList $exerciseDataModelList")

                val dbPath = DataBasket.getDBPath("users", "ex_data", true)

                // DB Writing
                for (dataModel in exerciseDataModelList) {
                    dbPath!!.push().setValue(dataModel)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "${dataModel.ex_date} 데이터 저장완료.",
                                Toast.LENGTH_SHORT
                            ).show()
                            mAlertDialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "데이터가 저장실패", Toast.LENGTH_SHORT).show()
                        }
                }


            } // btnDBDummyDataWrite

        } // btnCreateDummyData

        // Friend Mode
        btnRunFriendMode.setOnClickListener {

            val friendModeDialog = CustomDialog(this, R.layout.dialog_friend_mode, "testing")
            val friendModeAlertDialog = friendModeDialog.showDialog()
            val btnShowFriendList =
                friendModeAlertDialog!!.findViewById<Button>(R.id.btnShowFriendList)
            val btnRegisterFriend =
                friendModeAlertDialog.findViewById<Button>(R.id.btnRegisterFriend)
            val btnCheckMyCode = friendModeAlertDialog.findViewById<Button>(R.id.btnCheckMyCode)
            val btnFriendModeConfirm =
                friendModeAlertDialog.findViewById<Button>(R.id.btnFriendModeConfirm)
            friendModeAlertDialog.setCancelable(false)

            // 친구 목록 보기
            btnShowFriendList?.setOnClickListener {
                // TODO : 친구 목록 만들기
                
                
            }

            // 친구 등록
            btnRegisterFriend?.setOnClickListener {
                val registerDialog =
                    CustomDialog(this, R.layout.dialog_friend_register, "Friend Register")
                val registerAlertDialog = registerDialog.showDialog()
                registerAlertDialog?.setCancelable(false)

                val friendCodeArea =
                    registerAlertDialog?.findViewById<EditText>(R.id.friendCodeArea)
                val friendNicknameArea =
                    registerAlertDialog?.findViewById<TextView>(R.id.friendNicknameArea)
                val btnRegisterFriendToDB =
                    registerAlertDialog?.findViewById<Button>(R.id.btnRegisterFriendToDB)
                val btnRegisterConfirm =
                    registerAlertDialog?.findViewById<Button>(R.id.btnRegisterConfirm)

                var isFirstClick = true
                var friendNickname: String? = null
                var friendCode: String? = null

                // 친구를 DB에 저장
                btnRegisterFriendToDB?.setOnClickListener {

                    if (isFirstClick) {   // 검색 기능 실행 -> "[닉네임]님을 찾았습니다."
                        friendCode = friendCodeArea?.text?.toString()?.trim()
                        val dbPath =
                            database.getReference("users").child(friendCode!!).child("user_info")

                        dbPath.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                dataSnapshot.getValue(FriendDataModel::class.java)
                                val userInfoModel =
                                    dataSnapshot.getValue(UserInfoDataModel::class.java)
                                friendNickname = userInfoModel?.user_nickname
                                Log.d("db_data", dataSnapshot.toString())
                                Log.d(
                                    "db_data",
                                    dataSnapshot.getValue(UserInfoDataModel::class.java).toString()
                                )

                                friendNicknameArea?.text = "${friendNickname}님을 찾았습니다."
                                btnRegisterFriendToDB.text = "친구로 등록하기"
                                isFirstClick = false
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("db_data", "onCancelled")
                            }
                        })

                    } else {
                        // 등록 기능 실행 -> DB에 저장
                        if (friendCode != null && friendNickname != null) {

                            val friendDataModel =
                                FriendDataModel(friendCode, friendNickname)  // data model 생성
                            val dbPath = database.getReference("users")
                                .child(firebaseAuth.currentUser?.uid!!).child("friend_info")

                            // data write
                            dbPath.push().setValue(friendDataModel)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "친구 등록 완료", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error: 친구 등록 실패", Toast.LENGTH_SHORT)
                                        .show()
                                }

                        } else {
                            Toast.makeText(
                                this,
                                "Error: 친구 코드 또는 닉네임을 찾을 수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }

                btnRegisterConfirm?.setOnClickListener {

                    registerAlertDialog.dismiss()

                }

            }

            // 내 친구 코드 확인
            btnCheckMyCode?.setOnClickListener {

                val myCodeDialog = CustomDialog(this, R.layout.dialog_friend_my_code, "")
                val myCodeAlertDialog = myCodeDialog.showDialog()
                val myCodeArea = myCodeAlertDialog?.findViewById<TextView>(R.id.myCodeArea)
                val btnMyCodeConfirm =
                    myCodeAlertDialog?.findViewById<Button>(R.id.btnMyCodeConfirm)
                myCodeAlertDialog?.setCancelable(false)

                myCodeArea?.text = firebaseAuth.currentUser?.uid

                // 클립보드 복사하기
                val clipboardManager: ClipboardManager =
                    getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData: ClipData = ClipData.newPlainText("myCode", myCodeArea?.text)
                clipboardManager.primaryClip = clipData
                Toast.makeText(
                    this,
                    "회원코드 \"${myCodeArea?.text}\"이 클립보드에 저장되었습니다.",
                    Toast.LENGTH_LONG
                ).show()

                btnMyCodeConfirm?.setOnClickListener {
                    myCodeAlertDialog.dismiss()
                }

            }

            btnFriendModeConfirm?.setOnClickListener {

                friendModeAlertDialog.dismiss()

            }
        }


    }

//    @SuppressLint("SimpleDateFormat")
//    private fun getOneWeekFromDate(year: Int, month: Int, date: Int): MutableList<String> {
//
//        val dateList = mutableListOf<String>()
//
//        val cal = GregorianCalendar(year, month, date) // month: 0~11
//        val simpleDateFormat = SimpleDateFormat("yyMMdd")
//
//        cal.add(GregorianCalendar.DATE, -7)
//
//        for (i in 0..6) {
//            cal.add(GregorianCalendar.DATE, +1)
//            dateList.add(simpleDateFormat.format(cal.time))
//        }
//
//        return dateList
//
//    }

    // 운동 데이터 모델을 firebase에 write
    /*
        squat 개수를 랜덤으로 생성 (10 ~ 50개)
            운동시간은 개수의 2배
            성공 개수는 개수의 1/2배
            운동시간은 개수의 2.0배 ~ 3.0배
        Firebase에 저장 
     */
    private fun createDummyData(
        dateList: MutableList<String>,
        exType: String
    ): MutableList<ExerciseDataModel> {

        val exerciseDataModelList = mutableListOf<ExerciseDataModel>()

        for (i in 0..6) {
            val exDate = dateList[i]
            val exCount = rand(1, 50, "int")[0].toInt()
            val exTime = (exCount.toFloat() * rand(4, 0, "float")[0].toFloat()).toInt()
            val exSuccessCount = (exCount * 0.5).toInt()
            val exCalorie = (exCount * 0.5).toInt()

            val exerciseDataModel =
                ExerciseDataModel(
                    exDate,
                    exType,
                    exTime,
                    exCount,
                    exSuccessCount,
                    exCalorie
                )
            exerciseDataModelList.add(exerciseDataModel)
        }

        Log.d("dummy", "exerciseDataModelList $exerciseDataModelList")
        return exerciseDataModelList

    }

    private fun rand(
        from: Int,
        to: Int,
        type: String
    ): MutableList<String> { // from ~ to 사이의 정수를 출력

        val random = Random()
        val mutableList = mutableListOf<String>()

        when (type) {
            "int" -> mutableList.add((random.nextInt(to - from) + from).toString())
            "float" -> mutableList.add((random.nextFloat() + from.toFloat()).toString())
        }

        return mutableList

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}