package com.taoke.miquaner.view;

public class CustomerServiceView {

    private String weChat;
    private String mqq;

    public CustomerServiceView(String weChat, String mqq) {
        this.weChat = weChat;
        this.mqq = mqq;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getMqq() {
        return mqq;
    }

    public void setMqq(String mqq) {
        this.mqq = mqq;
    }
}
