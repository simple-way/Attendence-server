package com.example.mrc.attendencesystem.entity;

import java.io.Serializable;

public class UnReceivedMessage implements Serializable{
    private static final long serialVersionUID = 3L;
    int type;
    int receiverId;
    String content;
    int state;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
