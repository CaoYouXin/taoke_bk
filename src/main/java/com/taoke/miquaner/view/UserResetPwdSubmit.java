package com.taoke.miquaner.view;

import com.taoke.miquaner.data.EUser;

public class UserResetPwdSubmit extends EUser {

    private String smsCode;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
