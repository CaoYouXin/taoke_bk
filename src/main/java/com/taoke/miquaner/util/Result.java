package com.taoke.miquaner.util;

public class Result {

    public static final int SUCCESS = 2000;
    public static final int GENERAL_FAIL = 5000;

    public static Result success(Object body) {
        return new Result(SUCCESS, body);
    }

    public static Result fail(Object body) {
        return new Result(GENERAL_FAIL, body);
    }

    private int code;
    private Object body;

    public Result(int code, Object body) {
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
