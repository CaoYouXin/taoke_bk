package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "favorite_order")
public class EFavoriteOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "favorite_id", nullable = false)
    private Long favoriteId;

    @Column(name = "idx", nullable = false)
    private Integer order;

    @Column(name = "num_iid", nullable = false)
    private Long numIid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getNumIid() {
        return numIid;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    @Override
    public String toString() {
        return "EFavoriteOrder{" +
                "id=" + id +
                ", favoriteId=" + favoriteId +
                ", order=" + order +
                ", numIid=" + numIid +
                '}';
    }
}
