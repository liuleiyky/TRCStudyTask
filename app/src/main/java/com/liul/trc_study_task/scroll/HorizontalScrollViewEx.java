package com.liul.trc_study_task.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 实现一个类似ViewPager中嵌套ListView的效果
 */
public class HorizontalScrollViewEx extends ViewGroup{
    private int lastX;
    private int lastY;
    private int lastInterceptX;
    private int lastInterceptY;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int childWidth;
    private int mChildrenSize=3;
    private int mChildIndex;
    private int mMeasureWidth;
    private int mMeasureHeight;

    public HorizontalScrollViewEx(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();

        //进行子类测量
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        if(childCount==0){
            setMeasuredDimension(0,0);
        }else if(widthMode==MeasureSpec.EXACTLY&&heightMode==MeasureSpec.EXACTLY){
            View childView = getChildAt(0);
            mMeasureWidth=childView.getMeasuredWidth()*childCount;
            mMeasureHeight=childView.getMeasuredHeight();
            setMeasuredDimension(mMeasureWidth,mMeasureHeight);
        }else if(widthMode==MeasureSpec.AT_MOST){
            View childView = getChildAt(0);
            mMeasureWidth=childView.getMeasuredWidth()*childCount;
            mMeasureHeight=heightSize;
            setMeasuredDimension(mMeasureWidth,mMeasureHeight);
        }else if(heightMode==MeasureSpec.AT_MOST){
            View childView = getChildAt(0);
            mMeasureWidth=widthSize;
            mMeasureHeight=childView.getMeasuredHeight();
            setMeasuredDimension(mMeasureWidth,mMeasureHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft=0;
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            if(childView.getVisibility()!=View.GONE){
                childWidth=childView.getMeasuredWidth();
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft+=childWidth;
            }
        }
    }

        @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted=false;
        int x=(int) event.getX();
        int y=(int) event.getY();
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                intercepted=false;

                break;
            case MotionEvent.ACTION_MOVE:
                int offSetX=x-lastInterceptX;
                int offSetY=y-lastInterceptY;
                if(Math.abs(offSetX)>Math.abs(offSetY))
                    intercepted=true;
                else
                    intercepted=false;

                break;
            case MotionEvent.ACTION_UP:
                intercepted=false;
                break;
        }
        lastX=x;
        lastY=y;
        lastInterceptX=x;
        lastInterceptY=y;
        return intercepted;
    }

    private float latestOffSet;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("HorizontalScrollViewEx",""+getScrollX());
        mVelocityTracker.addMovement(event);
        int x=(int) event.getX();
        int y=(int) event.getY();
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int offSetX=x-lastX;
                int offSetY=y-lastY;
                latestOffSet=event.getX()-lastX;
//                Log.d("HorizontalScrollViewEx","offset:"+latestOffSet);
//                Log.d("HorizontalScrollViewEx",""+lastX+","+x);
                scrollBy(-offSetX,0);
                break;
            case MotionEvent.ACTION_UP:
//                Log.d("HorizontalScrollViewEx","scrollX:"+getScrollX());
//                smoothTo(getScrollX()+200,0);
//                if(latestOffSet>0){
//                    smoothTo(getScrollX()-200,0);
//                }else if(latestOffSet<0){
//                    smoothTo(getScrollX()+200,0);
//                }
                break;
        }
        lastX=x;
        lastY=y;
        return true;
    }

    private void smoothTo(int dx,int dy){
        mScroller.startScroll(getScrollX(),0,dx,0,500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }
}
