package com.liul.trc_study_task.scroll;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode=MeasureSpec.getMode(widthMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            Log.d("MeasureSpec","MeasureSpec.EXACTLY");
        }else if(mode==MeasureSpec.AT_MOST){
            Log.d("MeasureSpec","MeasureSpec.AT_MOST");
        }else if(mode==MeasureSpec.UNSPECIFIED){
            Log.d("MeasureSpec","MeasureSpec.UNSPECIFIED");
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
