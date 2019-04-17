package com.rapidzz.mymusicmap.view.activities

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import com.rapidzz.mymusicmap.other.util.SessionManager
import com.rapidzz.yourmusicmap.R
import com.rapidzz.yourmusicmap.view.dialog.AlertMessageDialog
import dmax.dialog.SpotsDialog
import java.util.*


open class BaseActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager;
    lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        if (!::dialog.isInitialized) {
            dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.label_loading)
                .setCancelable(false)
                .build()
        }

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

    fun showAlertDialog(msg: String){
        AlertMessageDialog.newInstance(msg).show(supportFragmentManager, AlertMessageDialog.TAG)
    }

    fun popCurrenFragment(){
        supportFragmentManager.popBackStack()
    }

    fun replaceFragment(fragment: androidx.fragment.app.Fragment, tag: String, addToStack: Boolean, clearStack: Boolean){
        if(clearStack)
            supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        var ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container,fragment,tag)
        if(addToStack)
            ft.addToBackStack(tag)
        ft.commit()
    }

    /*fun obtainWhatsOnFragment(): WhatsOnFragment{
        if(whatsOnFragment != null)
            return whatsOnFragment!!

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment != null && fragment is WhatsOnFragment && fragment.isComingUpFragment()) {
            whatsOnFragment = fragment
            return whatsOnFragment!!
        }else {
            whatsOnFragment = WhatsOnFragment.newInstance(Query.COMING_UP)
            return whatsOnFragment!!
        }
    }


    fun obtainHereNowFragment(): WhatsOnFragment{
        if(hereAndNowFragment != null)
            return hereAndNowFragment!!

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null && fragment is WhatsOnFragment && fragment.isHereAndNowFragment()) {
            hereAndNowFragment = fragment
            return hereAndNowFragment!!
        }else {
            hereAndNowFragment = WhatsOnFragment.newInstance(Query.HERE_AND_NOW)
            return hereAndNowFragment!!
        }
    }

    fun obtainExploreFragment(): ExploreFragment {
        if (exploreFragment != null)
            return exploreFragment!!

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment != null && fragment is ExploreFragment){
            exploreFragment = fragment
            return exploreFragment!!
        }else {
            exploreFragment = ExploreFragment()
            return exploreFragment!!
        }

    }


    fun obtainFavoriteFragment(): FavoritesFragment{
        if(favoritesFragment != null)
            return favoritesFragment!!

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null && fragment is FavoritesFragment) {
            favoritesFragment = fragment
            return favoritesFragment!!
        }else {
            favoritesFragment = FavoritesFragment()
            return favoritesFragment!!
        }
    }

    fun obtainMoreFragment(): MoreFragment{
        if(moreFragment != null)
            return moreFragment!!

        var fragment = supportFragmentManager.findFragmentById(com.rapidzz.mymusicmap.R.id.fragment_container)

        if (fragment != null && fragment is MoreFragment) {
            moreFragment = fragment
            return moreFragment!!
        } else {
            moreFragment = MoreFragment()
            return moreFragment!!
        }
    }*/

    /*fun obtainMusicDetail(): MusicDetailFragment?{

        var fragment = supportFragmentManager.findFragmentById(com.rapidzz.mymusicmap.R.id.fragment_container)

        if (fragment != null && fragment is MusicDetailFragment) {
            return fragment
        }

        return null
    }*/

    /*fun obtainMusicFragment(): MusicFragment?{

        var fragment = supportFragmentManager.findFragmentById(com.rapidzz.mymusicmap.R.id.fragment_container)

        if (fragment != null && fragment is MusicFragment) {
            return fragment
        }

        return null
    }

    fun getLastSegment(path: String): String{
        val id = path.substring(path.lastIndexOf('/') + 1)
        return id
    }
*/


   /* override fun attachBaseContext(newBase: Context?) {
        //val newLocale = Locale(SessionManager(newBase!!).getLocale())
        //val context = MyContextWrapper.wrapApplication(newBase, newLocale)
        super.attachBaseContext(newBase)
    }*/
}