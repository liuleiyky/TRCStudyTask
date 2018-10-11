package com.liul.trc_study_opengl;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OpenGL20Activity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private MyGLSurfaceView myGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //下面代码展示GLSurfaceView作为主视图的实现
//        glSurfaceView = new GLSurfaceView(this);

        myGLSurfaceView = new MyGLSurfaceView(this);
        setContentView(myGLSurfaceView);
    }
}
