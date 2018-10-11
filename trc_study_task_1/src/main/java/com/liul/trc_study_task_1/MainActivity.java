package com.liul.trc_study_task_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

/**
 * 音视频学习Task1：绘制一张图片，使用至少 3 种不同的 API，ImageView，SurfaceView，自定义 View
 */
public class MainActivity extends AppCompatActivity {

    private String filePath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trcstudytask1_activity_draw_image);

        //method_1  ImageView
        Bitmap bitmap= BitmapFactory.decodeFile(filePath);
        ImageView imageView=new ImageView(this);
        imageView.setImageBitmap(bitmap);


        //method_2  SurfaceView
        SurfaceView surfaceView=new SurfaceView(this);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Paint paint=new Paint();
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);

                Canvas canvas = surfaceHolder.lockCanvas();//锁定当前画布
                Bitmap bitmap= BitmapFactory.decodeFile(filePath);
                canvas.drawBitmap(bitmap,0,0,paint);//执行绘制操作
                surfaceHolder.unlockCanvasAndPost(canvas);//解除锁定并显示在界面上

            }
                @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });


    }
}
