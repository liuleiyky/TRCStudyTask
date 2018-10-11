package com.liul.trc_study_task.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TcpServerService extends Service {

    private Socket client;
    private boolean isServerDestroyed=false;
    private String[] mDefinedMessages=new String[]{
            "你好啊，哈哈",
            "请问你叫什么名字",
            "今天武汉天气不错，shy",
            "你知道吗，我可是可以和多个人同时聊天的",
            "给你讲个笑话吧：据说爱笑的人运气不会太差，不知道真假",
    };

    public TcpServerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {

            @Override
            public void run() {
                ServerSocket serverSocket;
                try {
                    serverSocket= new ServerSocket(8688);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                while(!isServerDestroyed){
                    try {
                        client = serverSocket.accept();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    responseClient(client);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void responseClient(Socket client) throws IOException {
        BufferedReader in=new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
        while(!isServerDestroyed){
            String readLine = in.readLine();
            Log.d("SocketTest","服务端收到："+readLine);
            if(readLine==null){
                //客户端断开了连接
                break;
            }
            int index = new Random().nextInt(mDefinedMessages.length);
            String msg=mDefinedMessages[index];
            SystemClock.sleep(1000);
            out.println(msg);
            out.flush();
        }
        //关闭流
        in.close();
        out.close();
        client.close();
    }

    @Override
    public void onDestroy() {
        isServerDestroyed=true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
