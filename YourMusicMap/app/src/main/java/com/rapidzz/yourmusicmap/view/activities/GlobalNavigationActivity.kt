package com.rapidzz.mymusicmap.view.activities

import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.media.session.MediaButtonReceiver.handleIntent
import android.util.Log
import android.view.View
import com.google.common.eventbus.Subscribe
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent
import com.rapidzz.mymusicmap.other.extensions.obtainViewModel
import com.rapidzz.yourmusicmap.R
import kotlinx.android.synthetic.main.activity_global_navigation.*
import kotlinx.android.synthetic.main.app_bar_main.*


open class GlobalNavigationActivity : BaseActivity() {
   /* lateinit var viewModel: MainViewModel
    var mDeeplink = ""

    lateinit var musicLibrary: MusicLibrary*/


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
           /* R.id.navigation_whats_on -> {
                val f = obtainWhatsOnFragment()
                replaceFragment(f, f.getSimpleName(), false, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_here_now -> {
                val f = obtainHereNowFragment()
                replaceFragment(f, f.getSimpleName(), false, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_explore -> {
                val f = obtainExploreFragment()
                replaceFragment(f, f.getSimpleName(), false, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                val f = obtainFavoriteFragment()
                replaceFragment(f, f.getSimpleName(), false, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                val f = obtainMoreFragment()
                replaceFragment(f, f.getSimpleName(), false, true)
                return@OnNavigationItemSelectedListener true
            }*/
        }
        false
    }

    fun setupNavigation() {
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_navigation)
        //FirebaseAnalytics.getInstance(this)
        //setSupportActionBar(toolbar)

        //musicLibrary = MusicLibrary(applicationContext)

        setupNavigation()

        //var f = obtainWhatsOnFragment()
        //replaceFragment(f, f.getSimpleName(), false, true)


    }


    override fun onResume() {
        super.onResume()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
       // if (intent != null)
          //  handleIntent(intent)
    }



    var serviceBound = false

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("serviceStatus", serviceBound)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceBound = savedInstanceState.getBoolean("serviceStatus")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            //service is active
        }
    }

    override fun onStart() {
        super.onStart()
    //    EventBusFactory.getEventBus().register(this)
    }

    override fun onPause() {
        super.onPause()
        try {

        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

}
