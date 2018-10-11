package com.liul.trc_study_task.handler;

import android.os.Looper;

public class CustomThread extends Thread {
    public CustomLocal<Looper> mCustomLocal;
    public CustomLocal.CustomLocalMap mCustomLocalMap;
}
