package com.liul.trc_study_task_6;

import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 学习MediaCodec API，完成音频AAC硬编、硬解
 * 学习MediaCodec API，完成视频H.264的硬编、硬解
 * MediaCodec类可以用于使用一些基本的多媒体编解码器（音视频编解码组件），它是android基本的多媒体支持基本框架的一部分，通常
 *           和MediaExtractor、MediaSync、MediaMuxer、MediaCrypto、MediaDrm、Image、Surface and AudioTask一起使用
 * MediaCodec可以处理具体的视频流，主要有这几个方法：
 *      getInputBuffers：获取需要编码数据的输入流队列，返回的是一个ByteBuffer数组
 *      queueInputBuffer：输入流入队列
 *      dequeueInputBuffer：从输入流队列中取数据进行编码操作
 *      getOutputBuffer：获取编解码之后的数据输出流队列，返回的是一个ByteBuffer数组
 *      dequeueOutputBuffer：从输出队列取出编码操作之后的数据
 *      releaseOutputBuffer：处理完成，释放ByteBuffer数据
 *
 * android硬编码流控：
 *      1、配置时设置目标码率和码率控制模式
 *      2、动态调整目标码率
 */
public class MediaCodecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_codec);

        //设置目标码率和码率控制模式
        MediaFormat mediaFormat=new MediaFormat();
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE,100);
        mediaFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR);

        //动态调整目标码率


    }
}
