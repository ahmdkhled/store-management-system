package com.ahmdkhled.storemanagmentsystem.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;

public class Spinner extends android.support.v7.widget.AppCompatSpinner{
    AdapterView.OnItemSelectedListener listener;
    public Spinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setSelection(int position)
    {
        super.setSelection(position);

        if (position == getSelectedItemPosition())
        {
            listener.onItemSelected(null, null, position, 0);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener)
    {
        this.listener = listener;
    }
}
