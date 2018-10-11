package com.liul.trc_study_task_5;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * MediaExtractor的作用是把音频和视频的数据进行分离
 * 主要API介绍：
 *      1、setDataResouce(String path)  既可以设置本地文件又可以设置网络文件
 *      2、getTrackCount()  得到源文件通道数
 *      3、getTrackFormat(int index)  获取指定的通道格式
 *      4、getSimpleTime()  返回当前的时间戳
 *      5、readSampleData(ByteBuffer byteBuf,int offset)  把指定通道中的数据按偏移量读取到ByteBuffer中
 *      6、advance()  读取下一帧数据
 *      7、release()  读取结束后释放资源
 */
public class MediaExtractorActivity extends AppCompatActivity {

    private String needType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_extractor);

        //使用示例
        MediaExtractor mediaExtractor=new MediaExtractor();
        try {
            //设置数据源
            mediaExtractor.setDataSource("");
            //获取源文件通道数
            int trackCount = mediaExtractor.getTrackCount();
            for(int i=0;i<trackCount;i++){
                //获取指定的通道格式
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                //从通道格式中提取mime类型
                String mime = trackFormat.getString(MediaFormat.KEY_MIME);
                //根据mime类型进行判断
                if(mime.equals(needType)){
                    mediaExtractor.selectTrack(i);
                }
            }

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while(mediaExtractor.readSampleData(byteBuffer,0)>=0){
                int index = mediaExtractor.getSampleTrackIndex();
                long sampleTime = mediaExtractor.getSampleTime();
                /**
                 *
                 *
                 */
                //读取下一帧数据
                mediaExtractor.advance();
            }

            mediaExtractor.release();
            mediaExtractor=null;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
