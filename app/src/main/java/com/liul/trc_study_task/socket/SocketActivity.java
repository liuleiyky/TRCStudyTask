package com.liul.trc_study_task.socket;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liul.trc_study_task.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SocketActivity extends AppCompatActivity {

    private Button btnSend;
    private PrintWriter pw;
    private Socket socket;
    private ListView listView;
    private TextView tvConnect;
    private EditText etMsg;
    private boolean isFinishing=false;
    private boolean connected=false;
    private String msg;
    private final static int MESSAGE_CONNECTING=0x1;
    private final static int MESSAGE_CONNECTED=0x2;
    private final static int MESSAGE_RECEIVER=0x3;

    private List<MsgInfo> msgInfoList;
    private MessageAdapter adapter;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_CONNECTING:
                    tvConnect.setText("连接中...");
                    break;
                case MESSAGE_CONNECTED:
                    tvConnect.setText("连接成功");
                    break;
                case MESSAGE_RECEIVER:
                    MsgInfo msgInfo= (MsgInfo) msg.obj;
                    msgInfoList.add(msgInfo);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        tvConnect = (TextView)findViewById(R.id.tvConnect);
        listView = (ListView)findViewById(R.id.listView);
        etMsg = (EditText)findViewById(R.id.etMsg);
        btnSend = (Button)findViewById(R.id.btnSend);

        Intent intent=new Intent(this,TcpServerService.class);
        startService(intent);

        //建立连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }).start();

        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendMsgToServer();
            }
        });

        msgInfoList = new ArrayList<MsgInfo>();
        adapter = new MessageAdapter();
        listView.setAdapter(adapter);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void connectTCPServer() {
        while(socket==null){
            try {
                Message message = Message.obtain();
                message.what=MESSAGE_CONNECTING;
                handler.sendMessage(message);

                socket=new Socket("localhost",8688);
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                connected=true;

                Message message1 = Message.obtain();
                message1.what=MESSAGE_CONNECTED;
                handler.sendMessage(message1);

            } catch (IOException e) {
                SystemClock.sleep(1000);
                e.printStackTrace();
                connected=false;
                Log.d("SocketTest","客户端收到："+e.toString());
            }
        }

        //接收服务端的消息
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(!isFinishing){
                msg = bufferedReader.readLine();
                MsgInfo msgInfo=new MsgInfo(msg,"2");

                Message message = Message.obtain();
                message.what=MESSAGE_RECEIVER;
                message.obj=msgInfo;
                handler.sendMessage(message);
            }
            if(pw!=null)
                pw.close();
            if(bufferedReader!=null)
                bufferedReader.close();
            if(socket!=null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsgToServer(){
        if(connected&&pw!=null){
            String sendMsg = etMsg.getText().toString();
            pw.println(sendMsg);
            etMsg.setText("");
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            msgInfoList.add(new MsgInfo(sendMsg,"1"));
            adapter.notifyDataSetChanged();
        }
    }

    private class MessageAdapter extends BaseAdapter{
        private LayoutInflater inflater=LayoutInflater.from(SocketActivity.this);
        @Override
        public int getCount() {
            return msgInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MsgInfo msgInfo = msgInfoList.get(position);
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                if(msgInfo.getType().equals("1")){
                    convertView=inflater.inflate(R.layout.list_itme_right,null);
                    holder.tvMsg=(TextView)convertView.findViewById(R.id.tvMsg);
                    holder.tvMsg.setBackgroundColor(Color.WHITE);
                }else{
                    convertView=inflater.inflate(R.layout.list_itme_left,null);
                    holder.tvMsg=(TextView)convertView.findViewById(R.id.tvMsg);
                    holder.tvMsg.setBackgroundColor(Color.GREEN);
                }
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
                if(msgInfo.getType().equals("1")){
                    holder.tvMsg.setBackgroundColor(Color.WHITE);
                }else{
                    holder.tvMsg.setBackgroundColor(Color.GREEN);
                }
            }
            holder.tvMsg.setText(msgInfo.getMsg());

            return convertView;
        }
    }

    class ViewHolder{
        private TextView tvMsg;
    }

    @Override
    protected void onDestroy() {
        isFinishing=true;
        super.onDestroy();
    }
}
