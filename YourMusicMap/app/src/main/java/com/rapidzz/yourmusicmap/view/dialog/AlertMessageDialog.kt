package com.rapidzz.yourmusicmap.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.rapidzz.yourmusicmap.R


class AlertMessageDialog : DialogFragment() {

    var message: String = ""
    var btnText: String = "Ok"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            arguments?.let {
                message = it.getString(MESSAGE,"")
            }
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.label_alert)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                .setCancelable(false)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        val TAG: String = this.javaClass.simpleName
        val MESSAGE: String = "message"
        fun newInstance(message: String): AlertMessageDialog{
            var dialogAlert = AlertMessageDialog()
            var bundle = Bundle()
            bundle.putString(MESSAGE,message)
            dialogAlert.arguments = bundle
            return dialogAlert
        }
    }


}

