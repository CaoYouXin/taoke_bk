package com.taoke.miquaner.util;

public class HomeType {

    /**
     * open in app, inject js to perform open in sys browser by click on a[target="sys"]
     */
    public static final Integer IN_APP_WEB_VIEW = 1;

    /**
     * open in sys browser
     */
    public static final Integer IN_SYS_WEB_VIEW = 1 << 1;

    /**
     * open a waterfall list
     */
    public static final Integer WATERFALL_LIST = 1 << 2;

    /**
     * reserved
     */
    public static final Integer RESERVED = 1 << 3;

}
