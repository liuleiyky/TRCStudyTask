package com.liul.trc_study_task.mvp;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V> {

    private WeakReference<V> weakReference;

    protected void attachView(V v){
        weakReference = new WeakReference<V>(v);
    }

    protected V getAttachView(){
        return weakReference.get();
    }

    protected void detachView(){
        if(weakReference!=null){
            weakReference.clear();
            weakReference=null;
        }
    }

}
