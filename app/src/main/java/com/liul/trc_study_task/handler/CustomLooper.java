package com.liul.trc_study_task.handler;

public class CustomLooper {
    private static CustomLocal<CustomLooper> mCustomLocal=new CustomLocal<CustomLooper>();

    public static void prepare(){
        if(mCustomLocal.get()!=null){
            throw new RuntimeException("");
        }
        mCustomLocal.set(new CustomLooper());
    }

}
