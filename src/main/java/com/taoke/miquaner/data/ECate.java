package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class ECate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "tb_cid", nullable = false)
    private String cid;

    @Column(name = "idx", nullable = false)
    private Integer order;

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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ECate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cid='" + cid + '\'' +
                ", order=" + order +
                '}';
    }
}
