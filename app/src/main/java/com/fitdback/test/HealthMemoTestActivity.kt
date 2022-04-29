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
        val userID = firebaseAuth.currentUser!!.uid

        // ListView
        val listView = findViewById<ListView>(R.id.healthMemoListView)

        // Main -> Adapter연결
        val adapter_list = ListViewAdapter(dataModelList)
        listView.adapter = adapter_list

        Log.d("data1", dataModelList.toString())

        // DB 불러오기
        database.getReference("users")
            .child(userID)
            .child("exerciseInfo")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d("data2", dataModelList.toString())
                    dataModelList.clear() // 리스트뷰 덧씌워짐 방지

                    for (dataSet in snapshot.children) { // uid/
                        Log.d("data3", dataSet.toString())
                        dataModelList.add(dataSet.getValue(FeedbackTestDataModel::class.java)!!) // 내가 작성한 DataModel의 형태로 잘 들어옴
                        Log.d("data4", dataModelList.toString())
                    }

                    // 데이터모델리스트가 다 만들어지면 어댑터를 새롭게 만들어라
                    adapter_list.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                    Log.w("loadPost:onCancelled", error.toException())

                }

            })

    }

}