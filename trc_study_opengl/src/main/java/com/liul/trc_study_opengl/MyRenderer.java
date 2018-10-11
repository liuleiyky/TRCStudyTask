package com.liul.trc_study_opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 实现了GLSurfaceView.Render类才是真正算是开始能够在应用中使用OpenGL ES。这个类控制着与它关联的GLSurfaceView的内容。在renderer里面有3个
 * 方法能够被android系统调用，以便知道在GLSurfaceView中绘制什么以及如何绘制。
 */
public class MyRenderer implements GLSurfaceView.Renderer{
    private Triangle triangle;
    private Square square;

    //在View的OpenGL环境被创建的时候调用
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //set the background frame color
//            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
        triangle = new Triangle();
        square = new Square();

    }

    //视图的几何形状发生变化（如屏幕方向改变时），则调用此方法
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    //每一次View重绘都会被调用
    @Override
    public void onDrawFrame(GL10 gl) {
        //redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }


    public static int loadShader(int type,String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
