package com.liul.trc_study_task_1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class RtcImageView extends View {

    private Bitmap bitmap;
    private Paint patint;

    public RtcImageView(Context context) {
        this(context,null);
        patint = new Paint();
    }

    public RtcImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public RtcImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0,patint);
    }

    public void setPath(String path){
        bitmap = BitmapFactory.decodeFile(path);
        invalidate();
    }
}
