package com.taoke.miquaner.serv.impl;

import com.mysql.jdbc.StringUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.ESearchKeyWord;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.SearchKeyWordRepo;
import com.taoke.miquaner.serv.ITbkServ;
import com.taoke.miquaner.util.DivideByTenthUtil;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.AliMaMaSubmit;
import com.taoke.miquaner.view.ShareSubmit;
import com.taoke.miquaner.view.ShareView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TbkServImpl implements ITbkServ {

    private static final Logger logger = LogManager.getLogger(TbkServImpl.class);
    private static final String FAIL_ON_EXTRACT_OBJECT_CONFIG = "在展开配置类时发生错误，可能是访问限定符有误";
    private static final String FAIL_ON_ALI_API = "调用阿里API时出错，可能需要提升调用次数";

    private String serverUrl;
    private String appKey;
    private String secret;

    private ConfigRepo configRepo;
    private SearchKeyWordRepo searchKeyWordRepo;

    @Autowired
    public TbkServImpl(ConfigRepo configRepo, SearchKeyWordRepo searchKeyWordRepo) {
        this.configRepo = configRepo;
        this.searchKeyWordRepo = searchKeyWordRepo;

        this.initParams();
    }

    private void initParams() {
        EConfig gate = this.configRepo.findByKeyEquals(AliMaMaSubmit.GATE);
        if (null != gate) {
            this.serverUrl = gate.getValue();
        }

        EConfig appKey = this.configRepo.findByKeyEquals(AliMaMaSubmit.APP_KEY);
        if (null != appKey) {
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
    public Object getCouponByCid(String cid, Long pageNo, EUser user, boolean isSuper) {
        DivideByTenthUtil.Tenth tenth = DivideByTenthUtil.get(this.configRepo);
        final double userRate = isSuper ? (1.0 - tenth.platform) : tenth.second;

        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkDgItemCouponGetRequest req = new TbkDgItemCouponGetRequest();
        req.setAdzoneId(Long.parseLong(user.getAliPid().substring(user.getAliPid().lastIndexOf('_') + 1)));
        req.setPlatform(2L);
        req.setPageSize(100L);
        req.setPageNo(pageNo);
        req.setCat(cid);
        TbkDgItemCouponGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error("error when invoke ali api");
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
        logger.debug(rsp.getBody());

        if (null == rsp.getResults()) {
            return Result.success(Collections.emptyList());
        }

        return Result.success(rsp.getResults().stream().peek(tbkCoupon -> {
            tbkCoupon.setCommissionRate(String.format(Locale.ENGLISH, "%.2f",
                    userRate * Double.parseDouble(tbkCoupon.getCommissionRate())));
        }).collect(Collectors.toList()));
    }

    @Override
    public Object getShareLink(ShareSubmit shareSubmit) {
        try {
            return Result.success(new ShareView(
                    this.getShortUrl(shareSubmit.getUrl()),
                    this.getTaobaoPwd(shareSubmit)
            ));
        } catch (ApiException e) {
            logger.error("error when invoke ali api");
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
    }

    @Override
    public Object getFavoriteList(Long pageNo) {
        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkUatmFavoritesGetRequest req = new TbkUatmFavoritesGetRequest();
        req.setPageNo(pageNo);
        req.setPageSize(20L);
        req.setFields("favorites_title,favorites_id,type");
        req.setType(-1L);
        TbkUatmFavoritesGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error("error when invoke ali api");
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
        logger.debug(rsp.getBody());

        if (null == rsp.getResults()) {
            return Result.success(Collections.emptyList());
        }

        return Result.success(rsp.getResults());
    }

    @Override
    public Object getFavoriteItems(Long favoriteId, Long pageNo, EUser user, boolean isSuper) {
        DivideByTenthUtil.Tenth tenth = DivideByTenthUtil.get(this.configRepo);
        final double userRate = isSuper ? (1.0 - tenth.platform) : tenth.second;

        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkUatmFavoritesItemGetRequest req = new TbkUatmFavoritesItemGetRequest();
        req.setPlatform(2L);
        req.setPageSize(100L);
        req.setAdzoneId(Long.parseLong(user.getAliPid().substring(user.getAliPid().lastIndexOf('_') + 1)));
        req.setFavoritesId(favoriteId);
        req.setPageNo(pageNo);
        req.setFields("commission_rate,coupon_click_url,coupon_end_time,coupon_info,coupon_remain_count,coupon_start_time,coupon_total_count,category,click_url,num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type");
        TbkUatmFavoritesItemGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error("error when invoke ali api");
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
        logger.debug(rsp.getBody());

        if (null == rsp.getResults()) {
            return Result.success(Collections.emptyList());
        }

        return Result.success(rsp.getResults().stream().peek(uatmTbkItem -> {
            uatmTbkItem.setTkRate(String.format(Locale.ENGLISH, "%.2f",
                    userRate * Double.parseDouble(uatmTbkItem.getTkRate())));
        }).collect(Collectors.toList()));
    }

    @Override
    public Object search(EUser user, String keyword) {
        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkDgItemCouponGetRequest req = new TbkDgItemCouponGetRequest();
        req.setAdzoneId(Long.parseLong(user.getAliPid().substring(user.getAliPid().lastIndexOf('_') + 1)));
        req.setPlatform(2L);
        req.setPageSize(100L);
        req.setPageNo(1L);
        req.setQ(keyword);
        TbkDgItemCouponGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error("error when invoke ali api");
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
        logger.debug(rsp.getBody());

        ESearchKeyWord one = this.searchKeyWordRepo.findByKeywordEquals(keyword);
        if (null == one) {
            one = new ESearchKeyWord();
            one.setKeyword(keyword);
            this.searchKeyWordRepo.save(one);
        }

        if (null == rsp.getResults()) {
            return Result.success(Collections.emptyList());
        }

        return Result.success(rsp.getResults().stream().peek(tbkCoupon -> {
            tbkCoupon.setCommissionRate(String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(tbkCoupon.getCommissionRate()) * 0.3));
        }).collect(Collectors.toList()));
    }

    @Override
    public Object hints(String keyword) {
        return Result.success(this.searchKeyWordRepo.findAllByKeywordContains(keyword)
                .stream().map(ESearchKeyWord::getKeyword).collect(Collectors.toList()));
    }

    private String getTaobaoPwd(ShareSubmit shareSubmit) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        req.setUserId("3434155161");
        req.setText(shareSubmit.getTitle());
        req.setUrl(shareSubmit.getUrl());
        TbkTpwdCreateResponse rsp = client.execute(req);
        logger.debug(rsp.getBody());
        return rsp.getData().getModel();
    }

    private String getShortUrl(String url) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkSpreadGetRequest req = new TbkSpreadGetRequest();
        List<TbkSpreadGetRequest.TbkSpreadRequest> list2 = new ArrayList<>();
        TbkSpreadGetRequest.TbkSpreadRequest obj3 = new TbkSpreadGetRequest.TbkSpreadRequest();
        list2.add(obj3);
        obj3.setUrl(url);
        req.setRequests(list2);
        TbkSpreadGetResponse rsp = client.execute(req);
        logger.debug(rsp.getBody());
        return rsp.getResults().get(0).getContent();
    }


}
