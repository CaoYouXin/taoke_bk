package com.taoke.miquaner.ctrl;

import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.UserLoginView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UserCtrl {

    @RequestMapping(value = "/tbk/user/login", method = RequestMethod.POST)
    public Object login() {
        UserLoginView body = new UserLoginView();
        body.setToken(StringUtil.toMD5HexString(MiquanerApplication.DEFAULT_DATE_FORMAT.format(new Date())));
        return Result.success(body);
    }

}
