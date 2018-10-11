package com.liul.trc_study_task_5;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * MediaMuxer的作用是生成音频或视频文件；还可以把音频与视频混合成一个音视频文件
 * 相关API介绍：
 *      1、MediaMuxer(String path,int format)  path:输出文件的名称  format：输出文件的格式，当前只支持mp4
 *      2、addTrack(MediaFormat format)  添加通道，我们更更多的是使用MediaCodec.getOutputFormat()或Extractor.getTractorFormat(int index)
 *                                       来获取MediaFormat，也可以自己创建
 *      3、start()
 *      4、writeSampleData(trackIndex,byteBuf,bufferInfo)  把ByteBuffer中的数据写到文件中
 *      5、stop停止合成文件
 *      6、释放资源
 */
public class MediaMuxerActivity extends AppCompatActivity {

    private boolean finished=false;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_muxer);

        //使用示例
        try {
            MediaMuxer mediaMuxer=new MediaMuxer("temp.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            MediaFormat audioFormat=new MediaFormat();
            MediaFormat videoFormat=new MediaFormat();

            mediaMuxer.addTrack(audioFormat);
            mediaMuxer.addTrack(videoFormat);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            MediaCodec.BufferInfo bufferInfo=new MediaCodec.BufferInfo();


            mediaMuxer.start();
            while(!finished){


            }
            mediaMuxer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
