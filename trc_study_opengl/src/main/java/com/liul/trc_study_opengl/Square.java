package com.liul.trc_study_opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {
    private FloatBuffer floatBuffer;
    private ShortBuffer drawListBuffer;

    static final int COORDS_PER_VERTEX=3;
    static float squareCoords[]={
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f  // top right
    };

    private short drawOrder[]={0, 1, 2, 0, 2, 3};

    public Square(){
        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(squareCoords.length*4);
        //修改缓冲区的字节顺序
        byteBuffer.order(ByteOrder.nativeOrder());

        floatBuffer=byteBuffer.asFloatBuffer();
        floatBuffer.put(squareCoords);
        floatBuffer.position(0);
    }
}
