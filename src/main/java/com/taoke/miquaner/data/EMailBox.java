package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mail_box")
public class EMailBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "msg_id")
    private EMessage message;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id")
    private EUser senderUser;

    @Column(name = "send_from_admin", nullable = false)
    private Boolean isSendFromAdmin;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_user_id")
    private EUser receiverUser;

    @Column(name = "admin_to_receive", nullable = false)
    private Boolean isAdminToReceive;

    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "checked", nullable = false)
    private Boolean checked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EMessage getMessage() {
        return message;
    }

    public void setMessage(EMessage message) {
        this.message = message;
    }

    public EUser getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(EUser senderUser) {
        this.senderUser = senderUser;
    }

    public Boolean getSendFromAdmin() {
        return isSendFromAdmin;
    }

    public void setSendFromAdmin(Boolean sendFromAdmin) {
        isSendFromAdmin = sendFromAdmin;
    }

    public EUser getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(EUser receiverUser) {
        this.receiverUser = receiverUser;
    }

    public Boolean getAdminToReceive() {
        return isAdminToReceive;
    }

    public void setAdminToReceive(Boolean adminToReceive) {
        isAdminToReceive = adminToReceive;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
