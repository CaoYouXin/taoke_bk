package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "withdraw")
public class EWithdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EUser user;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "payed", nullable = false)
    private Boolean payed;

    @Column(name = "pay_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EUser getUser() {
        return user;
    }

    public void setUser(EUser user) {
        this.user = user;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
