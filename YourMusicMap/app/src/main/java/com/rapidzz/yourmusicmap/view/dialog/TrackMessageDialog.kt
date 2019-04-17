package com.rapidzz.yourmusicmap.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.rapidzz.yourmusicmap.R
import com.rapidzz.yourmusicmap.other.util.RxBus


class TrackMessageDialog : androidx.fragment.app.DialogFragment() {

    var message: String = ""
    var call: String = ""
    var btnText: String = "Ok"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            arguments?.let {
                message = it.getString(MESSAGE,"")
                call = it.getString(CALL,"")
            }
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.label_success)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->

                        RxBus.defaultInstance().send("")
                        dialog.dismiss()
                        activity!!.supportFragmentManager.popBackStack()
                    })
                .setCancelable(false)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        val TAG: String = this.javaClass.simpleName
        val MESSAGE: String = "message"
        val CALL: String = "call"
        fun newInstance(message: String, call: String): TrackMessageDialog{
            var dialogAlert = TrackMessageDialog()
            var bundle = Bundle()
            bundle.putString(MESSAGE,message)
            bundle.putString(CALL,call)
            dialogAlert.arguments = bundle
            return dialogAlert
        }
    }


}

