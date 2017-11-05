package com.taoke.miquaner.util;

public class Result {

    private static final int SUCCESS = 2000;
    private static final int GENERAL_FAIL = 5000;

    public static final String SUCCESS_MSG = "操作成功";
    public static final ErrorR FAIL_ON_SQL = new ErrorR(ErrorR.SQL_ERROR, "操作失败");

    public static Result success(Object body) {
        return new Result(SUCCESS, body);
    }

    public static Result fail(Object body) {
        return new Result(GENERAL_FAIL, body);
    }

    private int code;
    private Object body;

    private Result(int code, Object body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
