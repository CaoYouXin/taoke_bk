package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "jd_token")
public class EJdToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uid", nullable = false)
    private String uid;

    @Column(name = "user_nick", nullable = false)
    private String userNick;

    @Column(name = "token_type", nullable = false)
    private String tokenType;

    @Column(name = "expire_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

}
