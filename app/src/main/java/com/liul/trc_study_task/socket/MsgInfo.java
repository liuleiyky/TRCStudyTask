package com.liul.trc_study_task.socket;

import java.io.Serializable;

public class MsgInfo implements Serializable{
    private String msg="";
    private String type="";//1-发送消息 2-接受到的消息

    public MsgInfo(String msg, String type) {
        this.msg = msg;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
