package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "help_doc")
public class EHelpDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "path", length = 1024, nullable = false)
    private String path;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "idx", nullable = false)
    private Integer order = Integer.MAX_VALUE;

    // if share code == null
    //     if candidate == true
    //         if direct user == true
    //             type = 1
    //          else
    //             type = 2
    //     else
    //         type = 3
    // else
    //     if direct user == true
    //         type = 4
    //     else
    //         type = 5
    @Column(name = "type", nullable = false)
    private Integer type = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "EHelpDoc{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", type=" + type +
                '}';
    }
}
