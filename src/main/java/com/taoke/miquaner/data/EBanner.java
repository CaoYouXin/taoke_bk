package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "banner")
public class EBanner {

    public static final Integer IN_APP_WEB_VIEW = 1;
    public static final Integer IN_SYS_WEB_VIEW = 1 << 1;
    public static final Integer RESERVED = 1 << 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "img_url", nullable = false, length = 2048)
    private String imgUrl;

    @Column(name = "idx", nullable = false)
    private Integer order;

    @Column(name = "type", nullable = false)
    private Integer type;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
