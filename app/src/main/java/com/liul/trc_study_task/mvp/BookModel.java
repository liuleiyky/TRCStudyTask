package com.liul.trc_study_task.mvp;

import android.os.Handler;
import android.os.Looper;

public class BookModel {
    private boolean cancel=false;
    private Handler mainHandler=new Handler(Looper.getMainLooper());


    public void queryBookInfo(final OnQueryBookInfoListener onQueryBookInfoListener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*5);
                    if(!cancel){
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                onQueryBookInfoListener.onSuccess("Android开发艺术探索");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void cancelQueryBookInfo(){
        cancel=true;
    }
}
