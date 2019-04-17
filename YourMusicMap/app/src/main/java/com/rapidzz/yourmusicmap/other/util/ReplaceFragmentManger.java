package com.rapidzz.yourmusicmap.other.util;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.rapidzz.yourmusicmap.R;


public class ReplaceFragmentManger {
    public void replaceFragment(Fragment f, String TAG, Bundle bundle, Context context){
        f.setArguments(bundle);
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_container,f,TAG);
        ft.addToBackStack(TAG);
        ft.commit();
    }
}
