package com.taoke.miquaner.serv.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.jdbc.StringUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.UatmTbkItem;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.ESearchKeyWord;
import com.taoke.miquaner.data.ETbkItem;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.SearchKeyWordRepo;
import com.taoke.miquaner.repo.TbkItemRepo;
import com.taoke.miquaner.serv.IShareServ;
import com.taoke.miquaner.serv.ITbkServ;
import com.taoke.miquaner.util.DivideByTenthUtil;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TbkServImpl implements ITbkServ {

    private static final Logger logger = LogManager.getLogger(TbkServImpl.class);
    private static final String FAIL_ON_EXTRACT_OBJECT_CONFIG = "在展开配置类时发生错误，可能是访问限定符有误";
    private static final String FAIL_ON_ALI_API = "调用阿里API时出错，可能需要提升调用次数";
    private static final String FAIL_ON_OBJECT_MAPPER_API = "对象序列化时出错";
    private static final String FAIL_ON_SERV_RET_NON_RESULT = "服务器内部错误";

    private String serverUrl;
    private String appKey;
    private String secret;

    private final ConfigRepo configRepo;
    private final SearchKeyWordRepo searchKeyWordRepo;
    private final TbkItemRepo tbkItemRepo;
    private final IShareServ shareServ;

    @Autowired
    public TbkServImpl(IShareServ shareServ, ConfigRepo configRepo, SearchKeyWordRepo searchKeyWordRepo, TbkItemRepo tbkItemRepo) {
        this.shareServ = shareServ;
        this.configRepo = configRepo;
        this.searchKeyWordRepo = searchKeyWordRepo;
        this.tbkItemRepo = tbkItemRepo;

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

        this.initParams();
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
            logger.error("error when invoke ali api" + e.getMessage());
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
                    this.getTaobaoPwd(shareSubmit.getTitle(), shareSubmit.getUrl())
            ));
        } catch (ApiException e) {
            logger.error("error when invoke ali api" + e.getMessage());
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
    }

    @Override
    public Object getShareLink2(ShareSubmit2 shareSubmit2) {
        ShareView2 view = new ShareView2();
        try {
            view.settPwd(this.getTaobaoPwd(shareSubmit2.getTitle(), shareSubmit2.getUrl()));
        } catch (ApiException e) {
            logger.error("error when invoke ali api" + e.getMessage());
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
        view.setImages(shareSubmit2.getImages());
        String json = null;
        try {
            json = MiquanerApplication.DEFAULT_OBJECT_MAPPER.writeValueAsString(view);
        } catch (JsonProcessingException e) {
            logger.error("error trans ShareView2 to json" + e.getMessage());
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_OBJECT_MAPPER_API, FAIL_ON_OBJECT_MAPPER_API));
        }

        Object shareSave = this.shareServ.shareSave(json);
        if (!(shareSave instanceof Result)) {
            logger.error("error when unexpected serv interface");
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_SERV_RET_NON_RESULT, FAIL_ON_SERV_RET_NON_RESULT));
        }

        return Result.success(new ShareView("share/" + ((Result) shareSave).getBody().toString(), view.gettPwd()));
    }

    @Override
    public Object getFavoriteList(Long pageNo) {
        this.initParams();
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
            logger.error("error when invoke ali api" + e.getMessage());
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

        final long adZoneId = Long.parseLong(user.getAliPid().substring(user.getAliPid().lastIndexOf('_') + 1));

        List<UatmTbkItem> uatmTbkItems = this.getUatmTbkItems(userRate, favoriteId, 1L, adZoneId);
        if (uatmTbkItems.size() == 100) {
            uatmTbkItems.addAll(this.getUatmTbkItems(userRate, favoriteId, 2L, adZoneId));
        }

        return Result.success(uatmTbkItems);
    }

    private List<UatmTbkItem> getUatmTbkItems(final double userRate, final Long favoriteId, final Long pageNo, final long adZoneId) {
        this.initParams();
        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);

        TbkUatmFavoritesItemGetRequest req = new TbkUatmFavoritesItemGetRequest();
        req.setPlatform(2L);
        req.setPageSize(100L);
        req.setAdzoneId(adZoneId);
        req.setFavoritesId(favoriteId);
        req.setPageNo(pageNo);
        req.setFields("commission_rate,coupon_click_url,coupon_end_time,coupon_info,coupon_remain_count,coupon_start_time,coupon_total_count,category,click_url,num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type");
        TbkUatmFavoritesItemGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error("error when invoke ali api" + e.getMessage());
            return Collections.emptyList();
        }
        logger.debug(rsp.getBody());

        if (null == rsp.getResults()) {
            return Collections.emptyList();
        }

        if ("15".equals(rsp.getErrorCode()) && "isv.invalid-parameter:favorites_id".equals(rsp.getSubCode())) {
            return Collections.emptyList();
        }

        return rsp.getResults().stream().peek(uatmTbkItem -> {
            uatmTbkItem.setTkRate(String.format(Locale.ENGLISH, "%.2f",
                    userRate * Double.parseDouble(uatmTbkItem.getTkRate())));
        }).collect(Collectors.toList());
    }

    @Override
    public Object search(EUser user, String keyword, Boolean isSuper) {
        DivideByTenthUtil.Tenth tenth = DivideByTenthUtil.get(this.configRepo);
        final double userRate = isSuper ? (1.0 - tenth.platform) : tenth.second;

        this.initParams();
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
            logger.error("error when invoke ali api" + e.getMessage());
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
        logger.debug(rsp.getBody());

        this.refreshKeyWord(keyword);

        if (null == rsp.getResults()) {
            return Result.success(Collections.emptyList());
        }

        return Result.success(rsp.getResults().stream().peek(tbkCoupon -> {
            tbkCoupon.setCommissionRate(String.format(Locale.ENGLISH, "%.2f",
                    userRate * Double.parseDouble(tbkCoupon.getCommissionRate())));
        }).collect(Collectors.toList()));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void refreshKeyWord(String keyword) {
        ESearchKeyWord one = this.searchKeyWordRepo.findByKeywordEquals(keyword);
        if (null == one) {
            one = new ESearchKeyWord();
            one.setKeyword(keyword);
            one.setCount(0L);
        }
        one.setCount(one.getCount() + 1);

        this.searchKeyWordRepo.save(one);
    }

    @Override
    public Object getTopSearchWords() {
        return Result.success(this.searchKeyWordRepo.findTop5ByOrderByCountDesc()
                .stream().map(ESearchKeyWord::getKeyword).collect(Collectors.toList()));
    }

    @Override
    public Object hints(String keyword) {
        return Result.success(this.searchKeyWordRepo.findAllByKeywordContains(keyword)
                .stream().map(ESearchKeyWord::getKeyword).collect(Collectors.toList()));
    }

    @Override
    public Map<Long, ETbkItem> loadSimpleItem(List<Long> id) {
        Stream<ETbkItem> eTbkItemStream = id.stream().distinct().map(nid -> this.tbkItemRepo.findOne(nid));
        Map<Long, ETbkItem> found = eTbkItemStream.filter(Objects::nonNull).collect(Collectors.toMap(ETbkItem::getId, Function.identity()));

        List<Long> notFound = id.stream().distinct().filter(nid -> !found.keySet().contains(nid)).collect(Collectors.toList());
        if (notFound.isEmpty()) {
            return found;
        }

        StringBuilder num_iids = new StringBuilder();
        notFound.forEach(nid -> num_iids.append(nid).append(','));
        String numIids = num_iids.toString();

        this.initParams();
        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setFields("num_iid,pict_url");
        req.setPlatform(2L);
        req.setNumIids(numIids.substring(0, numIids.length() - 1));
        TbkItemInfoGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error("error when invoke ali api" + e.getMessage());
            return found;
        }
        logger.debug(rsp.getBody());

        List<ETbkItem> toSave = rsp.getResults().stream().map(nTbkItem -> {
            ETbkItem value = new ETbkItem();
            value.setId(nTbkItem.getNumIid());
            value.setPicUrl(nTbkItem.getPictUrl());
            found.put(nTbkItem.getNumIid(), value);

            return value;
        }).collect(Collectors.toList());
        this.tbkItemRepo.save(toSave);
        return found;
    }

    @Override
    public Object getJuItems(EUser user, String keyword) {
        this.initParams();
        TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", "24677166", "c415a630e93808f2e56a985097c5ea20");
        JuItemsSearchRequest req = new JuItemsSearchRequest();
        JuItemsSearchRequest.TopItemQuery obj1 = new JuItemsSearchRequest.TopItemQuery();
        obj1.setCurrentPage(1L);
        obj1.setPageSize(100L);
        obj1.setPid(user.getAliPid());
        obj1.setStatus(2L);
        obj1.setWord(keyword);
        req.setParamTopItemQuery(obj1);
        JuItemsSearchResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error("error when invoke ali api" + e.getMessage());
            return Result.fail(new ErrorR(ErrorR.FAIL_ON_ALI_API, FAIL_ON_ALI_API));
        }
        logger.debug(rsp.getBody());

        this.refreshKeyWord(keyword);

        List<JuItemsSearchResponse.Items> modelList;
        try {
            modelList = rsp.getResult().getModelList();
            Objects.requireNonNull(modelList);
        } catch (Exception e) {
            modelList = Collections.emptyList();
        }

        return Result.success(modelList);
    }

    private String getTaobaoPwd(String text, String url) throws ApiException {
        this.initParams();
        TaobaoClient client = new DefaultTaobaoClient(this.serverUrl, this.appKey, this.secret);
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        req.setUserId("3434155161");
        req.setText(text);
        req.setUrl(url);
        TbkTpwdCreateResponse rsp = client.execute(req);
        logger.debug(rsp.getBody());
        return rsp.getData().getModel();
    }

    private String getShortUrl(String url) throws ApiException {
        this.initParams();
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
