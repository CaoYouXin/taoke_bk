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

    @Column(name = "name", unique = true, length = 32, nullable = false)
    private String name;

    @Column(name = "pwd", length = 32, nullable = false)
    private String pwd;

    @Column(name = "isDel")
    private Boolean isDeleted;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "grant_by_id")
    private EAdmin parentAdmin;

    @OneToMany(mappedBy = "parentAdmin", fetch = FetchType.LAZY)
    private List<EAdmin> grantedAdmins = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private ERole role;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<EMessage> createdMessages = new ArrayList<>();

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

    public List<EMessage> getCreatedMessages() {
        return createdMessages;
    }

    public void setCreatedMessages(List<EMessage> createdMessages) {
        this.createdMessages = createdMessages;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "EAdmin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", isDeleted=" + isDeleted +
                ", parentAdmin=" + parentAdmin +
                ", grantedAdmins=" + grantedAdmins +
                ", role=" + role +
                ", createdMessages=" + createdMessages +
                '}';
    }
}
