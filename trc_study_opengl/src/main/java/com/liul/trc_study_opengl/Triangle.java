package com.liul.trc_study_opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 定义三角形
 */
public class Triangle {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private FloatBuffer floatBuffer;
    static final int COORDS_PER_VERTEX=3;
    static float triangleCoords[]={
            0.0f,0.622008459f,0.0f,//top
            -0.5f,-0.31004243f,0.0f,//bottom left
            0.5f,-0.31004243f,0.0f  //bottom right
    };
    float color[]={0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public Triangle(){
        //initial vertex byte buffer for shape coordinates
        ByteBuffer bb=ByteBuffer.allocateDirect(triangleCoords.length*4);
        //use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        //create a floating point buffer from the ByteBuffer
        floatBuffer=bb.asFloatBuffer();
        //add the coordinates to the FloatBuffer
        floatBuffer.put(triangleCoords);
        //set the buffer to read the first coordinate
        floatBuffer.position(0);

    }


}
