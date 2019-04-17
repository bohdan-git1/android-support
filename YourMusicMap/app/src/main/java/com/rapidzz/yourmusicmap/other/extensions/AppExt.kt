package com.rapidzz.mymusicmap.other.extensions
/**
 * Various extension functions for AppCompatActivity.
 */

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.rapidzz.mymusicmap.other.factory.ViewModelFactory
import com.rapidzz.mymusicmap.view.activities.GlobalNavigationActivity
import com.rapidzz.mymusicmap.view.activities.LandingActivity
import com.rapidzz.yourmusicmap.view.activities.MainActivity


/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: androidx.fragment.app.Fragment, frameId: Int, clearStack: Boolean, addToBackstack: Boolean) {
    if(clearStack)
        supportFragmentManager.popBackStack(0, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
    supportFragmentManager.transact {
        replace(frameId, fragment)
        if(addToBackstack)
            addToBackStack(null)
    }
}

fun androidx.fragment.app.FragmentActivity.replaceFragmentInActivity(fragment: androidx.fragment.app.Fragment, frameId: Int, clearStack: Boolean, addToBackstack: Boolean) {
    if(clearStack)
        supportFragmentManager.popBackStack(0, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

fun androidx.fragment.app.FragmentActivity.gotoLandingActivity() {
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
fun AppCompatActivity.addFragmentToActivity(fragment: androidx.fragment.app.Fragment, tag: String) {
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

fun <T : ViewModel> androidx.fragment.app.FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> androidx.fragment.app.Fragment.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity?.application!!)).get(viewModelClass)


/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun androidx.fragment.app.FragmentManager.transact(action: androidx.fragment.app.FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}
