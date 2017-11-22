package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.util.IpUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CallbackCtrl {

    private static final Logger logger = LogManager.getLogger(CallbackCtrl.class);

    @RequestMapping("/callback/vip?code={code}")
    public ResponseEntity<String> vipCallback(@PathVariable(name = "code") String code, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://auth.vip.com/oauth2/token?client_id=%s&client_secret=%s&grant_type=authorization_code&redirect_uri=%s&request_client_ip=%s&code=%s",
                "110fd112", "077484E8C89F8740CD0176D7172EA8F0", "http://server.tkmqr.com:8080/api/vip", IpUtil.getIpAddr(request), code);

        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(""), String.class);

        logger.debug(exchange.getStatusCode().toString());

        String body = exchange.getBody();
        logger.debug(body);
        return ResponseEntity.ok().body(body);
    }

    @RequestMapping("/callback/jd")
    public ResponseEntity<String> jdCallback(HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String appKey = "F15DA8EC100B0182D24742EE9D13C586";
        String appSecret = "a75813e767b94274aecba60828822d76";
        String redirectUrl = "http://server.tkmqr.com:8080/api/callback/jd";
        String url ="https://oauth.jd.com/oauth/token?grant_type=authorization_code&client_id="+appKey
                +"&client_secret="+ appSecret
                +"&scope=read&redirect_uri="+ redirectUrl
                +"&code="+ request.getParameter("code");

        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(""), String.class);

        logger.debug(exchange.getStatusCode().toString());

        String body = exchange.getBody();
        logger.debug(body);
        return ResponseEntity.ok().body(body);
    }

}
