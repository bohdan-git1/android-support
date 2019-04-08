package com.rapidzz.mymusicmap.other.extensions
/**
 * Various extension functions for AppCompatActivity.
 */

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import com.rapidzz.mymusicmap.other.factory.ViewModelFactory
import com.rapidzz.mymusicmap.view.activities.GlobalNavigationActivity
import com.rapidzz.mymusicmap.view.activities.LandingActivity
import com.rapidzz.yourmusicmap.view.activities.MainActivity


/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int,clearStack: Boolean,addToBackstack: Boolean) {
    if(clearStack)
        supportFragmentManager.popBackStack(0,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    supportFragmentManager.transact {
        replace(frameId, fragment)
        if(addToBackstack)
            addToBackStack(null)
    }
}

fun FragmentActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int,clearStack: Boolean,addToBackstack: Boolean) {
    if(clearStack)
        supportFragmentManager.popBackStack(0,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    supportFragmentManager.transact {
        replace(frameId, fragment)
        if(addToBackstack)
            addToBackStack(null)
    }
}

fun Activity.gotoGlobalNavigationActivity() {
    val nextIntent  = Intent(this, MainActivity::class.java)
    nextIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(nextIntent)
    finish()
}

fun Activity.gotoLandingActivity() {
    val intent  = Intent(this, LandingActivity::class.java)
    startActivity(intent)
    finish()
}

fun FragmentActivity.gotoLandingActivity() {
    val intent  = Intent(this,LandingActivity::class.java)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.gotoLandingActivity() {
    val intent  = Intent(this,LandingActivity::class.java)
    startActivity(intent)
    finish()
}


/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity?.application!!)).get(viewModelClass)


/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}
