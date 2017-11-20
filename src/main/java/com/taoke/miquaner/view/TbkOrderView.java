package com.taoke.miquaner.view;

import com.taoke.miquaner.data.ETbkOrder;

public class TbkOrderView extends ETbkOrder {

    private boolean self;
    private String teammateName;
    private String picUrl;

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public String getTeammateName() {
        return teammateName;
    }

    public void setTeammateName(String teammateName) {
        this.teammateName = teammateName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
