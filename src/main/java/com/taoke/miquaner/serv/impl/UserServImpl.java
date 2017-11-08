package com.taoke.miquaner.serv.impl;

import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.EToken;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.TokenRepo;
import com.taoke.miquaner.repo.UserRepo;
import com.taoke.miquaner.serv.IUserServ;
import com.taoke.miquaner.util.DateUtils;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.UserRegisterSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
public class UserServImpl implements IUserServ {

    private static final String NO_USER_FOUND = "没有该用户";
    private static final String USER_WRONG_PWD = "用户密码错误";
    private static final String ALREADY_REGISTERED_USER = "已经注册过，请直接登录";

    private UserRepo userRepo;
    private TokenRepo tokenRepo;

    @Autowired
    public UserServImpl(UserRepo userRepo, TokenRepo tokenRepo) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
    }

    @Override
    public Object login(EUser user) {
        EUser one = this.userRepo.findByPhoneEquals(user.getPhone());
        if (null == one) {
            return Result.fail(new ErrorR(ErrorR.NO_USER_FOUND, NO_USER_FOUND));
        }

        if (!one.getPwd().equals(user.getPwd())) {
            return Result.fail(new ErrorR(ErrorR.USER_WRONG_PWD, USER_WRONG_PWD));
        }

        EToken token = this.tokenRepo.findByAdmin_Id(one.getId());
        if (null == token) {
            token = new EToken();
            token.setUser(one);
        }
        Date now = new Date();
        token.setToken(StringUtil.toMD5HexString(MiquanerApplication.DEFAULT_DATE_FORMAT.format(now)));
        token.setExpired(DateUtils.add(now, Calendar.DAY_OF_YEAR, 3));
        EToken eToken = this.tokenRepo.save(token);

        EUser eUser = new EUser();
        eUser.setName(one.getName());
        eUser.setPhone(one.getPhone());
        eToken.setUser(eUser);

        return Result.success(eToken);
    }

    @Override
    public Object register(UserRegisterSubmit userRegisterSubmit) {
        EUser byPhoneEquals = this.userRepo.findByPhoneEquals(userRegisterSubmit.getUser().getPhone());
        if (null != byPhoneEquals) {
            return Result.fail(new ErrorR(ErrorR.ALREADY_REGISTERED_USER, ALREADY_REGISTERED_USER));
        }

        userRegisterSubmit.getUser().setName("觅" + ("" + Math.random()).substring(2, 10));
        EUser saved = this.userRepo.save(userRegisterSubmit.getUser());

        EToken token = this.tokenRepo.findByAdmin_Id(saved.getId());
        if (null == token) {
            token = new EToken();
            token.setUser(saved);
        }
        Date now = new Date();
        token.setToken(StringUtil.toMD5HexString(MiquanerApplication.DEFAULT_DATE_FORMAT.format(now)));
        token.setExpired(DateUtils.add(now, Calendar.DAY_OF_YEAR, 3));
        EToken eToken = this.tokenRepo.save(token);

        EUser eUser = new EUser();
        eUser.setName(saved.getName());
        eUser.setPhone(saved.getPhone());
        eToken.setUser(eUser);

        return Result.success(eToken);
    }

}
