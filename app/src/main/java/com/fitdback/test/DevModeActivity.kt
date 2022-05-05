package com.fitdback.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fitdback.database.DataBasket
import com.fitdback.database.ExerciseDataModel
import com.fitdback.posedetection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate


/*
22.04.26 일종의 개발자 옵션
- 기능 테스트 용도
 */

class DevModeActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

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
        val btnBarChartTest2 = findViewById<Button>(R.id.btnBarChartTest2)

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
                    DataBasket.getDBPath(firebaseAuth, database, "users", "ex_data", true)

                dbPath?.push()?.setValue(ex_datamodel)

                mAlertDialog.dismiss()

            }

        }


        // Data Read Test
        btnDataReadTest.setOnClickListener {

            val maxOfDailyExCount: Int = 0
            val dailyExCountList = mutableListOf<Int>()

            val databasePath =
                DataBasket.getDBPath(firebaseAuth, database, "users", "ex_data", true)

            databasePath!!.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val dateOfWeekList = DataBasket.getDateOfWeek()
                    val exCountMap = mutableMapOf<String, Int>()

                    for (exDataSet in dataSnapshot.children) {

                        val exData = exDataSet.getValue(ExerciseDataModel::class.java)

                        // exCountMap에 <"yyMMdd", sumOfExCount> key-value 형태로 update
                        if (exData!!.ex_type.equals("squat")) {

                            when (exData.ex_date) {

                                dateOfWeekList[0] -> {
                                    DataBasket.updateMap(
                                        exCountMap,
                                        dateOfWeekList[0],
                                        exData.ex_count
                                    )
                                }
                                dateOfWeekList[1] -> {
                                    DataBasket.updateMap(
                                        exCountMap,
                                        dateOfWeekList[1],
                                        exData.ex_count
                                    )
                                }
                                dateOfWeekList[2] -> {
                                    DataBasket.updateMap(
                                        exCountMap,
                                        dateOfWeekList[2],
                                        exData.ex_count
                                    )
                                }
                                dateOfWeekList[3] -> {
                                    DataBasket.updateMap(
                                        exCountMap,
                                        dateOfWeekList[3],
                                        exData.ex_count
                                    )
                                }
                                dateOfWeekList[4] -> {
                                    DataBasket.updateMap(
                                        exCountMap,
                                        dateOfWeekList[4],
                                        exData.ex_count
                                    )
                                }
                                dateOfWeekList[5] -> {
                                    DataBasket.updateMap(
                                        exCountMap,
                                        dateOfWeekList[5],
                                        exData.ex_count
                                    )
                                }
                                dateOfWeekList[6] -> {
                                    DataBasket.updateMap(
                                        exCountMap,
                                        dateOfWeekList[6],
                                        exData.ex_count
                                    )
                                }

                            }

                        }

                    }

                    Log.d("exData", dateOfWeekList.toString())
                    Log.d("exData", exCountMap.toString())

                }

                override fun onCancelled(error: DatabaseError) {

                    Log.d("exData", "DB Read Error")

                }

            })

        }

        // barChart manipulation Test
        btnBarChartTest.setOnClickListener {

            val dialog = CustomDialog(this, R.layout.dialog_test_bar_chart, "bar chart")
            val mAlertDialog = dialog.showDialog()

            val barChart: BarChart? = mAlertDialog?.findViewById(R.id.testBarChart)

            val entries = ArrayList<BarEntry>()
            entries.add(BarEntry(1.2f, 20.0f))
            entries.add(BarEntry(2.2f, 70.0f))
            entries.add(BarEntry(3.2f, 30.0f))
            entries.add(BarEntry(4.2f, 90.0f))
            entries.add(BarEntry(5.2f, 70.0f))
            entries.add(BarEntry(6.2f, 30.0f))
            entries.add(BarEntry(7.2f, 90.0f))

            barChart!!.run {
                description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
                setMaxVisibleValueCount(7) // 최대 보이는 그래프 개수를 7개로 지정
                setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
                setDrawBarShadow(false) //그래프의 그림자
                setDrawGridBackground(false)//격자구조 넣을건지
                axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                    axisMaximum = 101f //100 위치에 선을 그리기 위해 101f로 맥시멈값 설정
                    axisMinimum = 0f // 최소값 0
                    granularity = 50f // 50 단위마다 선을 그리려고 설정.
                    setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
                    setDrawGridLines(false) //격자 라인 활용
                    setDrawAxisLine(true) // 축 그리기 설정
                    axisLineColor = ContextCompat.getColor(context, R.color.white) // 축 색깔 설정
                    gridColor = ContextCompat.getColor(context, R.color.white) // 축 아닌 격자 색깔 설정
                    textColor = ContextCompat.getColor(context, R.color.white) // 라벨 텍스트 컬러 설정
                    textSize = 13f //라벨 텍스트 크기
                }
                xAxis.run {
                    position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                    granularity = 1f // 1 단위만큼 간격 두기
                    setDrawAxisLine(true) // 축 그림
                    setDrawGridLines(false) // 격자
                    textColor = ContextCompat.getColor(context, R.color.white) //라벨 색상
                    textSize = 12f // 텍스트 크기
                    valueFormatter =
                        DevModeActivity().MyXAxisFormatter() // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
                }
                axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
                setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
                animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
                legend.isEnabled = false //차트 범례 설정
            }

        }
        btnBarChartTest2.setOnClickListener {
            startActivity(Intent(this, BarChartTestActivity::class.java))
        }

    }

    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("1차", "2차", "3차", "4차", "5차", "6차", "7차")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
        }
    }

}