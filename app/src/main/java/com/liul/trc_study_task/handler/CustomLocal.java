package com.liul.trc_study_task.handler;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class CustomLocal<T> {
//    private Map<Thread,T> map=new HashMap<Thread, T>();
    public void set(T t){
        CustomThread currentThread= (CustomThread) CustomThread.currentThread();
        CustomLocalMap map = getMap(currentThread);
        if(map!=null){

        }else{
            map=createMap(currentThread,t);
        }
    }

    private CustomLocalMap getMap(CustomThread t){
        return t.mCustomLocalMap;
    }

    private CustomLocalMap createMap(CustomThread t,T value){
        return new CustomLocalMap(t,value);
    }

    public T get(){
        return null;
    }

    class CustomLocalMap{
        private Thread thread;
        public CustomLocalMap(Thread thread,T value){
            this.thread=thread;
        }

        public void set(){

        }

        public void get(){

        }
    }


}
