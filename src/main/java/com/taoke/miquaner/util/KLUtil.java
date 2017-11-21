package com.taoke.miquaner.util;

import com.haitao.thirdpart.sdk.APIUtil;
import com.taoke.miquaner.MiquanerApplication;

import java.util.Date;
import java.util.TreeMap;

public class KLUtil {

    public static String activity(String unionId, String activityUrl) {
        TreeMap<String, String> parameterMap = new TreeMap<>();
        parameterMap.put("unionId", unionId);
        parameterMap.put("activityUrl", activityUrl);
        parameterMap.put("signMethod", "md5");
        parameterMap.put("timestamp", MiquanerApplication.DEFAULT_DATE_FORMAT.format(new Date()));
        parameterMap.put("v", "1.0");
        String sign = APIUtil.createSign("8d7a37fc-23ff-48e6-bb0b-8fd7c9272939", parameterMap);
        StringBuffer sb = new StringBuffer("http://cps.kaola.com/cps/api/queryActivityDetail?");
        parameterMap.entrySet().forEach(entry -> {
            sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
        });
        sb.append("sign=").append(sign);
        return sb.toString();
    }

}
