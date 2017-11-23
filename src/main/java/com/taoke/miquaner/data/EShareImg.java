package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "share_img")
public class EShareImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idx", nullable = false)
    private Integer order;

    @Column(name = "img_url", unique = true, nullable = false, length = 2048)
    private String imgUrl;

    @Column(name = "type", nullable = false)
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
