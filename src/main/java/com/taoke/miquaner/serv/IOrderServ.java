package com.taoke.miquaner.serv;

import com.taoke.miquaner.data.EUser;

import java.io.IOException;
import java.util.List;

public interface IOrderServ {

    Object upload(String filePath) throws IOException;

    Object list(EUser user, Boolean isSuper, Integer type, Integer pageNo);

    Object getChildUserCommit(List<EUser> children);

    Object withdraw(EUser user, Double amount, Boolean isSuper);

    Object canDraw(EUser user, Boolean isSuper);

    Object lastMonthSettled(EUser user, Boolean isSuper);

    Object thisMonthSettled(EUser user, Boolean isSuper);

    Object userWithdrawList(Integer type, Integer pageNo);

    Object userWithdrawList(String key);

    Object payUserWithdraw(Long withdrawId);

}
