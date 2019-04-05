package com.rapidzz.mymusicmap.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.rapidzz.mymusicmap.other.extensions.gotoGlobalNavigationActivity
import com.rapidzz.mymusicmap.other.extensions.replaceFragmentInActivity
import com.rapidzz.yourmusicmap.BuildConfig
import com.rapidzz.yourmusicmap.R
import com.rapidzz.yourmusicmap.view.fragments.LoginFragment
import com.rapidzz.yourmusicmap.view.fragments.SplashFragment


class LandingActivity : BaseActivity() {

    companion object {
        val SPLASH_DELAY: Long = 3000
        val SKIP_SPLASH: String = "skip_splash"
        val START_UP_MESSAGe: String = "start_up_message"
    }
    var handler: Handler? = null
    var runnable: Runnable? = null

    lateinit var mCallbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val skipSplash = intent.getBooleanExtra(SKIP_SPLASH,false)
        val startMessage = intent.getStringExtra(START_UP_MESSAGe)
        if(!startMessage.isNullOrEmpty()){
                showAlertDialog(startMessage)
        }
        startSplash(skipSplash);
    }

    fun startSplash(skip:Boolean) {
        if(skip) {
            if (sessionManager.isLoggedIn()) {
                //replaceFragmentInActivity(obtainHomeFragment(), R.id.fragment_container, true, false)
                gotoGlobalNavigationActivity()
            } else {
                //replaceFragmentInActivity(obtainLoginFragment(), R.id.fragment_container, true, false)
            }
        }else{
            replaceFragmentInActivity(obtainSplashFragment(), R.id.fragment_container, true, false);
            handler = Handler()
            runnable = Runnable {
                if (sessionManager.isLoggedIn()) {
                    //replaceFragmentInActivity(obtainHomeFragment(), R.id.fragment_container, true, false)
                    gotoGlobalNavigationActivity()
                } else {
                    replaceFragmentInActivity(obtainLoginFragment(), R.id.fragment_container, true, false)
                }
            }
            handler!!.postDelayed(runnable, SPLASH_DELAY)
        }
    }

    fun isSplashRunning(): Boolean{
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        return (fragment  != null && fragment is SplashFragment)
    }

    override fun onPause() {
        super.onPause()
        if(handler != null && runnable != null){
                handler!!.removeCallbacks(runnable)
        }
    }

    override fun onResume() {
        super.onResume()
        if(handler != null && runnable != null && isSplashRunning()){
            handler!!.postDelayed(runnable,SPLASH_DELAY)
        }
    }

    private fun obtainSplashFragment(): SplashFragment {
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(fragment  != null && fragment is SplashFragment)
            return fragment
        else
            return SplashFragment()
    }

    private fun obtainLoginFragment(): LoginFragment {
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(fragment  != null && fragment is LoginFragment)
            return fragment
        else
            return LoginFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

    }
/*
    private fun obtainHomeFragment(): HomeFragment {
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(fragment  != null && fragment is HomeFragment)
            return fragment
        else
            return HomeFragment()
    }*/
}
