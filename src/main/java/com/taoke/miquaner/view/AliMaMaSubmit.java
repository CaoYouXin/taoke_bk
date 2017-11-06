package com.taoke.miquaner.view;

public class AliMaMaSubmit {

    public static final String ACCOUNT = "Account";
    public static final String MAIL = "Mail";
    public static final String PWD = "Pwd";
    public static final String APP_KEY = "AppKey";
    public static final String SECRET = "Secret";
    public static final String GATE = "Gate";
    public static final String PID_K = "Pid";

    private String account;
    private String mail;
    private String pwd;
    private String appKey;
    private String secret;
    private String gate;
    private String pid;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
