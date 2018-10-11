package com.liul.trc_study_task.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.liul.trc_study_task.R;

public class CircleView extends View {

    private Paint mPaint;
    private int mRadius;
    private int color;
    private int mMeasureWidth;
    private int mMeasureHeight;

    public CircleView(Context context) {
        super(context);
        init();
        Log.d("CircleView",""+1);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mRadius = typedArray.getInteger(R.styleable.CircleView_radius, 0);
        color=typedArray.getColor(R.styleable.CircleView_color,Color.RED);
        typedArray.recycle();
        init();
        Log.d("CircleView",""+mRadius);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mRadius = typedArray.getInteger(R.styleable.CircleView_radius, 0);
        typedArray.recycle();
        Log.d("CircleView",""+3);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode==MeasureSpec.EXACTLY){
            if(widthSize>=mRadius*2)
                mMeasureWidth=widthSize;
            else
                mMeasureWidth=mRadius*2;
        }else if(widthMode==MeasureSpec.AT_MOST){
            if(200>=mRadius*2)
                mMeasureWidth=200;
            else
                mMeasureWidth=mRadius*2;
        }else if(widthMode==MeasureSpec.UNSPECIFIED){

        }
        if(heightMode==MeasureSpec.EXACTLY){
            if(widthSize>=mRadius*2)
                mMeasureHeight=widthSize;
            else
                mMeasureHeight=mRadius*2;
        }else if(heightMode==MeasureSpec.AT_MOST){
            if(200>=mRadius*2)
                mMeasureHeight=200;
            else
                mMeasureHeight=mRadius*2;
        }else if(heightMode==MeasureSpec.UNSPECIFIED){

        }

        setMeasuredDimension(mMeasureWidth,mMeasureHeight);
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int paddingLeft=getPaddingLeft();
        int paddingTop=getPaddingTop();
        int paddingRight=getPaddingRight();
        int paddingBottom=getPaddingBottom();

        int width=getWidth()-paddingLeft-paddingRight;
        int height=getHeight()-paddingTop-paddingBottom;
//        mRadius=Math.min(width,height)/2;

        canvas.drawCircle(100,100,mRadius,mPaint);
    }
}
