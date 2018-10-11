package com.liul.trc_study_opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLSurfaceView extends GLSurfaceView {
    public MyGLSurfaceView(Context context) {
        super(context);

        //create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        MyRenderer myRenderer=new MyRenderer();

        //set the renderer for drawing on the GLSurfaceView
        setRenderer(myRenderer);

    }

}
