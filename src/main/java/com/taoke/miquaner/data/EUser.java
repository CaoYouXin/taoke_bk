package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class EUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    private EUser pUser;

    @OneToMany(mappedBy = "pUser")
    private List<EUser> cUsers = new ArrayList<>();

    @Column(name = "phone", unique = true, length = 32, nullable = false)
    private String phone;

    @Column(name = "u_pwd", length = 32, nullable = false)
    private String pwd;

    @Column(name = "name", unique = true, length = 32, nullable = false)
    private String name;

    @Column(name = "real_name", length = 32)
    private String realName;

    @Column(name = "aliPayId")
    private String aliPayId;

    @Column(name = "qqId")
    private String qqId;

    @Column(name = "weChatId")
    private String weChatId;

    @Column(name = "announcement", length = 1024)
    private String announcement;

    @Column(name = "aliPid", length = 1024, unique = true)
    private String aliPid;

    @Column(name = "code", unique = true, length = 10)
    private String code;

    @Column(name = "ext")
    private String ext;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EUser getpUser() {
        return pUser;
    }

    public void setpUser(EUser pUser) {
        this.pUser = pUser;
    }

    public List<EUser> getcUsers() {
        return cUsers;
    }

    public void setcUsers(List<EUser> cUsers) {
        this.cUsers = cUsers;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAliPayId() {
        return aliPayId;
    }

    public void setAliPayId(String aliPayId) {
        this.aliPayId = aliPayId;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
