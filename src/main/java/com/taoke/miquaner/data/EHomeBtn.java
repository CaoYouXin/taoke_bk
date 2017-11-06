package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "home_btn")
public class EHomeBtn {

    public static final Integer BANNER = 1;
    public static final Integer TOOL = 1 << 1;
    public static final Integer GROUP = 1 << 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "img_url", nullable = false, length = 2048)
    private String imgUrl;

    @Column(name = "location", nullable = false)
    private Integer locationType;

    @Column(name = "idx", nullable = false)
    private Integer order;

    @Column(name = "type", nullable = false)
    private Integer openType;

    @Column(name = "ext", nullable = false, length = 102400)
    private String ext;

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

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
