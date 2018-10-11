package com.liul.trc_study_task_4;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 编码器类
 */
public class Encoder {
    private final String MIME_TYPE="video/avc";
    private MediaCodec mediaCodec;
    private int colorFormat;
    private MediaCodec.BufferInfo bufferInfo;
    private MediaFormat mediaFormat;
    private ByteBuffer[] inputBuffers;
    private ByteBuffer[] outputBuffers;

    private int BUFFER_OK=0;
    public int BUFFER_TOO_SMALL = 1;
    private int BUFFER_TIMEOUT=2;

    /**
     * 初始化编码器
     */
    public void init() throws IOException {
        mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        colorFormat = MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar;
        bufferInfo = new MediaCodec.BufferInfo();
    }

    /**
     * 配置编码器，需要配置颜色、帧率、比特率以及视频宽高
     * @param width     视频的宽
     * @param height    视频的高
     * @param bitrate   视频比特率
     * @param framerate 视频帧率
     */
    public void configure(int width,int height,int bitrate,int framerate){
        mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE,width,height);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE,bitrate);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE,framerate);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL,-1);
        mediaCodec.configure(mediaFormat,null,null,MediaCodec.CONFIGURE_FLAG_ENCODE);
    }

    /**
     * 开启编解码器，获取输入输出缓冲区
     */
    public void start(){
        mediaCodec.start();
        inputBuffers = mediaCodec.getInputBuffers();
        outputBuffers = mediaCodec.getOutputBuffers();
    }

    /**
     * 向编码器输入数据，此处要求输入YUV420P的数据
     * @param data  YUV数据
     * @param len   数据长度
     * @param timeStamp     时间戳
     * @return
     */
    public int input(byte[] data,int len,long timeStamp){
        //获取可用缓冲区索引，如果存在可用缓冲区，此方法会返回其位置索引，否则返回-1，
        //参数为超时时间设置，单位是毫秒，如果此参数是0，则立即返回；如果参数小于0，则无限等待知道有可用的缓冲区；如果参数大于0，则等待时间
        //为出入的毫秒值
        int index = mediaCodec.dequeueInputBuffer(BUFFER_TIMEOUT);
        if(index>=0){
            //往缓冲区中传入原始数据
            ByteBuffer byteBuffer=inputBuffers[index];
            byteBuffer.clear();//清除原来内容以接受新的内容
            byteBuffer.put(data,0,len);//将需要编码的数据放入缓冲区
            mediaCodec.queueInputBuffer(index,0,len,timeStamp,0);
        }else{
            return index;
        }
        return BUFFER_OK;
    }


    /**
     * 输出编码后的数据
     */
//    public int output(byte[] out,int[] len,long[] ts){
//        int index = mediaCodec.dequeueOutputBuffer(bufferInfo, BUFFER_TIMEOUT);
//        if(index>=0){
//            ByteBuffer byteBuffer=outputBuffers[index];
//            byte[] outData=new byte[bufferInfo.size];
//            byteBuffer.get(outData,0,bufferInfo.size);
//            mediaCodec.releaseOutputBuffer(index,false);
//        }
//    }


}
