package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
public class ERole {

    public static final String SUPER_ROLE_NAME = "super role";

    public boolean isSuperRole() {
        return SUPER_ROLE_NAME.equals(this.name);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<EAdmin> admins = new ArrayList<>();

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<EPrivilege> privileges = new ArrayList<>();

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
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

    public List<EAdmin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<EAdmin> admins) {
        this.admins = admins;
    }

    @Override
    public String toString() {
        return "ERole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", admins=" + admins +
                ", privileges=" + privileges +
                ", menus=" + menus +
                '}';
    }
}
