package com.fitdback.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.fitdback.posedetection.R

class FeedbackTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_test)

        // 다이얼로그
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_exercise_data_write, null)
        val mBuilder =
            AlertDialog.Builder(this).setView(mDialogView).setTitle("FeedbackTest 다이얼로그")

        val mAlertDialog = mBuilder.show()

        val btnToFeedbackTestActivity =
            mAlertDialog.findViewById<Button>(R.id.btnDataWrite) // mAlertDialog에서 찾아야함!!

    }

}