package com.rapidzz.yourmusicmap.other.util;

import android.content.Context;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

public class LocationAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    private boolean mIsSearchEnabled = true;

    public void setSearchEnabled(boolean isEnabled) {
        mIsSearchEnabled = isEnabled;
    }

    public LocationAutoCompleteTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public LocationAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public LocationAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mIsSearchEnabled) {
            super.performFiltering(text, keyCode);
        }
    }
}
// this is how to disable AutoCompleteTextView filter
//    @Override
//    protected void performFiltering(final CharSequence text, final int keyCode) {
//        String filterText = "";
//        super.performFiltering(filterText, keyCode);
//    }
//}
