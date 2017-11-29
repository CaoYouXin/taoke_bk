package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "message")
public class EMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 10240)
    private String content;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private EAdmin admin;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EUser user;

    @OneToMany(mappedBy = "message")
    private List<EMailBox> mailBoxes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<EMailBox> getMailBoxes() {
        return mailBoxes;
    }

    public void setMailBoxes(List<EMailBox> mailBoxes) {
        this.mailBoxes = mailBoxes;
    }

    public EAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(EAdmin admin) {
        this.admin = admin;
    }

    public EUser getUser() {
        return user;
    }

    public void setUser(EUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "EMessage{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", admin=" + admin +
                ", user=" + user +
                ", mailBoxes=" + mailBoxes +
                '}';
    }
}
