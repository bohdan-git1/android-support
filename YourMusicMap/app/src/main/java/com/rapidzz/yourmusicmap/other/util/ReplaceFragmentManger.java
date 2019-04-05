package com.rapidzz.yourmusicmap.other.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.rapidzz.yourmusicmap.R;


public class ReplaceFragmentManger {
    public void replaceFragment(Fragment f, String TAG, Bundle bundle, Context context){
        f.setArguments(bundle);
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,f,TAG);
        ft.addToBackStack(TAG);
        ft.commit();
    }
}
