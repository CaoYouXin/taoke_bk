package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
public class ERole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<EAdmin> records = new ArrayList<>();

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<EPrivilege> privileges = new ArrayList<>();

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<EMenu> menus = new ArrayList<>();

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

    public List<EAdmin> getRecords() {
        return records;
    }

    public void setRecords(List<EAdmin> records) {
        this.records = records;
    }

    public List<EPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<EPrivilege> privileges) {
        this.privileges = privileges;
    }

    public List<EMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<EMenu> menus) {
        this.menus = menus;
    }
}
