package com.taoke.miquaner.view;

public class ShareView {

    private String shortUrl;
    private String tPwd;

    public ShareView(String shortUrl, String tPwd) {
        this.shortUrl = shortUrl;
        this.tPwd = tPwd;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String gettPwd() {
        return tPwd;
    }

    public void settPwd(String tPwd) {
        this.tPwd = tPwd;
    }

    @Override
    public String toString() {
        return shortUrl + '\n' + "复制这条消息，" + tPwd + "，打开【手机淘宝】即可查看";
    }
}
