package com.taoke.miquaner.ctrl;

import com.taoke.miquaner.util.KLUtil;
import com.taoke.miquaner.util.Result;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class KLCtrl {

    private static final Logger logger = LogManager.getLogger(KLCtrl.class);

    @RequestMapping("/kl/test")
    public Object test() {
        String url = KLUtil.activity("999684138096", "https://m.kaola.com/activity/h5/25702.shtml");
        logger.debug(url);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(""), String.class);
        String body = exchange.getBody();

        logger.debug(exchange.getStatusCode().toString());
        logger.debug(body);

        return Result.success(body);
    }

}
