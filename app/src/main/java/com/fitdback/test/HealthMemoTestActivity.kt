package com.fitdback.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.fitdback.posedetection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HealthMemoTestActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    val dataModelList = mutableListOf<FeedbackTestDataModel>() // DataModel 리스트 만들기

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_memo_test)

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.database
        val myRef = database.getReference("userExerciseInfo")

        // ListView
        val listView = findViewById<ListView>(R.id.healthMemoListView)

        // Main -> Adapter연결
        val adapter_list = ListViewAdapter(dataModelList)
        listView.adapter = adapter_list

        // DB 불러오기 (사용자별)
        myRef.child(firebaseAuth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) { // snapshot : 모든 데이터를 가져옴

                Log.d("point", dataModelList.toString()) // 로그 계속 찍어보면서 공부
                dataModelList.clear() // 리스트뷰 덧씌워짐 방지
                Log.d("point", dataModelList.toString())

                for (dataModel in snapshot.children) { // 반복문을 이용해 snapshot의 내용을 꺼냄

                    /* Log 결과
                    D/Data: DataSnapshot { key = -N0ZpFxUwo3lpcwibAi2, value = {date=2022, 3, 20, memo=my health} }
                    D/Data: DataSnapshot { key = -N0ZpFxUwo3lpcwibAi2, value = {date=2022, 3, 20, memo=my health} }
                    D/Data: DataSnapshot { key = -N0ZsSSVw4cEgj_Ma8i0, value = {date=, memo=} }
                     */
                    Log.d("Data", dataModel.toString())
                    dataModelList.add(dataModel.getValue(FeedbackTestDataModel::class.java)!!) // 내가 작성한 DataModel의 형태로 잘 들어옴

                }

                // 데이터모델리스트가 다 만들어지면 어댑터를 새롭게 만들어라
                adapter_list.notifyDataSetChanged()

                Log.d("DataModel", dataModelList.toString())

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

}