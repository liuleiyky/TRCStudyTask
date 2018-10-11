package com.liul.trc_study_task.scroll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class ScrollerView extends View {
    private Paint paint;
    private float radius=50;
    private Scroller mScroller;
    //    private float x=200;
//    private float y=200;

    public ScrollerView(Context context) {
        super(context);
    }

    public ScrollerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth( 100 );
        paint.setStyle(Paint.Style.STROKE);

        mScroller = new Scroller(context);

    }

    public ScrollerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
////        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
////
////        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
////        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
//
//        setMeasuredDimension(100,100);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d("ScrollerView","startAnimation:"+getLeft()+","+getTop());
        canvas.drawCircle(100,100,50,paint);
        Log.d("ScrollerView","onDraw");
    }
    private int lastX;
    private int lastY;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private int x;
    private int y;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        x= (int) event.getRawX();
//        y= (int) event.getRawY();
////        Log.d("ScrollerView","onTouchEvent:"+x+","+y);
//        int action = event.getAction();
//        switch (action){
//            case MotionEvent.ACTION_DOWN:
//
//                lastX=x;
//                lastY=y;
////                left=getLeft();
////                top=getTop();
////                right=getRight();
////                bottom=getBottom();
////                lastX=(int) event.getRawX();
////                lastY=(int) event.getRawY();
////                Log.d("ScrollerView","ACTION_DOWN:"+startX+","+startY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //计算移动的距离
//                AnimatorSet animatorSet=new AnimatorSet();
//                ObjectAnimator animatorX = ObjectAnimator.ofFloat(ScrollerView.this, "translationX", lastX,x);
//                ObjectAnimator animatorY = ObjectAnimator.ofFloat(ScrollerView.this, "translationY", lastY,y);
//                animatorSet.play(animatorX);
//                animatorSet.play(animatorY);
//                animatorSet.setDuration(10).start();
//                lastX=x;
//                lastY=y;
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        return true;
//    }
    public void smoothScrollTo(int destX,int destY){
        int startX = getScrollX();
        int offSetX=destX-startX;
        mScroller.startScroll(startX,0,offSetX,0,3000);
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
