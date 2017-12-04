package com.taoke.miquaner.util;

public class Result {

    private static final int SUCCESS = 2000;
    private static final int UN_AUTH = 4010;
    private static final int UN_AUTH_ADMIN = 4011;
    private static final int GENERAL_FAIL = 5000;
    private static final int VERSION_LOW = 5010;

    public static final String SUCCESS_MSG = "操作成功";
    public static final ErrorR FAIL_ON_SQL = new ErrorR(ErrorR.SQL_ERROR, "操作失败");

    public static Result success(Object body) {
        return new Result(SUCCESS, body);
    }

    public static Result fail(Object body) {
        return new Result(GENERAL_FAIL, body);
    }

    public static Result unAuth() {
        return new Result(UN_AUTH, null);
    }

    public static Result versionLow() {
        return new Result(VERSION_LOW, null);
    }

    public static Result unAuthAdmin() {
        return new Result(UN_AUTH_ADMIN, null);
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

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", body=" + body +
                '}';
    }
}
