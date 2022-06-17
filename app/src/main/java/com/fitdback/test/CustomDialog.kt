package com.fitdback.test

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.fitdback.posedetection.R

class CustomDialog(context: Context, xmlSource: Int, title: String) {

    private val mDialogView: View? =
        LayoutInflater.from(context).inflate(xmlSource, null)
    private val mBuilder: AlertDialog.Builder? =
        AlertDialog.Builder(context).setView(mDialogView).setTitle(title)

    fun showDialog(): AlertDialog? {
        return mBuilder!!.show()
    }

}
