package com.taoke.miquaner.serv.impl;

import com.mysql.jdbc.StringUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgItemCouponGetRequest;
import com.taobao.api.response.TbkDgItemCouponGetResponse;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.serv.ITbkServ;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AliMaMaSubmit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class TbkServImpl implements ITbkServ {

    private static final Logger logger = LogManager.getLogger(TbkServImpl.class);
    private static final String FAIL_ON_EXTRACT_OBJECT_CONFIG = "在展开配置类时发生错误，可能是访问限定符有误";
    private static final String FAIL_ON_ALI_API = "调用阿里API时出错，可能需要提升调用次数";

    private String serverUrl;
    private String appKey;
    private String secret;

    private ConfigRepo configRepo;

    @Autowired
    public TbkServImpl(ConfigRepo configRepo) {
        this.configRepo = configRepo;

        this.initParams();
    }

    private void initParams() {
        EConfig gate = this.configRepo.findByKeyEquals(AliMaMaSubmit.GATE);
        if (null != gate) {
            this.serverUrl = gate.getValue();
        }

        EConfig appKey = this.configRepo.findByKeyEquals(AliMaMaSubmit.APP_KEY);
        if(null != appKey) {
            this.appKey = appKey.getValue();
        }

        EConfig secret = this.configRepo.findByKeyEquals(AliMaMaSubmit.SECRET);
        if (null != secret) {
            this.secret = secret.getValue();
        }
    }

    @Override
    public Object setAliMaMa(AliMaMaSubmit aliMaMaSubmit) {
        List<EConfig> toSave = new ArrayList<>();
        for (Method method : AliMaMaSubmit.class.getDeclaredMethods()) {
            String methodName = method.getName();
            EConfig config = null;

            if (methodName.startsWith("get")) {
                String key = methodName.substring(3);
                config = this.configRepo.findByKeyEquals(key);
                if (null == config) {
                    config = new EConfig();
                    config.setKey(key);
                }
            }

            if (methodName.startsWith("is")) {
                String key = methodName.substring(2);
                config = this.configRepo.findByKeyEquals(key);
                if (null == config) {
                    config = new EConfig();
                    config.setKey(key);
                }
            }

            if (null == config) {
                continue;
            }

            try {
                config.setValue((String) method.invoke(aliMaMaSubmit));
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("set ali api settings, error with extract configs");
                return Result.fail(new ErrorR(ErrorR.FAIL_ON_EXTRACT_OBJECT_CONFIG, FAIL_ON_EXTRACT_OBJECT_CONFIG));
            }

            if (StringUtils.isNullOrEmpty(config.getValue())) {
                continue;
            }

            toSave.add(config);
        }

        this.configRepo.save(toSave);
        this.initParams();
        return Result.success("All Set");
    }

    @Override
    public Object getAliMaMa() {
        AliMaMaSubmit ret = new AliMaMaSubmit();
        for (Method method : AliMaMaSubmit.class.getDeclaredMethods()) {
            String methodName = method.getName();

            if (!methodName.startsWith("set")) {
                continue;
            }

            String key = methodName.substring(3);
            EConfig config = this.configRepo.findByKeyEquals(key);
            if (null == config) {
                continue;
            }

            try {
                method.invoke(ret, config.getValue());
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("set ali api settings, error with extract configs");
                return Result.fail(new ErrorR(ErrorR.FAIL_ON_EXTRACT_OBJECT_CONFIG, FAIL_ON_EXTRACT_OBJECT_CONFIG));
            }
        }
        return Result.success(ret);
    }

    @Override
    public Object getCouponByCid(String cid, Long pageNo, EUser user) {
        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkDgItemCouponGetRequest req = new TbkDgItemCouponGetRequest();
        req.setAdzoneId(Long.parseLong(user.getAliPid().substring(user.getAliPid().lastIndexOf('_'))));
        req.setPlatform(2L);
        req.setPageSize(10000L);
        req.setPageNo(pageNo);
        req.setCat(cid);
        TbkDgItemCouponGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error("error when invoke ali api");
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
        return rsp.getBody();
    }


}
