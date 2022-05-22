package com.fitdback.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog

class CustomDialog(context: Context, xmlSource: Int, title: String) {

    private val mDialogView: View? =
        LayoutInflater.from(context).inflate(xmlSource, null)
    private val mBuilder: AlertDialog.Builder? =
        AlertDialog.Builder(context).setView(mDialogView).setTitle(title)

    fun showDialog(): AlertDialog? {
        return mBuilder!!.show()
    }

}
