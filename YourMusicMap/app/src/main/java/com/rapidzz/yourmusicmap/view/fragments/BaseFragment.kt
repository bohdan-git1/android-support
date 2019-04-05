package com.rapidzz.yourmusicmap.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rapidzz.mymusicmap.other.util.SessionManager
import com.rapidzz.yourmusicmap.R
import com.rapidzz.yourmusicmap.view.dialog.AlertMessageDialog
import dmax.dialog.SpotsDialog

open class BaseFragment: Fragment(){

    lateinit var dialog: AlertDialog
    lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!::dialog.isInitialized) {
            dialog = SpotsDialog.Builder()
                .setContext(activity)
                .setMessage(R.string.label_loading)
                .setCancelable(false)
                .build()
        }
        if (!::sessionManager.isInitialized) {
            sessionManager = SessionManager(context!!)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::dialog.isInitialized) {
            dialog = SpotsDialog.Builder()
                .setContext(activity)
                .setMessage(R.string.label_loading)
                .setCancelable(false)
                .build()
        }
        if (!::sessionManager.isInitialized) {
            sessionManager = SessionManager(context!!)
        }

/*        if(this is WhatsOnFragment || this is MoreFragment) {
            activity?.findViewById<View>(R.id.appbar)?.visibility = View.GONE
        }else */
      /*  if(false){
            activity?.findViewById<View>(R.id.appbar)?.visibility = View.VISIBLE
        }else{
            activity?.findViewById<View>(R.id.appbar)?.visibility = View.GONE
        }*/

       /* if(this is PhotosViewerFragment){
            activity?.findViewById<View>(R.id.navigation)?.visibility = View.GONE
        }else{
            activity?.findViewById<View>(R.id.navigation)?.visibility = View.VISIBLE
        }*/
    }

    fun showProgressDialog(show: Boolean){
        if(dialog != null && show) {
            if(!dialog.isShowing)
                dialog.apply {
                     show()
                }
        }else if(dialog != null && !show){
            if(dialog.isShowing)
                dialog.dismiss()
        }
    }

    fun getSessionManager(context: Context): SessionManager{
        if (!::sessionManager.isInitialized) {
            sessionManager = SessionManager(context!!)
        }
        return sessionManager
    }

    fun setClickEnable(button: View, enabled: Boolean){
        button.isClickable = enabled
        button.isEnabled = enabled
    }

    fun showAlertDialog(msg: String){
        AlertMessageDialog.newInstance(msg).show(childFragmentManager, AlertMessageDialog.TAG)
    }

    fun onBackPress(){
        activity?.onBackPressed()
    }

    fun getSimpleName(): String{
        return this.javaClass.simpleName
    }
}