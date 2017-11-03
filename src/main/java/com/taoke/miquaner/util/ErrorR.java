package com.taoke.miquaner.util;

public class ErrorR {

    public static final String CAN_NOT_SAVE_OBJECT_MSG = "无法保存数据到数据库";

    public static final String ST_NOT_MATCH = "ST_NOT_MATCH";
    public static final String NO_SUPER_ROLE = "NO_SUPER_ROLE";
    public static final String CAN_NOT_SAVE_OBJECT = "CAN_NOT_SAVE_OBJECT";
    public static final String ALREADY_HAS_SUPER_USER = "ALREADY_HAS_SUPER_USER";

    private String key;
    private String msg;

    public ErrorR(String key, String msg) {
        this.key = key;
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
