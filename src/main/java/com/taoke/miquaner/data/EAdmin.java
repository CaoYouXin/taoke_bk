package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin")
public class EAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "pwd", length = 32, nullable = false)
    private String pwd;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "grant_by_id")
    private EAdmin parentAdmin;

    @OneToMany(mappedBy = "parentAdmin", fetch = FetchType.LAZY)
    private List<EAdmin> grantedAdmins = new ArrayList<>();

    @ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private ERole role;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public EAdmin getParentAdmin() {
        return parentAdmin;
    }

    public void setParentAdmin(EAdmin parentAdmin) {
        this.parentAdmin = parentAdmin;
    }

    public List<EAdmin> getGrantedAdmins() {
        return grantedAdmins;
    }

    public void setGrantedAdmins(List<EAdmin> grantedAdmins) {
        this.grantedAdmins = grantedAdmins;
    }
}
