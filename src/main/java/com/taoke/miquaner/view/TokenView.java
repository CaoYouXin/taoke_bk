package com.taoke.miquaner.view;

import com.taoke.miquaner.data.EToken;

public class TokenView extends EToken {

    private Boolean candidate;
    private Boolean directUser;

    public Boolean getCandidate() {
        return candidate;
    }

    public void setCandidate(Boolean candidate) {
        this.candidate = candidate;
    }

    public Boolean getDirectUser() {
        return directUser;
    }

    public void setDirectUser(Boolean directUser) {
        this.directUser = directUser;
    }
}
