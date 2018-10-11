package com.liul.trc_study_task_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 录制声音自定义控件
 */
public class AudioRecordView extends View {
    private int radius=200;
    private Paint paint;

    public AudioRecordView(Context context) {
        super(context);
    }

    public AudioRecordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.BLACK);

    }

    public AudioRecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        canvas.drawCircle(radius,radius,radius,paint);
    }
}
