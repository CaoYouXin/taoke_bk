package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "search_keys")
public class ESearchKeyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "word", unique = true, nullable = false)
    private String keyword;

    @Column(name = "count", nullable = false)
    private Long count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ESearchKeyWord{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                ", count=" + count +
                '}';
    }
}
