package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbk_order")
public class ETbkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "click_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date clickTime;

    @Column(name = "item_title", nullable = false)
    private String itemTitle;

    @Column(name = "item_numIid", nullable = false)
    private Long itemNumIid;

    @Column(name = "wangwangName", nullable = false)
    private String wangwangName;

    @Column(name = "shopTitle", nullable = false)
    private String shopTitle;

    @Column(name = "item_count", nullable = false)
    private Long itemCount;

    @Column(name = "item_unit_price", nullable = false)
    private String itemUnitPrice;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "order_type", nullable = false)
    private String orderType;

    @Column(name = "income_rate", nullable = false)
    private String incomeRate;

    @Column(name = "divide_rate", nullable = false)
    private String divideRate;

    @Column(name = "payed_amount", nullable = false)
    private String payedAmount;

    @Column(name = "estimate_effect", nullable = false)
    private String estimateEffect;

    @Column(name = "settle_amount", nullable = false)
    private String settleAmount;

    @Column(name = "estimate_income", nullable = false)
    private String estimateIncome;

    @Column(name = "settle_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settleTime;

    @Column(name = "commission_rate", nullable = false)
    private String commissionRate;

    @Column(name = "commission_amount", nullable = false)
    private String commissionAmount;

    @Column(name = "subsidy_rate", nullable = false)
    private String subsidyRate;

    @Column(name = "subsidy_amount", nullable = false)
    private String subsidyAmount;

    @Column(name = "subsidy_type", nullable = false)
    private String subsidyType;

    @Column(name = "platform", nullable = false)
    private String platform;

    @Column(name = "3rd_service", nullable = false)
    private String service3rd;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "site_id", nullable = false)
    private Long siteId;

    @Column(name = "site_name", nullable = false)
    private String siteName;

    @Column(name = "adzone_id", nullable = false)
    private Long adZoneId;

    @Column(name = "adzone_name", nullable = false)
    private String adZoneName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getClickTime() {
        return clickTime;
    }

    public void setClickTime(Date clickTime) {
        this.clickTime = clickTime;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Long getItemNumIid() {
        return itemNumIid;
    }

    public void setItemNumIid(Long itemNumIid) {
        this.itemNumIid = itemNumIid;
    }

    public String getWangwangName() {
        return wangwangName;
    }

    public void setWangwangName(String wangwangName) {
        this.wangwangName = wangwangName;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public Long getItemCount() {
        return itemCount;
    }

    public void setItemCount(Long itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(String itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getIncomeRate() {
        return incomeRate;
    }

    public void setIncomeRate(String incomeRate) {
        this.incomeRate = incomeRate;
    }

    public String getDivideRate() {
        return divideRate;
    }

    public void setDivideRate(String divideRate) {
        this.divideRate = divideRate;
    }

    public String getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(String payedAmount) {
        this.payedAmount = payedAmount;
    }

    public String getEstimateEffect() {
        return estimateEffect;
    }

    public void setEstimateEffect(String estimateEffect) {
        this.estimateEffect = estimateEffect;
    }

    public String getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(String settleAmount) {
        this.settleAmount = settleAmount;
    }

    public String getEstimateIncome() {
        return estimateIncome;
    }

    public void setEstimateIncome(String estimateIncome) {
        this.estimateIncome = estimateIncome;
    }

    public Date getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getSubsidyRate() {
        return subsidyRate;
    }

    public void setSubsidyRate(String subsidyRate) {
        this.subsidyRate = subsidyRate;
    }

    public String getSubsidyAmount() {
        return subsidyAmount;
    }

    public void setSubsidyAmount(String subsidyAmount) {
        this.subsidyAmount = subsidyAmount;
    }

    public String getSubsidyType() {
        return subsidyType;
    }

    public void setSubsidyType(String subsidyType) {
        this.subsidyType = subsidyType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getService3rd() {
        return service3rd;
    }

    public void setService3rd(String service3rd) {
        this.service3rd = service3rd;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Long getAdZoneId() {
        return adZoneId;
    }

    public void setAdZoneId(Long adZoneId) {
        this.adZoneId = adZoneId;
    }

    public String getAdZoneName() {
        return adZoneName;
    }

    public void setAdZoneName(String adZoneName) {
        this.adZoneName = adZoneName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ETbkOrder eTbkOrder = (ETbkOrder) o;

        if (!orderId.equals(eTbkOrder.orderId)) return false;
        if (!createTime.equals(eTbkOrder.createTime)) return false;
        if (!clickTime.equals(eTbkOrder.clickTime)) return false;
        if (!itemTitle.equals(eTbkOrder.itemTitle)) return false;
        if (!itemNumIid.equals(eTbkOrder.itemNumIid)) return false;
        if (!wangwangName.equals(eTbkOrder.wangwangName)) return false;
        if (!shopTitle.equals(eTbkOrder.shopTitle)) return false;
        if (!itemCount.equals(eTbkOrder.itemCount)) return false;
        if (!itemUnitPrice.equals(eTbkOrder.itemUnitPrice)) return false;
        if (!orderStatus.equals(eTbkOrder.orderStatus)) return false;
        if (!orderType.equals(eTbkOrder.orderType)) return false;
        if (!incomeRate.equals(eTbkOrder.incomeRate)) return false;
        if (!divideRate.equals(eTbkOrder.divideRate)) return false;
        if (!payedAmount.equals(eTbkOrder.payedAmount)) return false;
        if (!estimateEffect.equals(eTbkOrder.estimateEffect)) return false;
        if (!settleAmount.equals(eTbkOrder.settleAmount)) return false;
        if (!estimateIncome.equals(eTbkOrder.estimateIncome)) return false;
        if (settleTime != null ? !settleTime.equals(eTbkOrder.settleTime) : eTbkOrder.settleTime != null) return false;
        if (!commissionRate.equals(eTbkOrder.commissionRate)) return false;
        if (!commissionAmount.equals(eTbkOrder.commissionAmount)) return false;
        if (!subsidyRate.equals(eTbkOrder.subsidyRate)) return false;
        if (!subsidyAmount.equals(eTbkOrder.subsidyAmount)) return false;
        if (!subsidyType.equals(eTbkOrder.subsidyType)) return false;
        if (!platform.equals(eTbkOrder.platform)) return false;
        if (!service3rd.equals(eTbkOrder.service3rd)) return false;
        if (!category.equals(eTbkOrder.category)) return false;
        if (!siteId.equals(eTbkOrder.siteId)) return false;
        if (!siteName.equals(eTbkOrder.siteName)) return false;
        if (!adZoneId.equals(eTbkOrder.adZoneId)) return false;
        return adZoneName.equals(eTbkOrder.adZoneName);
    }

    @Override
    public int hashCode() {
        int result = orderId.hashCode();
        result = 31 * result + createTime.hashCode();
        result = 31 * result + clickTime.hashCode();
        result = 31 * result + itemTitle.hashCode();
        result = 31 * result + itemNumIid.hashCode();
        result = 31 * result + wangwangName.hashCode();
        result = 31 * result + shopTitle.hashCode();
        result = 31 * result + itemCount.hashCode();
        result = 31 * result + itemUnitPrice.hashCode();
        result = 31 * result + orderStatus.hashCode();
        result = 31 * result + orderType.hashCode();
        result = 31 * result + incomeRate.hashCode();
        result = 31 * result + divideRate.hashCode();
        result = 31 * result + payedAmount.hashCode();
        result = 31 * result + estimateEffect.hashCode();
        result = 31 * result + settleAmount.hashCode();
        result = 31 * result + estimateIncome.hashCode();
        result = 31 * result + (settleTime != null ? settleTime.hashCode() : 0);
        result = 31 * result + commissionRate.hashCode();
        result = 31 * result + commissionAmount.hashCode();
        result = 31 * result + subsidyRate.hashCode();
        result = 31 * result + subsidyAmount.hashCode();
        result = 31 * result + subsidyType.hashCode();
        result = 31 * result + platform.hashCode();
        result = 31 * result + service3rd.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + siteId.hashCode();
        result = 31 * result + siteName.hashCode();
        result = 31 * result + adZoneId.hashCode();
        result = 31 * result + adZoneName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ETbkOrder{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", createTime=" + createTime +
                ", clickTime=" + clickTime +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemNumIid=" + itemNumIid +
                ", wangwangName='" + wangwangName + '\'' +
                ", shopTitle='" + shopTitle + '\'' +
                ", itemCount=" + itemCount +
                ", itemUnitPrice='" + itemUnitPrice + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderType='" + orderType + '\'' +
                ", incomeRate='" + incomeRate + '\'' +
                ", divideRate='" + divideRate + '\'' +
                ", payedAmount='" + payedAmount + '\'' +
                ", estimateEffect='" + estimateEffect + '\'' +
                ", settleAmount='" + settleAmount + '\'' +
                ", estimateIncome='" + estimateIncome + '\'' +
                ", settleTime=" + settleTime +
                ", commissionRate='" + commissionRate + '\'' +
                ", commissionAmount='" + commissionAmount + '\'' +
                ", subsidyRate='" + subsidyRate + '\'' +
                ", subsidyAmount='" + subsidyAmount + '\'' +
                ", subsidyType='" + subsidyType + '\'' +
                ", platform='" + platform + '\'' +
                ", service3rd='" + service3rd + '\'' +
                ", category='" + category + '\'' +
                ", siteId=" + siteId +
                ", siteName='" + siteName + '\'' +
                ", adZoneId=" + adZoneId +
                ", adZoneName='" + adZoneName + '\'' +
                '}';
    }
}
