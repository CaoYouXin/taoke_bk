package com.taoke.miquaner.data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bi_item_detail_clicked")
public class EBiItemDetailClicked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EUser user;

    @Column(name = "time_point", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timePoint;

    @Column(name = "tp_year", nullable = false)
    private Short year;

    @Column(name = "tp_month", nullable = false)
    private Byte month;

    @Column(name = "tp_day", nullable = false)
    private Byte day;

    @Column(name = "tp_hour", nullable = false)
    private Byte hour;

    @Column(name = "tp_minute", nullable = false)
    private Byte minute;

    @Column(name = "tp_second", nullable = false)
    private Byte second;

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

    public Date getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Date timePoint) {
        this.timePoint = timePoint;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Byte getMonth() {
        return month;
    }

    public void setMonth(Byte month) {
        this.month = month;
    }

    public Byte getDay() {
        return day;
    }

    public void setDay(Byte day) {
        this.day = day;
    }

    public Byte getHour() {
        return hour;
    }

    public void setHour(Byte hour) {
        this.hour = hour;
    }

    public Byte getMinute() {
        return minute;
    }

    public void setMinute(Byte minute) {
        this.minute = minute;
    }

    public Byte getSecond() {
        return second;
    }

    public void setSecond(Byte second) {
        this.second = second;
    }
}
