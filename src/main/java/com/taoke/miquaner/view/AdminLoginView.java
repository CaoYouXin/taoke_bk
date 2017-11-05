package com.taoke.miquaner.view;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EToken;

public class AdminLoginView {

    private EAdmin admin;
    private EToken token;

    public AdminLoginView(EAdmin admin, EToken token) {
        this.admin = admin;
        this.token = token;
    }

    public EAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(EAdmin admin) {
        this.admin = admin;
    }

    public EToken getToken() {
        return token;
    }

    public void setToken(EToken token) {
        this.token = token;
    }
}
