package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "tbk_item_simple")
public class ETbkItem {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "pic_url", length = 2048, nullable = false)
    private String picUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
