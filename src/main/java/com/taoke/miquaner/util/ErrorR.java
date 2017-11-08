package com.taoke.miquaner.util;

public class ErrorR {

    public static final String CAN_NOT_SAVE_OBJECT_MSG = "无法保存数据到数据库";
    public static final String CAN_NOT_SAVE_OBJECT = "CAN_NOT_SAVE_OBJECT";

    static final String SQL_ERROR = "SQL_ERROR";

    public static final String ST_NOT_MATCH = "ST_NOT_MATCH";
    public static final String NO_SUPER_ROLE = "NO_SUPER_ROLE";
    public static final String ALREADY_HAS_SUPER_USER = "ALREADY_HAS_SUPER_USER";
    public static final String SUBMIT_NEED_ROLE = "SUBMIT_NEED_ROLE";
    public static final String NO_ID_FOUND = "NO_ID_FOUND";
    public static final String SUBMIT_NEED_NAME = "SUBMIT_NEED_NAME";
    public static final String ADMIN_NOT_FOUND = "ADMIN_NOT_FOUND";
    public static final String ADMIN_WRONG_PWD = "ADMIN_WRONG_PWD";
    public static final String ADMIN_NOT_PERMITTED = "ADMIN_NOT_PERMITTED";
    public static final String FAIL_ON_EXTRACT_OBJECT_CONFIG = "FAIL_ON_EXTRACT_OBJECT_CONFIG";
    public static final String FAIL_ON_ALI_API = "FAIL_ON_ALI_API";
    public static final String NO_USER_FOUND = "NO_USER_FOUND";
    public static final String USER_WRONG_PWD = "USER_WRONG_PWD";
    public static final String ALREADY_REGISTERED_USER = "ALREADY_REGISTERED_USER";
    public static final String NO_TITLE_FOUND = "NO_TITLE_FOUND";
    public static final String NO_COL_HANDLER_FOUND = "NO_COL_HANDLER_FOUND";

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
