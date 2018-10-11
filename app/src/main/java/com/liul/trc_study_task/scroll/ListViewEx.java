package com.liul.trc_study_task.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

public class ListViewEx extends ListView {
    private int lastX;
    private int lastY;
    private HorizontalScrollViewEx parent;

    public ListViewEx(Context context) {
        super(context);
    }

    public ListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setParentView(HorizontalScrollViewEx view){
        parent=view;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x=(int) event.getX();
        int y=(int) event.getY();
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                parent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int offSetX=x-lastX;
                int offSetY=y-lastY;
                if(Math.abs(offSetX)>Math.abs(offSetY)){
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        lastX=x;
        lastY=y;
        return super.dispatchTouchEvent(event);
    }

}
