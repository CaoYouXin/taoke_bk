package com.taoke.miquaner.ctrl;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.cps.UnionSearchQueryCouponGoodsRequest;
import com.jd.open.api.sdk.response.cps.UnionSearchQueryCouponGoodsResponse;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JdCPSCtrl {

    private static final Logger logger = LogManager.getLogger(JdCPSCtrl.class);

    @RequestMapping(value = "/jd/test", method = RequestMethod.GET)
    public Object test() {
        JdClient client = new DefaultJdClient("https://api.jd.com/routerjson", "f0fa32a8-73e4-4567-9d9f-ee1b1699ed24", "F15DA8EC100B0182D24742EE9D13C586", "a75813e767b94274aecba60828822d76");

        UnionSearchQueryCouponGoodsRequest request = new UnionSearchQueryCouponGoodsRequest();

        request.setPageIndex(1);
        request.setPageSize(100);
        request.setCid3(123);

        UnionSearchQueryCouponGoodsResponse response = null;
        try {
            response = client.execute(request);
        } catch (JdException e) {
            logger.error("error when invoke jd api", e);
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_JD_API, "调用京东API时出错"));
        }

        return Result.success(response.getQueryCouponGoodsResult());
    }

}
