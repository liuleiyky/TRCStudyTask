package com.liul.trc_study_task.crash;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        dumpExceptionToSDCard(e);
    }

    private void dumpExceptionToSDCard(Throwable e) {

    }
}
