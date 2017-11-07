package com.taoke.miquaner.util;

import com.taoke.miquaner.MiquanerApplication;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpUtils {

    public static void returnJSON(HttpServletResponse response, Object ret) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Request-Headers", "auth,content-type");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String content = MiquanerApplication.DEFAULT_OBJECT_MAPPER.writeValueAsString(ret);
        response.getWriter().write(content);
    }

}
