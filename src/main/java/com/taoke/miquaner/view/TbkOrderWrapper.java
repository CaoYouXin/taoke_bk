package com.taoke.miquaner.view;

import com.taoke.miquaner.data.ETbkOrder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

public class TbkOrderWrapper extends ETbkOrder {

    //    _NONE(-1),
//    NUMERIC(0),
//    STRING(1),
//    FORMULA(2),
//    BLANK(3),
//    BOOLEAN(4),
//    ERROR(5);
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TOW {
        String value();

        int type();

        boolean double2long() default false;

        boolean string2date() default false;

        boolean double2string() default false;

        boolean string2long() default false;
    }

    public ETbkOrder getEntity() {
        return this;
    }

    @Override
    @TOW(value = "订单编号", type = 1, string2long = true)
    public void setOrderId(Long id) {
        super.setOrderId(id);
    }

    @Override
    @TOW(value = "创建时间", type = 1, string2date = true)
    public void setCreateTime(Date createTime) {
        super.setCreateTime(createTime);
    }

    @Override
    @TOW(value = "点击时间", type = 1, string2date = true)
    public void setClickTime(Date clickTime) {
        super.setClickTime(clickTime);
    }

    @Override
    @TOW(value = "商品信息", type = 1)
    public void setItemTitle(String itemTitle) {
        super.setItemTitle(itemTitle);
    }

    @Override
    @TOW(value = "商品ID", type = 1, string2long = true)
    public void setItemNumIid(Long itemNumIid) {
        super.setItemNumIid(itemNumIid);
    }

    @Override
    @TOW(value = "掌柜旺旺", type = 1)
    public void setWangwangName(String wangwangName) {
        super.setWangwangName(wangwangName);
    }

    @Override
    @TOW(value = "所属店铺", type = 1)
    public void setShopTitle(String shopTitle) {
        super.setShopTitle(shopTitle);
    }

    @Override
    @TOW(value = "商品数", type = 0, double2long = true)
    public void setItemCount(Long itemCount) {
        super.setItemCount(itemCount);
    }

    @Override
    @TOW(value = "商品单价", type = 0, double2string = true)
    public void setItemUnitPrice(String itemUnitPrice) {
        super.setItemUnitPrice(itemUnitPrice);
    }

    @Override
    @TOW(value = "订单状态", type = 1)
    public void setOrderStatus(String orderStatus) {
        super.setOrderStatus(orderStatus);
    }

    @Override
    @TOW(value = "订单类型", type = 1)
    public void setOrderType(String orderType) {
        super.setOrderType(orderType);
    }

    @Override
    @TOW(value = "收入比率", type = 1)
    public void setIncomeRate(String incomeRate) {
        super.setIncomeRate(incomeRate);
    }

    @Override
    @TOW(value = "分成比率", type = 1)
    public void setDivideRate(String divideRate) {
        super.setDivideRate(divideRate);
    }

    @Override
    @TOW(value = "付款金额", type = 0, double2string = true)
    public void setPayedAmount(String payedAmount) {
        super.setPayedAmount(payedAmount);
    }

    @Override
    @TOW(value = "效果预估", type = 0, double2string = true)
    public void setEstimateEffect(String estimateEffect) {
        super.setEstimateEffect(estimateEffect);
    }

    @Override
    @TOW(value = "结算金额", type = 0, double2string = true)
    public void setSettleAmount(String settleAmount) {
        super.setSettleAmount(settleAmount);
    }

    @Override
    @TOW(value = "预估收入", type = 0, double2string = true)
    public void setEstimateIncome(String estimateIncome) {
        super.setEstimateIncome(estimateIncome);
    }

    @Override
    @TOW(value = "结算时间", type = 1, string2date = true)
    public void setSettleTime(Date settleTime) {
        super.setSettleTime(settleTime);
    }

    @Override
    @TOW(value = "佣金比率", type = 1)
    public void setCommissionRate(String commissionRate) {
        super.setCommissionRate(commissionRate);
    }

    @Override
    @TOW(value = "佣金金额", type = 0, double2string = true)
    public void setCommissionAmount(String commissionAmount) {
        super.setCommissionAmount(commissionAmount);
    }

    @Override
    @TOW(value = "补贴比率", type = 1)
    public void setSubsidyRate(String subsidyRate) {
        super.setSubsidyRate(subsidyRate);
    }

    @Override
    @TOW(value = "补贴金额", type = 0, double2string = true)
    public void setSubsidyAmount(String subsidyAmount) {
        super.setSubsidyAmount(subsidyAmount);
    }

    @Override
    @TOW(value = "补贴类型", type = 1)
    public void setSubsidyType(String subsidyType) {
        super.setSubsidyType(subsidyType);
    }

    @Override
    @TOW(value = "成交平台", type = 1)
    public void setPlatform(String platform) {
        super.setPlatform(platform);
    }

    @Override
    @TOW(value = "第三方服务来源", type = 1)
    public void setService3rd(String service3rd) {
        super.setService3rd(service3rd);
    }

    @Override
    @TOW(value = "类目名称", type = 1)
    public void setCategory(String category) {
        super.setCategory(category);
    }

    @Override
    @TOW(value = "来源媒体ID", type = 1, string2long = true)
    public void setSiteId(Long siteId) {
        super.setSiteId(siteId);
    }

    @Override
    @TOW(value = "来源媒体名称", type = 1)
    public void setSiteName(String siteName) {
        super.setSiteName(siteName);
    }

    @Override
    @TOW(value = "广告位ID", type = 1, string2long = true)
    public void setAdZoneId(Long adZoneId) {
        super.setAdZoneId(adZoneId);
    }

    @Override
    @TOW(value = "广告位名称", type = 1)
    public void setAdZoneName(String adZoneName) {
        super.setAdZoneName(adZoneName);
    }
}
