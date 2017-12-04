package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "privilege")
public class EPrivilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "api", length = 2048, nullable = false)
    private String api;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "android_v")
    private String androidVersion;

    @Column(name = "ios_v")
    private String iOSVersion;

    @Column(name = "web_v")
    private String webVersion;

    @Column(name = "is_admin", nullable = false)
    private Boolean admin;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "role_privilege",
            joinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<ERole> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public List<ERole> getRoles() {
        return roles;
    }

    public void setRoles(List<ERole> roles) {
        this.roles = roles;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getiOSVersion() {
        return iOSVersion;
    }

    public void setiOSVersion(String iOSVersion) {
        this.iOSVersion = iOSVersion;
    }

    public String getWebVersion() {
        return webVersion;
    }

    public void setWebVersion(String webVersion) {
        this.webVersion = webVersion;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "EPrivilege{" +
                "id=" + id +
                ", api='" + api + '\'' +
                ", method='" + method + '\'' +
                ", androidVersion='" + androidVersion + '\'' +
                ", iOSVersion='" + iOSVersion + '\'' +
                ", webVersion='" + webVersion + '\'' +
                ", admin=" + admin +
                '}';
    }
}
