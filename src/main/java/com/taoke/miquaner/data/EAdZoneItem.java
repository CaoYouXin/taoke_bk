package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "ad_zone_item")
public class EAdZoneItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "img_url", nullable = false, length = 2048)
    private String imgUrl;

    @Column(name = "col_span", nullable = false)
    private Integer colSpan;

    @Column(name = "row_span", nullable = false)
    private Integer rowSpan;

    @Column(name = "type", nullable = false)
    private Integer openType;

    @Column(name = "idx", nullable = false)
    private Integer order;

    @Column(name = "ios_idx", nullable = false)
    private Integer iosOrder;

    @Column(name = "ext", nullable = false, length = 102400)
    private String ext;

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Integer getIosOrder() {
        return iosOrder;
    }

    public void setIosOrder(Integer iosOrder) {
        this.iosOrder = iosOrder;
    }

    @Override
    public String toString() {
        return "EAdZoneItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", colSpan=" + colSpan +
                ", rowSpan=" + rowSpan +
                ", openType=" + openType +
                ", order=" + order +
                ", iosOrder=" + iosOrder +
                ", ext='" + ext + '\'' +
                '}';
    }
}
